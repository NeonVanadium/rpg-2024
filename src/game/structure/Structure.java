package game.structure;

import game.GameObject;
import game.Prompts.PromptOption;
import game.Prompts.SelectableInt;
import java.util.LinkedList;
import java.util.List;

public class Structure extends GameObject {

  private Room[] rooms = new Room[]{
      new Room("Tower", "The base of a stone tower overgrown with green vines.", new int[]{-1, 1}),
      new Room("Vine-touched Platform", "A circular, stone platform high atop a strange, vine-covered tower.", new int[]{0})
  };
  String distantName; // the name shown to the player when viewed from a distance.

  public Structure(String name) {
    this.name = name;
    this.distantName = name;
    // add rooms param
  }

  public Structure(String name, String distantName) {
    this.name = name;
    this.distantName = distantName;
    // and rooms param

  }

  public String getDistantName() {
    return distantName;
  }

  public String getName() {
    return name;
  }

  public boolean isEnterable() {
    return this.rooms != null && this.rooms.length > 0;
  }

  public String getRoomName(int id) {
    if (isValidRoomId(id)) {
      return rooms[id].getName();
    }
    return "INVALID ROOM";
  }

  public String getRoomDescription(int id) {
    if (isValidRoomId(id)) {
      return rooms[id].getDescription();
    }
    return "INVALID ROOM";
  }

  public List<PromptOption> getRoomExits(int id) {
    if (isValidRoomId(id)) {
      int[] exits = rooms[id].getExits();
      if (exits == null) {
        return null;
      }
      LinkedList<PromptOption> exitPOs = new LinkedList<>();
      for (int i = 0; i < exits.length; i++) {
        if (exits[i] == -1) {
          exitPOs.add(new PromptOption("The open world", new SelectableInt(-1)));
        } else {
          exitPOs.add(new PromptOption(rooms[exits[i]].getName(), new SelectableInt(exits[i])));
        }
      }
      return exitPOs;
    }
    return null;
  }

  private boolean isValidRoomId(int id) {
    return rooms != null && id >= 0 && id < rooms.length;
  }

}
