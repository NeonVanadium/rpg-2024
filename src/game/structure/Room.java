package game.structure;

import game.prompts.Selectable;

public class Room implements Selectable {
  private final String name, desc;
  private int[] exits;
  private String[] exitTags;

  public Room(String name, String desc, int[] exits) {
    this.name = name;
    this.desc = desc;
    this.exits = exits;
  }

  public Room(String name, String desc) {
    this.name = name;
    this.desc = desc;
    this.exits = null;
  }

  public String getName() { return name; }
  public String getDescription() { return desc; }
  public int[] getExits() { return exits; }
}
