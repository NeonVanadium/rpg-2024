package game.events;

import game.ControlOrb;
import game.Player;
import game.TagAndTopicManager;
import game.characters.CharacterManager;

/**
 * Used both on top-level events and in If parts.
 */
class EventCondition {
  String subject, object, query;
  boolean negated;

  public EventCondition(String rawLine) {
    String[] ifParts = rawLine.split(" ");
    if (ifParts.length == 1) {
      query = ifParts[0];
      return;
    } else if (ifParts[0].equals("NOT")) {
      negated = true;
      query = ifParts[1];
      return;
    }
    this.subject = ifParts[0].trim();
    if (ifParts[1].trim().equals("NOT")) {
      negated = true;
      this.query = ifParts[2].trim();
      if (ifParts.length > 3) {
        object = ifParts[3].trim();
      }
    } else {
      this.query = ifParts[1].trim();
      if (ifParts.length > 2) {
        object = ifParts[2].trim();
      }
    }
  }

  boolean isMet() {
    boolean result = switch (query) {
      case "INPARTY" -> Player.getPartyMembers().contains(CharacterManager.get(subject));
      case "IN" -> CharacterManager.get(subject).currentStructure != null && CharacterManager.get(subject).currentStructure.getLabel().equals(object);
      case "COMPLETED", "COMPLETE" -> EventManager.isEventCompleted(subject);
      case "INOPENWORLD" -> CharacterManager.get(subject).currentStructure == null;
      case "WITH" -> CharacterManager.get(subject).isInSameSpotAs(CharacterManager.get(object));
      case "CHECK" -> CharacterManager.skillCheck("PLAYER", subject, Integer.parseInt(object));
      case "HASATTRIBUTE" -> CharacterManager.get(subject).hasAttribute(object);
      default -> TagAndTopicManager.hasLog(query); // if the condition is one token long, we simply check if this tag has been logged in the event manager.
    };
    return negated ? !result : result;
  }
}
