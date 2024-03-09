package game;

import game.structure.Structure;

/**
 * Parent of GameCharacters and (later) Items. GameObjects that can be inside structures.
 */
public abstract class Movable extends GameObject {
  public Structure currentStructure;
  public int currentRoom;

  public boolean inStructure(String label) {
    return currentStructure != null && currentStructure.getLabel().equals(label);
  }

  @Override
  public boolean isInSameSpotAs(GameObject other) {
    if (other instanceof Movable && currentStructure != null) {
      return ((Movable) other).currentRoom == currentRoom &&
          ((Movable) other).currentStructure == currentStructure;
    }
    return super.isInSameSpotAs(other);
  }
}
