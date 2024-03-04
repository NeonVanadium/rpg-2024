package game.events;

import game.Util;

public class GotoEventPart implements EventPart {
  public final String nextEvent;

  public GotoEventPart(String raw) {
    nextEvent = raw.substring((Util.SPECIAL_PART_SYMBOL + "GOTO").length()).trim();
  }

}
