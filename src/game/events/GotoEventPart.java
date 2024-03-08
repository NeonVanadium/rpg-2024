package game.events;

import game.ControlOrb;

public class GotoEventPart implements EventPart {
  public final String nextEvent;

  public GotoEventPart(String raw) {
    nextEvent = EventManager.getEventPrefix() + raw.substring(("GOTO").length()).trim();
  }

  public void run(ControlOrb orb) {
    EventManager.queueEventWithTitle(nextEvent);
  }

}
