package game.characters;

import game.GameObject;

public class GameCharacter extends GameObject {

  public static int DEFAULT_MOVE_SPEED = 10;

  public Gender gender;

  public GameCharacter(String name, Gender gender) {
    this.name = name; this.gender = gender;
  }

  public String getGenericDescription() {
    return "a " + gender.name().toLowerCase();
  }

}


