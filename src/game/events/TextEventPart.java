package game.events;

import game.ControlOrb;

public record TextEventPart(String text) implements EventPart {

  @Override
  public void run(ControlOrb orb) {
    orb.clear();
    orb.print(text);
  }

  /**
   * Exists to allow [SKILL passed] tags to be added to text messages that depend on them.
   */
  public void appendRun(String toAppend, ControlOrb orb) {
    orb.clear();
    orb.print(toAppend + text);
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
