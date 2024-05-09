package view.window;

import java.awt.Color;
import java.awt.Graphics;

// a label that animates the typing of its text contents whenever it acquires new text.
public class TypewriterLabel extends Label {

	String fullText;
	int typedCharacters;
	private static final int TYPE_SPEED = 1; // how many characters per tick are written?
	private static final int AUDIO_BLIP_FREQUENCY = 2; // a blip sounds every this many characters.
	private static final int SHORT_DELAY = 5;
	private static final int LONG_DELAY = 10;
	private static final String LONG_DELAY_CHARS = ".:"; // arrays don't have a contains but strings do!
	private static final String SHORT_DELAY_CHARS = ",-;";
	private static int delayUntilNextChar = 0;
	private static int untilBlip = AUDIO_BLIP_FREQUENCY;
	private static boolean PLAY_AUDIO_BLIPS = false;
	private AudioManager audioManager;

	public TypewriterLabel(String name, String text, Color color, float fontSize, AlignmentLocation horz,
			AlignmentLocation vert, String zoneName, WolgonPanel panel) {
		super(name, text, color, fontSize, horz, vert, zoneName, panel);
		audioManager = new AudioManager();
		init(text);
	}

	public TypewriterLabel(String name, String text, Color color, float fontSize, String otherLabelName,
			WolgonPanel panel) {
		super(name, text, color, fontSize, otherLabelName, panel);
		audioManager = new AudioManager();
		init(text);
	}

	private void init(String fullText) {
		this.fullText = fullText;
		typedCharacters = 0;
	}

	@Override
	protected void draw(Graphics g) {
		super.setTextFontAndColor(g);
		if (this.hasTextChanged()) {
			this.wrapText(g);
			if (typedCharacters < fullText.length())
				textLineDrawer(g);
		}
	}

	private void textLineDrawer(Graphics g) {
		int lengthOfLinesSoFar = 0;
		String[] lines = this.wrappedText.split("\n");

		for (int i = 0; i < lines.length; i++) {
			if (lengthOfLinesSoFar + lines[i].length() <= typedCharacters) {
				// We've passed this line. Draw it completely.
				g.drawString(lines[i], this.getX(), (int) (this.getY() + (i * this.fontSize)));
				lengthOfLinesSoFar += lines[i].length();
			} else if (!lines[i].isBlank()){
				// We're currently typing this line. Draw it partially.
				nextCharacterLogic();
				g.drawString(lines[i].substring(0, typedCharacters - lengthOfLinesSoFar),
						this.getX(), (int) (this.getY() + (i * this.fontSize)));
				break;
			}
		}


	}

	private void nextCharacterLogic() {
		if (delayUntilNextChar > 0) {
			delayUntilNextChar--;
		}

		char curChar = fullText.charAt(typedCharacters);

		if (delayUntilNextChar == 0) {
			typedCharacters += TYPE_SPEED;
			maybePlayBlip(curChar);
		}

		if (!Character.isAlphabetic(curChar) && curChar != ' ') {
			untilBlip = 0;
			if (delayUntilNextChar <= 0) {
				if (LONG_DELAY_CHARS.contains("" + curChar)) {
					delayUntilNextChar = LONG_DELAY;
				} else if (SHORT_DELAY_CHARS.contains("" + curChar)) {
					delayUntilNextChar = SHORT_DELAY;
				}
			}
		}
	}

	/**
	 * Helper for draw.
	 */
	private void maybePlayBlip(char curChar) {
		if (PLAY_AUDIO_BLIPS && audioManager != null) {
			if (curChar != ' ' && untilBlip == 0) {
				audioManager.playBlip();
				untilBlip = AUDIO_BLIP_FREQUENCY;
			} else {
				if (untilBlip > 0) untilBlip--;
			}
		}
	}

	public void setText(String s) {
		fullText = s;
		typedCharacters = 0;
		super.setText(fullText);
	}

	public String getFullText() {
		return fullText;
	}

	public boolean doneTyping() {
		return getFullText().length() == getText().length();
	}

	public void instacomplete() {
		typedCharacters = fullText.length();
		super.setText(fullText);
		audioManager.signalDoneTyping();
	}

}
