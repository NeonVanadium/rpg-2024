package view.window;

import view.ViewConstants;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Set;

class Label implements IRectangle {

	private static int TYPE_SPEED = 3; // how many characters are typed for update.

	protected String text;
	protected String wrappedText;
	protected Color color; //text color
	protected float fontSize;
	protected String clickValue = null; // what value will be returned to the controller if this label is clicked?

	private Label parent; // may be null, the label relative to which this is positioned
	private AlignmentLocation vert;
	private AlignmentLocation horz;
	private int width; //how wide the label is (set by wrapText())
	private int height; //how tall the label is (set by wrapText())
	private PanelZone zone;
	private boolean isTypewriter = true;
	private int typedCharacters = 0;
	private static Set<String> keywords; // these will be highlighted if they appear in the text.

	private static final String EMPTY = "";
	
	// Has the text changed since the previous wrapping?  If true, wrapText will be called.
	// This system is in place is because wrapText requires a graphics object which will not be present
	// when calling setText.
	private boolean textChanged = true; 
	
	// since the y position of the label is below the first line, this stores the y-coord above the first line, 
	// for use in contains.
	private int trueTop; 
	
	// if this label is an exit button or similar which needs to be deleted when the 
	// displayed data is updated
	private boolean isTemporary; 	

	// abstraction constructor
	private Label(String name, Color color, float fontSize, WolgonPanel panel) {
		this.text = EMPTY;
		this.color = color;
		this.fontSize = fontSize;
		panel.addLabel(name, this);
	}
	
	// Positions label relative to one of the AlignmentLocations (see enum in Label) of its zone
	public Label(String name, Color color, float fontSize, AlignmentLocation horz,
							 AlignmentLocation vert, String zoneName, WolgonPanel panel) {
		
		this(name, color, fontSize, panel);
		this.zone = panel.getZone(zoneName);
		this.horz = horz;
		this.vert = vert;
	}

	// Positions label below an existing label.
	public Label(String name, Color color, float fontSize, String otherLabelName, WolgonPanel panel) {
		this(name, color, fontSize, panel);
		this.parent = panel.getLabel(otherLabelName);
		this.zone = parent.zone;
	}
	
	protected void draw(Graphics g) {
		resetTextAndFontColor(g);
		if(this.textChanged) {
			this.wrapText(g);
			if (!this.isTypewriter) {
				typedCharacters = getText().length();
				drawText(g);
			}
		}
		if (!doneTyping()) {
			typedCharacters += TYPE_SPEED;
			if (typedCharacters < getText().length() && getText().charAt(typedCharacters - 1) == ViewConstants.TEXT_MODIFIER_START.charAt(0)) { // you'd think strings and chars would compare easier. Alas.
				// modifiers are of format ~(some character). Make sure we never cut that in half.
				typedCharacters += 1;
			}

		}
		drawText(g);
	}

	public boolean doneTyping() {
		return this.typedCharacters >= getText().length();
	}

	private void drawText(Graphics g) {
		int upTo = Math.min(typedCharacters, getText().length());
		int drawn = 0;
		String toDrawThisLine;
		resetTextAndFontColor(g);

		String[] lines = this.wrappedText.split("\n");
		int lineNum = 0;
		for(String line : lines) {
			toDrawThisLine = drawn + line.length() < typedCharacters ? line
					: line.substring(0, upTo - drawn);

			if (!toDrawThisLine.contains(ViewConstants.TEXT_MODIFIER_START)) {
				g.drawString(toDrawThisLine, this.getX(), (int) (this.getY() + (lineNum * this.fontSize)));
			} else {
				handleLineWithModifiers(g, toDrawThisLine, (int) (this.getY() + (lineNum * this.fontSize)));
			}

			drawn += toDrawThisLine.length();
			lineNum++;
		}
	}

	private void handleLineWithModifiers(Graphics g, String line, int lineY) {
		FontMetrics metrics = g.getFontMetrics();

		String toDraw = EMPTY;
		int widthSoFar = 0;
		for (String section : line.split(ViewConstants.TEXT_MODIFIER_START)) {
			if (widthSoFar == 0 && !line.startsWith(ViewConstants.TEXT_MODIFIER_START)) {
				toDraw = section;
				g.drawString(toDraw, this.getX() + widthSoFar, lineY);
			} else if (!section.isBlank()) {
				// one that has a modifier char at the start
				doModifier(g, section.charAt(0));
				if (section.length() > 1) {
					toDraw = section.substring(1);
					g.drawString(toDraw, this.getX() + widthSoFar, lineY);
				} else {
					toDraw = EMPTY;
				}
			}
			widthSoFar += metrics.stringWidth(toDraw);
		}
	}

	private void doModifier(Graphics g, char modifier) {
		if (modifier == 'r') { // red
			setTextColor(g, ViewConstants.YELL);
		}
		else if (modifier == 'k') { // keyword
		 	setTextColor(g, ViewConstants.KEYWORD);
		}
		else if (modifier == 'x') {
			resetColor(g);
		}
	}

	public void setTextColor(Graphics g, Color c) {
		g.setColor(c);
	}

	public void setBaseColor(Color c) {
		this.color = c;
	}

	public void resetColor(Graphics g) {
		g.setColor(this.color);
	}

	protected void resetTextAndFontColor(Graphics g) {
		g.setFont(g.getFont().deriveFont(this.fontSize));
		resetColor(g);
	}

