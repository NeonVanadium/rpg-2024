package game.events;

import game.ControlOrb;

public class IfEventPart implements EventPart {
  EventCondition condition;
  EventPart ifYes;
  private boolean result, conditionChecked;

  public IfEventPart(String rawLine, EventPart nested) {
    condition = new EventCondition(rawLine.substring(rawLine.indexOf(' ') + 1)); // cut out the IF.
    this.ifYes = nested;
  }

  public void run(ControlOrb orb) {
    conditionChecked = false;
    checkCondition();

    if (result) {

      if (condition.query.equals("CHECK") && ifYes instanceof TextEventPart textPart) {
          textPart.appendRun("[" + condition.subject + " passed] ", orb);
      } else {
        ifYes.run(orb);
      }
    }
  }

  @Override
  public boolean pauseAfter() {
    checkCondition();
    return result ? ifYes.pauseAfter() : false;
  }

  @Override
  public boolean pauseBefore() {
    checkCondition();
    return true;
  }

  private void checkCondition() {
    result = condition.isMet();
  }
}
