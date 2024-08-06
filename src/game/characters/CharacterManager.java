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
  private final static Map<String, String> STATS = new HashMap<>();
  private final static Map<String, Skill> SKILLS = new HashMap<>();

  public static final String UNKNOWN_NAME = "???";
  public static final int SKILL_CHECK_DIE = 10; // when a skill check is made, rolls a this-many-sided die.

  public static void loadCharacters() {
    Util.parseFileAndDoEachLine(GameMaster.getResourceFolder() + "stats_and_skills.txt",
        CharacterManager::makeStatsOrSkill);

    characters.put("PLAYER", new GameCharacter("PLAYER", Gender.SOMETHING_ELSE, makeCharacterStatTemplate(), "you."));
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
    String[] lineParts = line.split(" ", 4); // note that 0th will be >> or >
    String label = lineParts[1].trim();
    if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
      Gender gender = Gender.getFromString(lineParts[2].trim());
      knownNames.put(label, UNKNOWN_NAME);
      String desc = line.contains("\"") ? line.substring(line.indexOf("\"") + 1, line.length() - 1) // violently temporary. Gets a description string from the character page. Later, descriptions should all be generated.
          : null;
      characters.put(label, new GameCharacter(label, gender, makeCharacterStatTemplate(), desc));
    } else if (line.startsWith(Util.SPECIAL_PART_SYMBOL)) {
      Creep c = new Creep(label, makeCharacterStatTemplate());
      creeps.put(c.getLabel(), c);
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
