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
    parts.add(makePartBasedOnLine(rawPartString));
  }

  private EventPart makePartBasedOnLine(String rawPartString) {
    if (rawPartString.startsWith("CHOICE")) {
      return new ChoiceEventPart(rawPartString);
    }
    else if (rawPartString.startsWith("GOTO")) {
      return new GotoEventPart(rawPartString);
    }
    else if (rawPartString.startsWith("JOINPARTY")) {
      return new JoinPartyEventPart(rawPartString);
    }
    else if (rawPartString.startsWith("LEAVEPARTY")) {
      return new LeavePartyEventPart(rawPartString);
    }
    else if (rawPartString.startsWith("SETNAME")) {
      return new SetNameEventPart(rawPartString);
    }
    else if (rawPartString.startsWith("SAY")) {
      return new SayEventPart(rawPartString);
    }
    else if (rawPartString.startsWith("IF")) {
      String[] conditionAndBody = rawPartString.split(Util.COMPONENT_DELINEATOR, 2);
      EventPart nested = makePartBasedOnLine(conditionAndBody[1].trim());
      return new IfEventPart(conditionAndBody[0], nested);
    }
    else {
      return new TextEventPart(rawPartString);
    }
  }

}
