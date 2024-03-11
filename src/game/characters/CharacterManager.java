package game.characters;

import game.GameMaster;
import game.Player;
import game.Util;
import java.util.HashMap;
import java.util.Map;

public class CharacterManager {
  private final static Map<String, GameCharacter> characters = new HashMap<>();
  private final static Map<String, Creep> creeps = new HashMap<>();
  private final static Map<String, String> knownNames = new HashMap<>(); // the names the player knows for any given character
  public static final String UNKNOWN_NAME = "???";
  public static final int SKILL_CHECK_DIE = 10; // when a skill check is made, rolls a this-many-sided die.

  public static void loadCharacters() {
    characters.put("PLAYER", new GameCharacter("PLAYER", Gender.SOMETHING_ELSE));
    knownNames.put("PLAYER", "The player"); // temp until they enter it, obviously
    Player.character = characters.get("PLAYER");
    Util.parseFileAndDoEachLine(GameMaster.RESOURCE_FOLDER + "characters.txt",
        CharacterManager::makeCharacterFromLine);
  }

  private static void makeCharacterFromLine(String line) {
    String[] lineParts = line.split(" "); // note that 0th will be >> or >
    String label = lineParts[1].trim();
    if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
      Gender gender = Gender.getFromString(lineParts[2].trim());
      characters.put(label, new GameCharacter(label, gender));
      knownNames.put(label, UNKNOWN_NAME);
    } else if (line.startsWith(Util.SPECIAL_PART_SYMBOL)) {
      Creep c = new Creep(label);
      creeps.put(c.getLabel(), c);
      System.out.println("Made creep " + c);
    }
  }

  public static GameCharacter get(String label) {
    if (characters.containsKey(label)) return characters.get(label);
    else if (creeps.containsKey(label)) return creeps.get(label).clone();
    else throw new IllegalArgumentException("No character or creep exists with label: " + label + ".");
  }

  public static boolean contains(String label) {
    return characters.containsKey(label) || creeps.containsKey(label);
  }

  public static String getKnownName(String label) {
    if (creeps.containsKey(label)) {
      return creeps.get(label).name;
    }
    return knownNames.get(label);
  }

  public static void setKnownName(String label, String newName) { knownNames.put(label, newName); }

  public static GameCharacter player() {
    return characters.get("PLAYER");
  }

}
