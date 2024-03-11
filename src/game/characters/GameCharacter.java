package game.characters;

public class GameCharacter extends game.Movable {

  public static int DEFAULT_MOVE_SPEED = 10;

  public Gender gender;

  public GameCharacter(String name, Gender gender) {
    this.label = name; this.gender = gender;
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
    result.append(getGenericDescription());
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

  public int roll(String skill) {
    int roll = (int) (Math.random() * CharacterManager.SKILL_CHECK_DIE) + 1;
    System.out.println(String.format("%s rolled %d on a %s check", this.label, roll, skill));
    return roll;
  }

}


