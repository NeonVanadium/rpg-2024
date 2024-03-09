package game.events;

import game.ControlOrb;

public class IfEventPart implements EventPart {
  EventCondition condition;
  EventPart ifYes;

  public IfEventPart(String rawLine, EventPart nested) {
    condition = new EventCondition(rawLine.substring(rawLine.indexOf(' ') + 1)); // cut out the IF.
    this.ifYes = nested;
  }

  public void run(ControlOrb orb) {
    if (condition.isMet()) {
      ifYes.run(orb);
    }
  }
}
