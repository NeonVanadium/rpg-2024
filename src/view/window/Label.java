package view.window;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

class Label implements IRectangle {

	protected String text;
	protected String wrappedText;
	protected Color color; //text color
	protected float fontSize;

	private Label parent; // may be null, the label relative to which this is positioned
	private AlignmentLocation vert;
	private AlignmentLocation horz;
	private int width; //how wide the label is (set by wrapText())
	private int height; //how tall the label is (set by wrapText())
	private PanelZone zone;
	
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
	private Label(String name, String text, Color color, float fontSize, WolgonPanel panel) {
		this.text = text;
		this.color = color;
		this.fontSize = fontSize;
		panel.addLabel(name, this);
	}
	
	// Positions label relative to one of the AlignmentLocations (see enum in Label) of its zone
	public Label(String name, String text, Color color, float fontSize,
			AlignmentLocation horz, AlignmentLocation vert, String zoneName, WolgonPanel panel) {
		
		this(name, text, color, fontSize, panel);
		this.zone = panel.getZone(zoneName);
		this.horz = horz;
		this.vert = vert;
	}

	// Positions label below an existing label.
	public Label(String name, String text, Color color, float fontSize, String otherLabelName, WolgonPanel panel) {
		this(name, text, color, fontSize, panel);
		this.parent = panel.getLabel(otherLabelName);
		this.zone = parent.zone;
	}
	
	protected void draw(Graphics g) {
		setTextFontAndColor(g);
		if(this.textChanged) {
			this.wrapText(g);
			String[] lines = this.wrappedText.split("\n");
			for(int i = 0; i < lines.length; i++) {
				g.drawString(lines[i], this.getX(), (int) (this.getY() + (i * this.fontSize)));
			}
		}
	}

	protected void setTextFontAndColor(Graphics g) {
		g.setFont(g.getFont().deriveFont(this.fontSize));
		g.setColor(this.color);
	}

	// given the text and its container, inserts \n at various locations so that
	// the text will all appear in its box. Fills relevant fields on the instance.
	public void wrapText(Graphics g) { 
		wrappedText = "";
		width = 0;
		height = 0;
		trueTop = -1;

		FontMetrics metrics = g.getFontMetrics();
		int availableSpace = zone.getWidth() - (2 * WolgonPanel.BUFFER); //available horizontal space in the zone, in pixels
		int lastNewLineIndex = 0;

		String curLine = "";
		String lineAtNextSpace = "";

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
			if(text.charAt(curSpace) == '\n' || lineTooWide){
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

	public void setTextColor(Color c) {
		this.color = c;
	}

	public void setText(String s) {
		text = s;
		textChanged = true;
	}

	public String getText() {
		return text;
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

	protected boolean hasTextChanged() {
		return this.textChanged;
	}
	
	//
	// EMPTY FUNCTIONS FOR SUBCLASS FUNCTIONALITY
	//
	
	// to be called when the mouse moves over this label. Empty on this class, but will be overridden on subclass Button.
	public void hover() {
		// nothing
	}
	
	// to be called when the mouse moves off this label. Empty on this class, but will be overriden on subclass button.
	public void unhover() {
		// nothing
	}
	
	// empty on this class, used on button subclass to call its stored function to be run
	public void runFunction() { 
		// nothing
	}

}

