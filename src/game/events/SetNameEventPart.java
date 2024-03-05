package game.events;

import game.characters.CharacterManager;

public class SetNameEventPart implements EventPart{
  String characterLabel, newName;

  public SetNameEventPart(String rawLine) {
    String[] parts = rawLine.split(" ");
    this.characterLabel = parts[1].trim();
    this.newName = parts[2].trim();
  }
}
