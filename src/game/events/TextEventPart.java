package game.events;

import game.ControlOrb;

public record TextEventPart(String text) implements EventPart {

  @Override
  public void run(ControlOrb orb) {
    orb.clear();
    orb.print(text);
  }

  @Override
  public boolean pauseAfter() {
    return true;
  }

  @Override
  public boolean pauseBefore() {
    return true;
  }
}
