package game.characters;

import game.GameMaster;
import game.Player;
import game.Util;
import java.util.HashMap;
import java.util.Map;

public class CharacterManager {
  private static Map<String, Movable> characters = new HashMap();
  private static Map<String, String> knownNames = new HashMap<>(); // the names the player knows for any given character
  public static final String UNKNOWN_NAME = "???";

  public static void loadCharacters() {
    characters.put("PLAYER", new Movable("PLAYER", Gender.SOMETHING_ELSE));
    Player.character = characters.get("PLAYER");
    Util.parseFileAndDoEachLine(GameMaster.RESOURCE_FOLDER + "characters.txt",
        CharacterManager::makeCharacterFromLine);
  }

  private static void makeCharacterFromLine(String line) {
    String[] lineParts = line.split(" "); // note that 0th will be Util.ENTRY_START_SYMBOL
    String label = lineParts[1].trim();
    Gender gender = Gender.getFromString(lineParts[2].trim());
    characters.put(label, new Movable(label, gender));
    knownNames.put(label, UNKNOWN_NAME);
  }

  public static Movable get(String label) {
    return characters.get(label);
  }

  public static String getKnownName(String label) { return knownNames.get(label); }

  public static void setKnownName(String label, String newName) { knownNames.put(label, newName); }

  public static Movable player() {
    return characters.get("PLAYER");
  }

}
