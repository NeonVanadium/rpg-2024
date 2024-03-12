package game.events;

import game.Player;
import game.characters.CharacterManager;

/**
 * Used both on top-level events and in If parts.
 */
class EventCondition {
  String subject, object, query;
  boolean negated;

  public EventCondition(String rawLine) {
    String[] ifParts = rawLine.split(" ");
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
      case "IN" -> CharacterManager.get(subject).currentStructure.getLabel().equals(object);
      case "COMPLETED", "COMPLETE" -> EventManager.isEventCompleted(subject);
      case "INOPENWORLD" -> CharacterManager.get(subject).currentStructure == null;
      case "WITH" -> CharacterManager.get(subject).isInSameSpotAs(CharacterManager.get(object));
      case "CHECK" -> Player.character.roll(subject) > Integer.parseInt(object);
      default -> false;
    };
    return negated ? !result : result;
  }
}
