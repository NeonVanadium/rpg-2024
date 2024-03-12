package game.events;

import game.ControlOrb;

public class IfEventPart implements EventPart {
  EventCondition condition;
  EventPart ifYes;
  private boolean pauseAfter = false;

  public IfEventPart(String rawLine, EventPart nested) {
    condition = new EventCondition(rawLine.substring(rawLine.indexOf(' ') + 1)); // cut out the IF.
    this.ifYes = nested;
  }

  public void run(ControlOrb orb) {
    if (condition.isMet()) {
      ifYes.run(orb);
      pauseAfter = ifYes.pauseAfter();
    }
  }

  @Override
  public boolean pauseAfter() {
    return pauseAfter;
  }

  @Override
  public boolean pauseBefore() {
    return false;
  }
}
