package game.events;

import game.characters.CharacterManager;

public class SayEventPart implements EventPart {
  String speaker, line;

  public SayEventPart(String rawLine) {
    String[] parts = rawLine.split(" ", 3);
    this.speaker = parts[1].trim();
    this.line = parts[2].trim();
  }

  public String format() {
    return CharacterManager.getKnownName(speaker) + ": \"" + line + "\"";
  }
}
