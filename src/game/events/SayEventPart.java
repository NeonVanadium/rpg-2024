package game.events;

import game.ControlOrb;
import game.characters.CharacterManager;

public class SayEventPart implements EventPart {
  String speaker, line;

  public SayEventPart(String rawLine) {
    String[] parts = rawLine.split(" ", 3);
    this.speaker = parts[1].trim();
    this.line = parts[2].trim();
  }

  private String format() {
    return CharacterManager.getKnownName(speaker) + ": \"" + line + "\"";
  }

  @Override
  public void run(ControlOrb orb) {
    orb.clear();
    orb.print(format());
  }
}
