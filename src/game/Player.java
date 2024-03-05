package game;

import game.characters.CharacterManager;
import game.characters.GameCharacter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
  public static GameCharacter character;
  private static Set<GameCharacter> party = new HashSet<>();

  public static void addToParty(String characterLabel, ControlOrb orb) {
    GameCharacter c = CharacterManager.get(characterLabel);
    party.add(c);
    orb.print(CharacterManager.getKnownName(c.getLabel()) + " has joined the party.");
  }

  public static void removeFromParty(String characterLabel, ControlOrb orb) {
    GameCharacter c = CharacterManager.get(characterLabel);
    party.remove(c);
    orb.print(CharacterManager.getKnownName(c.getLabel()) + " has left the party.");
  }

  public static List<GameCharacter> getPartyMembers() {
    return party.stream().toList();
  }

}
