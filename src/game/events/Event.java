package game.events;

import game.Util;
import java.util.LinkedList;
import java.util.List;

public class Event {
  public final String title;
  private LinkedList<EventPart> parts;
  private LinkedList<EventCondition> triggerConditions;

  public Event(String rawLine) {
    String noStartSymbol = rawLine.substring(rawLine.indexOf(' '));
    // Presence of the delineator implies the event has automatic trigger conditions.
    if (rawLine.contains(Util.COMPONENT_DELINEATOR)) {
      String[] titleAndConditions = noStartSymbol.split(Util.COMPONENT_DELINEATOR);
      this.title = titleAndConditions[0].trim();
      triggerConditions = new LinkedList<>();
      for (int i = 1; i < titleAndConditions.length; i++) {
        triggerConditions.add(new EventCondition(titleAndConditions[i].trim()));
      }
    } else {
      this.title = noStartSymbol.trim();
    }

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

  public boolean conditionsMet() {
    if (this.triggerConditions == null) return false;
    for (EventCondition c : triggerConditions) {
      if (!c.isMet()) return false;
    }
    return true;
  }

  public boolean hasConditions() {
    return this.triggerConditions != null && !this.triggerConditions.isEmpty();
  }

}
