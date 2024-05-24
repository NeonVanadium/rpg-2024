package view.window;

import java.awt.Color;
import java.awt.Graphics;

// a label that animates the typing of its text contents whenever it acquires new text.
public class TypewriterLabel extends Label {

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

	public TypewriterLabel(String name, Color color, float fontSize, AlignmentLocation horz,
			AlignmentLocation vert, String zoneName, WolgonPanel panel) {
		super(name, color, fontSize, horz, vert, zoneName, panel);
		audioManager = new AudioManager();
		typedCharacters = 0;
	}

	public TypewriterLabel(String name, Color color, float fontSize, String otherLabelName,
			WolgonPanel panel) {
		super(name, color, fontSize, otherLabelName, panel);
		audioManager = new AudioManager();
		typedCharacters = 0;
	}

	@Override
	protected void draw(Graphics g) {
		super.setTextFontAndColor(g);
		if (this.hasTextChanged()) {
			this.wrapText(g);
		}
		if (!this.doneTyping()) {
			nextCharacterLogic();
		}
		textLineDrawer(g);
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
				g.drawString(lines[i].substring(0, typedCharacters - lengthOfLinesSoFar),
						this.getX(), (int) (this.getY() + (i * this.fontSize)));
				break;
			}
		}
	}

	private void nextCharacterLogic() {
		typedCharacters += TYPE_SPEED;
		/*char curChar = getText().charAt(typedCharacters);

		if (delayUntilNextChar > 0) {
			delayUntilNextChar--;
		} else if (delayUntilNextChar == 0) {
			typedCharacters += TYPE_SPEED;
			if (typedCharacters > getText().length()) {
				typedCharacters = getText().length();
			}
			maybePlayBlip(curChar);
		}

		if (!Character.isAlphabetic(curChar)) {
			untilBlip = 0;
			if (delayUntilNextChar <= 0) {
				if (LONG_DELAY_CHARS.contains("" + curChar)) {
					delayUntilNextChar = LONG_DELAY;
				} else if (SHORT_DELAY_CHARS.contains("" + curChar)) {
					delayUntilNextChar = SHORT_DELAY;
				}
			}
		}*/
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
		typedCharacters = 0;
		super.setText(s);
	}

	public boolean doneTyping() {
		return typedCharacters == getText().length();
	}

	public void instacomplete() {
		typedCharacters = getText().length() - 1;
		audioManager.signalDoneTyping();
	}

}
