package game;

import game.characters.CharacterManager;
import game.characters.GameCharacter;
import game.structure.Structure;
import java.util.HashSet;
import java.util.Set;

public class Player {
  public static GameCharacter character;
  public static Set<GameCharacter> party = new HashSet<>();

  public static void addToParty(String characterLabel, ControlOrb orb) {
    GameCharacter c = CharacterManager.get(characterLabel);
    party.add(c);
    orb.print(c.getName() + " has joined the party.");
  }

  public static void removeFromParty(String characterLabel, ControlOrb orb) {
    GameCharacter c = CharacterManager.get(characterLabel);
    party.remove(c);
    orb.print(c.getName() + " has left the party.");
  }

}
