package game.events;

import game.ControlOrb;
import game.characters.CharacterManager;

public class SetNameEventPart implements EventPart{
  String characterLabel, newName;

  public SetNameEventPart(String rawLine) {
    String[] parts = rawLine.split(" ");
    this.characterLabel = parts[1].trim();
    this.newName = parts[2].trim();
  }

  @Override
  public void run(ControlOrb orb) {
    CharacterManager.setKnownName(characterLabel, newName);
  }
}
