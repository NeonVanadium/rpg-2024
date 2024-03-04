package game.structure;

import game.Util;
import game.prompts.Selectable;

public class Room implements Selectable {
  private final String name, desc;
  private int[] exits;
  private String[] exitTags;

  public Room(String name, String desc, String exitsAndDescriptions) {
    this.name = name;
    this.desc = desc;

    if (exitsAndDescriptions != null && !exitsAndDescriptions.isBlank()) {
      String[] ead = exitsAndDescriptions.split("/");

      exits = new int[ead.length];
      exitTags = new String[ead.length];
      String[] curExitAndDescription;

      for (int i = 0; i < ead.length; i++) {
        curExitAndDescription = ead[i].split(Util.SPECIAL_PART_SYMBOL);
        exits[i] = Integer.parseInt(curExitAndDescription[1].trim());
        exitTags[i] = curExitAndDescription[0].trim();
      }
    } else {
      exits = null;
      exitTags = null;
    }
  }

  public String getName() { return name; }
  public String getDescription() { return desc; }
  public int[] getExits() { return exits; }
  public String[] getExitTags() { return exitTags; }
}
