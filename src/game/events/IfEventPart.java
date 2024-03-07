package game.events;

import game.ControlOrb;
import game.Player;
import game.characters.CharacterManager;

public class IfEventPart implements EventPart {
  String subject;
  String query;
  boolean negated;
  EventPart ifYes;

  public IfEventPart(String rawLine, EventPart nested) {
    this.ifYes = nested;
    String[] ifParts = rawLine.split(" ");
    this.subject = ifParts[1].trim();
    if (ifParts[2].trim().equals("NOT")) {
      negated = true;
      this.query = ifParts[3].trim();
    } else {
      this.query = ifParts[2].trim();
    }
  }

  public void run(ControlOrb orb) {
    if (checkCondition()) {
      ifYes.run(orb);
    }
  }

  public boolean checkCondition() {
    boolean result = false;
    if (query.equals("INPARTY")) {
      result = Player.getPartyMembers().contains(CharacterManager.get(subject));
    }
    return negated ? !result : result;
  }

}
