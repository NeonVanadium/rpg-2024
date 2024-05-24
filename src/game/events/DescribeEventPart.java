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
    orb.print(CharacterManager.get(characterLabel).getDetailedDescription());
  }

  @Override
  public boolean pauseAfter() {
    return true;
  }

  @Override
  public boolean pauseBefore() {
    return false;
  }
}
