package game.characters;

import game.GameMaster;
import game.Player;
import game.TagAndTopicManager;
import shared.Util;

import java.util.*;

public class CharacterManager {
  private final static Map<String, GameCharacter> characters = new HashMap<>();
  private final static Map<String, Creep> creeps = new HashMap<>();
  private final static Map<String, String> knownNames = new HashMap<>(); // the names the player knows for any given character
  private final static Map<String, String> STATS = new HashMap<>();
  private final static Map<String, Skill> SKILLS = new HashMap<>();

  private static String[] template; // derived from the characters file, the format (ie, what attribute is at which position) of a character declaration.

  public static final String UNKNOWN_NAME = "???";
  public static final int SKILL_CHECK_DIE = 10; // when a skill check is made, rolls a this-many-sided die.

  public static void loadCharacters() {
    Util.parseFileAndDoEachLine(GameMaster.getResourceFolder() + "stats_and_skills.txt",
        CharacterManager::makeStatsOrSkill);

    characters.put("PLAYER", new GameCharacter("PLAYER", new HashMap<>(), makeCharacterStatTemplate(), "you."));
    knownNames.put("PLAYER", "The player"); // temp until they enter it, obviously
    Player.character = characters.get("PLAYER");
    Util.parseFileAndDoEachLine(GameMaster.getResourceFolder() + "characters.txt",
        CharacterManager::makeCharacterFromLine);
  }



  private static void makeStatsOrSkill(String line) {
    if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
      String[] parts = line.substring(line.indexOf(' ')).split(Util.COMPONENT_DELINEATOR);
      STATS.put(parts[0].trim(), parts[1].trim());
    } else if (line.startsWith(Util.SPECIAL_PART_SYMBOL)) {
      Skill newSkill = new Skill(line);
      SKILLS.put(newSkill.name, newSkill);
    }
  }

  private static void makeCharacterFromLine(String line) {
    // the first line tells us what the format is for the rest of the lines.
    if (template == null) {
      if (!line.startsWith("NAME")) {
        throw new RuntimeException("Character template doesn't start with required attribute NAME");
      }
      template = line.split(" ");
    }
    else {
      String[] lineParts = line.split(" ", template.length + 2); // + 2 for the starting >> and the optional ending description override.
      String label = lineParts[1].trim(); // NAME / label is required to be first.

      if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
        makeCharacter(label, lineParts);
      } else if (line.startsWith(Util.SPECIAL_PART_SYMBOL)) {
        Creep c = new Creep(label, makeCharacterStatTemplate());
        creeps.put(c.getLabel(), c);
      }
    }
  }

  /**
   * Helper for makeCharacterFromLine for readability.
   */
  private static void makeCharacter(String label, String[] lineParts) {
    String description = null;
    Map<String, String> attributes = new HashMap<>();

    for (int i = 2; i < lineParts.length; i++) {
      int templateIndex = i - 1; // -1 to compensate for starting >>, which template does not have.
      if (templateIndex >= template.length) { // we've got more tokens than the template specified -- the rest is description override.
        description = lineParts[i];
      } else {
        String attributeCategory = template[templateIndex];
        attributes.put(attributeCategory, lineParts[i]);
      }
    }

    knownNames.put(label, UNKNOWN_NAME);
    characters.put(label, new GameCharacter(label, attributes, makeCharacterStatTemplate(), description));
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

  private static HashMap<String, Integer> makeCharacterStatTemplate() {
    HashMap<String, Integer> map = new HashMap();
    for (String s : STATS.keySet()) {
      map.put(s, 0);
    }
    return map;
  }

  /**
   * Roll a skill check with the provided Difficulty Class.
   * @return True if the character passed the check.
   */
  public static boolean skillCheck(String characterLabel, String skillOrStat, int DC) {
    int roll = Util.random(SKILL_CHECK_DIE) + 1;

    if (STATS.containsKey(skillOrStat)) {
      return get(characterLabel).getModifier(skillOrStat) >= DC;
    } else if (SKILLS.containsKey(skillOrStat)) {
      for (String statLabel : SKILLS.get(skillOrStat).relevantStats) {
        roll += get(characterLabel).getModifier(statLabel);
      }
    } else {
      throw new IllegalArgumentException(skillOrStat + " is not a known skill name!");
    }

    System.out.println(String.format("%s rolled %d on a DC%d %s check",
        characterLabel, roll, DC, skillOrStat));
    return roll >= DC;

  }

}
