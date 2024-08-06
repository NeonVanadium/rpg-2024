package game.events;

import game.ControlOrb;

/**
 * An abstraction event part. Inserts the event parts of the specified event part into the running event.
 * Should be used for small sub-components that are shared between event branches, etc.
 */
public class InsertEventPart implements EventPart {
  String eventToInsert; // name of the event whose parts should be added to the event this part belongs to.

  public InsertEventPart(String raw) {
    eventToInsert = raw.substring(("INSERT").length()).trim();
  }

  public void run(ControlOrb orb) {
    EventManager.pushToEventInsertStack(eventToInsert);
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
