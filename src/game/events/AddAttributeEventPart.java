package game.events;

import game.ControlOrb;
import game.characters.CharacterManager;

public class AddAttributeEventPart implements EventPart{
    String characterLabel, attributeCategory, attributeLabel;

    public AddAttributeEventPart(String rawLine) {
        String[] parts = rawLine.split(" ");
        this.characterLabel = parts[1].trim();
        this.attributeCategory = parts[2].trim();
        if (parts.length > 3) {
            this.attributeLabel = parts[3].trim();
        }
    }

    @Override
    public void run(ControlOrb orb) {
        if (attributeLabel != null) {
            CharacterManager.get(characterLabel).addAttribute(attributeCategory, attributeLabel);
        } else {
            CharacterManager.get(characterLabel).addAttribute(attributeCategory);
        }
    }

    @Override
    public boolean pauseAfter() {
        return false;
    }

    @Override
    public boolean pauseBefore() {
        return false;
    }
}