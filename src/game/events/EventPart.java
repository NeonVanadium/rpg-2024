package game.events;

import game.ControlOrb;
import game.Util;
import game.prompts.Selectable;

public interface EventPart extends Selectable {
  static EventPart makePartBasedOnLine(String rawPartString) {
    if (rawPartString.startsWith("CHOICE")) {
      return new ChoiceEventPart(rawPartString);
    } else if (rawPartString.startsWith("GOTO")) {
      return new GotoEventPart(rawPartString);
    } else if (rawPartString.startsWith("JOINPARTY")) {
      return new JoinPartyEventPart(rawPartString);
    } else if (rawPartString.startsWith("LEAVEPARTY")) {
      return new LeavePartyEventPart(rawPartString);
    } else if (rawPartString.startsWith("SETNAME")) {
      return new SetNameEventPart(rawPartString);
    } else if (rawPartString.startsWith("SAY")) {
      return new SayEventPart(rawPartString);
    } else if (rawPartString.startsWith("YELL")) {
      return new YellEventPart(rawPartString);
    } else if (rawPartString.startsWith("IF")) {
      String[] conditionAndBody = rawPartString.split(Util.COMPONENT_DELINEATOR, 2);
      EventPart ifYes = makePartBasedOnLine(conditionAndBody[1].trim());
      return new IfEventPart(conditionAndBody[0], ifYes);
    } else if (rawPartString.startsWith("FIGHT")) {
      return new FightEventPart(rawPartString);
    } else if (rawPartString.startsWith("DESCRIBE")) {
      return new DescribeEventPart(rawPartString);
    } else if (rawPartString.startsWith("MOVE")) {
      return new MoveEventPart(rawPartString);
    } else if (rawPartString.startsWith("INSERT")) {
      return new InsertEventPart(rawPartString);
    } else if (rawPartString.startsWith("LOG")) {
      return new LogEventPart(rawPartString);
    } else if (rawPartString.startsWith("SETATTRIBUTE")) {
      return new AddAttributeEventPart(rawPartString);
    }
    else {
      return new TextEventPart(rawPartString);
    }
  }

  void run(ControlOrb orb);

  boolean pauseAfter();

  boolean pauseBefore();
}
