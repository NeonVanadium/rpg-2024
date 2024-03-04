package game.events;

import game.Util;
import java.util.LinkedList;
import java.util.List;

public class Event {
  public final String title;
  private LinkedList<EventPart> parts;

  public Event(String title) {
    this.title = title.trim();
    parts = new LinkedList<>();
  }

  public List<EventPart> getEventParts() { return parts; }

  public void addPart(String rawPartString) {
    EventPart newEventPart;

    if (rawPartString.startsWith(Util.SPECIAL_PART_SYMBOL + "CHOICE")) {
      newEventPart = new ChoiceEventPart(rawPartString);
    } else if (rawPartString.startsWith(Util.SPECIAL_PART_SYMBOL + "GOTO")) {
      newEventPart = new GotoEventPart(rawPartString);
    } else {
      newEventPart = new TextEventPart(rawPartString);
    }

    parts.add(newEventPart);
  }

}
