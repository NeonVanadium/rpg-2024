package game;

import game.characters.GameCharacter;
import game.structure.Structure;
import java.util.HashSet;
import java.util.Set;

public class Player {
  public GameCharacter character;
  public Set<GameCharacter> party = new HashSet<>();

  public void addToParty(GameCharacter c) {
    party.add(c);
  }

  public void removeFromParty(GameCharacter c) {
    party.remove(c);
  }

}
