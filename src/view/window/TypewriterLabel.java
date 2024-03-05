package view.window;

import java.awt.Color;
import java.awt.Graphics;

// a label that animates the typing of its text contents whenever it acquires new text.
public class TypewriterLabel extends Label {

	String fullText;
	int typedCharacters;
	private static final int TYPE_SPEED = 1; // how many characters per tick are written?

	public TypewriterLabel(String name, String text, Color color, float fontSize, AlignmentLocation horz,
			AlignmentLocation vert, String zoneName, WolgonPanel panel) {
		super(name, text, color, fontSize, horz, vert, zoneName, panel);
		init(text);
	}

	public TypewriterLabel(String name, String text, Color color, float fontSize, String otherLabelName,
			WolgonPanel panel) {
		super(name, text, color, fontSize, otherLabelName, panel);
		init(text);
	}

	private void init(String fullText) {
		this.fullText = fullText;
		typedCharacters = 0;
	}

	protected void draw(Graphics g) {
		if (typedCharacters <= fullText.length()) {
			super.setText(fullText.substring(0, typedCharacters));
			typedCharacters += TYPE_SPEED;
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
	}

}
