package game.events;

import game.ControlOrb;
import shared.Util;

public class TextInputEventPart implements EventPart {
    // TEXTINPUT SETNAME PLAYER ~
    String nestedDeclaration;
    private EventPart nestedPart = null;

    public TextInputEventPart(String rawLine) {
        nestedDeclaration = rawLine.split(" ", 2)[1]; // every word after TEXTINPUT
    }

    @Override
    public void run(ControlOrb orb) {
        String userInput = orb.getTextInput();
        String replaced = nestedDeclaration.replace(Util.TEXT_INSERT_SYMBOL, userInput);
        nestedPart = EventPart.makePartBasedOnLine(replaced);
        nestedPart.run(orb);
    }

    @Override
    public boolean pauseAfter() {
        return nestedPart != null ? nestedPart.pauseAfter() : true;
    }

    @Override
    public boolean pauseBefore() {
        return false;
    }
}
