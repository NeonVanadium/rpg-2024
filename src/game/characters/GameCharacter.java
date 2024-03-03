package game.characters;

import game.GameObject;
import game.Movable;

public class GameCharacter extends Movable {

  public static int DEFAULT_MOVE_SPEED = 10;

  public Gender gender;

  public GameCharacter(String name, Gender gender) {
    this.name = name; this.gender = gender;
  }

  public String getGenericDescription() {
    if (gender == Gender.SOMETHING_ELSE) {
      return "someone";
    }
    return "a " + gender.name().toLowerCase();
  }

}


