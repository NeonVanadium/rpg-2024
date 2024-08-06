package game.events;

import game.ControlOrb;

/**
 * Stores a tag in a set, which work as boolean conditions. Will let events hinge on more
 * specific booleans than whether
 */
public class LogEventPart implements EventPart{
  String tag;

  public LogEventPart(String raw) {
    tag = raw.substring(("LOG").length()).trim();
  }

  public void run(ControlOrb orb) {
    EventManager.logTag(tag);
  }

  @Override
  public boolean pauseAfter() {
    return false;
  }

  @Override
  public boolean pauseBefore() {
    return false;
  }
}
