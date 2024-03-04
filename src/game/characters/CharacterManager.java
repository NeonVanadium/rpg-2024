package game.characters;

import game.Util;
import java.util.HashMap;
import java.util.Map;

public class CharacterManager {
  private static Map<String, GameCharacter> characters = new HashMap();

  public static void loadCharacters() {
    Util.parseFileAndDoEachLine("resources\\characters.txt",
        CharacterManager::makeCharacterFromLine);
  }

  private static void makeCharacterFromLine(String line) {
    String[] lineParts = line.split(" "); // note that 0th will be Util.ENTRY_START_SYMBOL
    String label = lineParts[1].trim();
    Gender gender = Gender.getFromString(lineParts[2].trim());
    characters.put(label, new GameCharacter(label, gender));
  }

  public static GameCharacter get(String label) {
    return characters.get(label);
  }

}
