package view.window;

import java.awt.Color;
import java.awt.Graphics;

// a label that animates the typing of its text contents whenever it acquires new text.
public class TypewriterLabel extends Label {

	String fullText;
	int typedCharacters;
	private static final int TYPE_SPEED = 1; // how many characters per tick are written?
	private static final int AUDIO_BLIP_FREQUENCY = 1; // a blip sounds every this many characters.
	private static final int SHORT_DELAY = 5;
	private static final int LONG_DELAY = 10;
	private static final String LONG_DELAY_CHARS = ".:"; // arrays don't have a contains but strings do!
	private static final String SHORT_DELAY_CHARS = ",-;";
	private static int untilTypeCharacter = 0;
	private static int untilBlip = AUDIO_BLIP_FREQUENCY;
	private AudioManager audioManager;

	public TypewriterLabel(String name, String text, Color color, float fontSize, AlignmentLocation horz,
			AlignmentLocation vert, String zoneName, WolgonPanel panel) {
		super(name, text, color, fontSize, horz, vert, zoneName, panel);
		//audioManager = new AudioManager();
		init(text);
	}

	public TypewriterLabel(String name, String text, Color color, float fontSize, String otherLabelName,
			WolgonPanel panel) {
		super(name, text, color, fontSize, otherLabelName, panel);
		//audioManager = new AudioManager();
		init(text);
	}

	private void init(String fullText) {
		this.fullText = fullText;
		typedCharacters = 0;
	}

	protected void draw(Graphics g) {
		if (typedCharacters >= fullText.length()) {
			super.setText(fullText);
			typedCharacters = fullText.length();
		}
		else {
			char curChar = fullText.charAt(typedCharacters);

			if (untilTypeCharacter > 0) untilTypeCharacter--;

			if (untilTypeCharacter == 0) {
				super.setText(fullText.substring(0, typedCharacters));
				typedCharacters += TYPE_SPEED;
				if (audioManager != null) {
					if (curChar != ' ' && untilBlip == 0) {
						audioManager.playBlip();
						untilBlip = AUDIO_BLIP_FREQUENCY;
					} else {
						if (untilBlip > 0) untilBlip--;
					}
				}
			}

			if (!Character.isAlphabetic(curChar)) {
				untilBlip = 0;
				if (untilTypeCharacter <= 0) {
					if (LONG_DELAY_CHARS.contains("" + curChar)) {
						untilTypeCharacter = LONG_DELAY;
					} else if (SHORT_DELAY_CHARS.contains("" + curChar)){
						untilTypeCharacter = SHORT_DELAY;
					}
				}
			}
		}
		super.draw(g);
	}

	public void setText(String s) {
		fullText = s;
		typedCharacters = 0;
		super.setText("");
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
