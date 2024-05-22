package game.events;

import game.ControlOrb;

public class GotoEventPart implements EventPart {
  public final String nextEvent;

  public GotoEventPart(String raw) {
    nextEvent = raw.substring(("GOTO").length()).trim();
  }

  public void run(ControlOrb orb) {
    EventManager.queueEventWithTitle(nextEvent);
  }

  @Override
  public boolean pauseAfter() {
    return false;
  }

  @Override
  public boolean pauseBefore() {
    return true;
  }

}
