package game.structure;

import game.Prompts.Selectable;

public class Room implements Selectable {
  private final String name, desc;

  public Room(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  public String getName() { return name; }
  public String getDescription() { return desc; }
}