	/**
	 * given the text and its container, inserts \n at various locations so that
	 * the text will fit cleanly in the table. Fills relevant fields on the instance.
	 */
	private void wrapText(Graphics g) {
		wrappedText = EMPTY;

		if (text.isEmpty()) {
			return;
		}

		width = 0;
		height = 0;
		trueTop = -1;

		FontMetrics metrics = g.getFontMetrics();
		int lastNewLineIndex = 0;

		String curLine = EMPTY;
		String lineAtNextSpace = EMPTY;

		//note, these indeces could also be newlines
		int curSpace = 0;
		int nextSpace;

		//based on next space and current index.
		boolean lineTooWide;

		while(indexOfEither(text, ' ', '\n', curSpace + 1) != -1) {
			nextSpace = indexOfEither(text, ' ', '\n', curSpace + 1); //index of the next space or newline
			lineAtNextSpace = text.substring(lastNewLineIndex, nextSpace); //the line including the next word

			lineTooWide = (metrics.stringWidth(lineAtNextSpace) >= zone.getBufferedWith());

			//if we need a new line
			if(curSpace > text.length() || text.charAt(curSpace) == '\n' || lineTooWide){
				newWrappedLine(curLine, g);
				lastNewLineIndex = curSpace + 1;
				curLine = this.text.substring(lastNewLineIndex, nextSpace);
			} else {
				curLine = lineAtNextSpace;
			}

			curSpace = nextSpace;
		}

		//fenceposting
		curLine = text.substring(lastNewLineIndex);
		if (metrics.stringWidth(curLine) >= zone.getBufferedWith()) {
			newWrappedLine(curLine.substring(0, curLine.lastIndexOf(' ')), g);
			newWrappedLine(curLine.substring(curLine.lastIndexOf(' ') + 1), g);
		} else {
			newWrappedLine(curLine, g);
		}
	}

	// helper for wrapLine() to accommodate for manual \n
	private int indexOfEither(String str, char first, char second, int fromIndex) {
		int indexFirst = str.indexOf(first, fromIndex);
		int indexSecond = str.indexOf(second, fromIndex);

		if(indexFirst == indexSecond) { //will only be true if these are both -1
			return -1;
		}
		else if(indexSecond == -1 || indexFirst < indexSecond) {
			return indexFirst;
		}
		else {
			return indexSecond;
		}
	}

	// helper for wrapLine() to prevent rewriting code for fenceposting
	private void newWrappedLine(String curLine, Graphics g) {
		if(g.getFontMetrics().stringWidth(curLine) > width) {
			width = g.getFontMetrics().stringWidth(curLine);
		}

		//the height of the current line of text, in pixels
		int lineHeight = (int) g.getFontMetrics().getStringBounds(curLine, g).getHeight();

		if(trueTop == -1) {
			trueTop = getY() - lineHeight;
		}

		height += lineHeight;
		wrappedText += curLine + "\n";
	}

	private void markKeywords() {
		for (String term : keywords) {
			if (text.contains(term)) {
				int index = text.indexOf(term);
				text = text.substring(0, index) + ViewConstants.TEXT_MODIFIER_START + "k" + term + ViewConstants.TEXT_MODIFIER_START + "x" + text.substring(index + term.length());
			}
		}
	}

	public static void setKeywordList(Set<String> keywords) {
		Label.keywords = keywords;
	}

	public void setText(String s) {
		if (!s.contains(text)) {
			typedCharacters = 0;
		}
		text = s;
		markKeywords();
		textChanged = true;

	}

	public String getText() {
		return text;
	}

	public void clear() {
		setText(EMPTY);
	}

	// determines the x position of this label
	public int getX() {
		if(parent == null) {
			return getAlignedHorizontalPosition();
		}
		else {
			return parent.getX();
		}
	}

	// determines the y position of this label
	public int getY() {
		if(parent == null) {
			return getAlignedVerticalPosition();
		}
		else {
			return parent.getY() + parent.height;
			//TODO: modify get aligned coords to take an IPositioner
		}
	}

	private int getAlignedVerticalPosition() {
		int halfLineHeight = (int) (WolgonPanel.DEFAULT_FONT_SIZE / 2);

		return switch (vert) {
			case Top -> zone.getY() + (4 * WolgonPanel.BUFFER);
			case Bottom -> (zone.getY() + zone.getHeight()) - (halfLineHeight + 4 * WolgonPanel.BUFFER);
			case VCenter -> zone.getY() + (zone.getHeight() / 2) - halfLineHeight;
			default -> 0;
		};
	}

	private int getAlignedHorizontalPosition() {
		return switch (horz) {
			case HCenter -> zone.getX() + (zone.getWidth() / 2) - (this.getWidth() / 2);
			case Left -> zone.getX() + WolgonPanel.BUFFER;
			case Right -> (zone.getX() + zone.getWidth()) - this.getWidth() - WolgonPanel.BUFFER;
			default -> 0;
		};
	}

	public boolean contains(Point p) {  	
		return (p.x >= getX() && p.x <= getX() + width) && (p.y >= trueTop && p.y <= trueTop + height);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void makeTemporary() {
		this.isTemporary = true;
	}
	
	public boolean isTemporary() {
		return this.isTemporary;
	}
	
	// to be called when the mouse moves over this label. Empty on this class, but will be overridden on subclass Button.
	public void hover() {
		if (this.clickValue != null) {
			setBaseColor(ViewConstants.HOVER);
		}
	}
	
	// to be called when the mouse moves off this label. Empty on this class, but will be overriden on subclass button.
	public void unhover() {
		setBaseColor(ViewConstants.DEFAULT);
	}

	public void setClickValue(String value) {
		this.clickValue = value;
	}

	public Character getClickValue() {
		return clickValue != null ? clickValue.charAt(0) : ' ';
	}

	public void instacomplete() {
		this.typedCharacters = getText().length();
	}

	public void setTypewriter(boolean value) {
		this.isTypewriter = value;
	}
}

