package game.characters;

public class Movable extends game.Movable {

  public static int DEFAULT_MOVE_SPEED = 10;

  public Gender gender;

  public Movable(String name, Gender gender) {
    this.label = name; this.gender = gender;
  }

  public String getGenericDescription() {
    if (gender == Gender.SOMETHING_ELSE) {
      return "someone";
    }
    return "a " + gender.name().toLowerCase();
  }

  @Override
  public String getNameToDisplayAsOption() {
    if (CharacterManager.getKnownName(this.label).equals(CharacterManager.UNKNOWN_NAME)) {
      return getGenericDescription();
    } else {
      return CharacterManager.getKnownName(this.label);
    }
  }

}


