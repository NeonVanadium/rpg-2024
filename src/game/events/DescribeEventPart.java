package game.events;

import game.ControlOrb;
import game.characters.CharacterManager;

public class DescribeEventPart implements EventPart {

  String characterLabel;

  public DescribeEventPart(String rawLine) {
    characterLabel = rawLine.split(" ")[1].trim();
  }

  @Override
  public void run(ControlOrb orb) {
    new TextEventPart(CharacterManager.get(characterLabel).getDetailedDescription()).run(orb);
  }
}
