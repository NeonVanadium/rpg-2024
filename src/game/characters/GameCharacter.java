package game.characters;

import java.util.Map;

public class GameCharacter extends game.Movable {

  public static int DEFAULT_MOVE_SPEED = 10;

  public final Map<String, Integer> stats;
  public Gender gender;

  private String desciptionOverride;

  public GameCharacter(String name, Gender gender, Map<String, Integer> stats, String describeOverride) {
    this.label = name; this.gender = gender; this.stats = stats; this.desciptionOverride = describeOverride;
  }

  public String getGenericDescription() {
    if (gender == Gender.SOMETHING_ELSE) {
      return "someone";
    }
    return "a " + gender.name().toLowerCase();
  }

  public String getDetailedDescription() {
    StringBuilder result = new StringBuilder();
    String start = gender == Gender.SOMETHING_ELSE ? "They are " : gender == Gender.WOMAN ? "She is " : "He is ";
    result.append(start);
    if (desciptionOverride != null) {
      result.append(desciptionOverride);
    } else {
      result.append(getGenericDescription());
    }
    result.append('.');
    return result.toString();
  }

  @Override
  public String getNameToDisplayAsOption() {
    if (CharacterManager.getKnownName(this.label).equals(CharacterManager.UNKNOWN_NAME)) {
      return getGenericDescription();
    } else {
      return CharacterManager.getKnownName(this.label);
    }
  }

  public int getModifier(String statLabel) {
    return stats.get(statLabel);
  }

}


