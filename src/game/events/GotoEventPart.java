package game.events;

public class GotoEventPart implements EventPart {
  public final String nextEvent;

  public GotoEventPart(String raw) {
    nextEvent = raw.substring(("GOTO").length()).trim();
  }

}
