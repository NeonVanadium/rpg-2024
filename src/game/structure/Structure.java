package game.structure;

import game.GameObject;
import game.Movable;
import shared.Util;
import game.prompts.PromptOption;
import game.prompts.SelectableInt;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Structure extends GameObject {

  private ArrayList<Room> rooms;
  private Set<Movable> thingsInHere;
  String distantName; // the name shown to the player when viewed from a distance.

  public Structure(String name, String distantName) {
    this.label = name;
    this.distantName = distantName;
    this.rooms = new ArrayList<>();
  }

  /**
   * Used during initialization. Parses a line of text into a room.
   */
  public void addRoom(String rawLine) {
    String[] parts = rawLine.split(Util.COMPONENT_DELINEATOR);
    rooms.add(new Room(parts[1].trim(), parts[2].trim(), parts[3].trim()));
  }

  public String getDistantName() {
    return distantName;
  }

  public String getLabel() {
    return label;
  }

  public boolean isEnterable() {
    return this.rooms != null && this.rooms.size() > 0;
  }

  public String getRoomName(int id) {
    if (isValidRoomId(id)) {
      return rooms.get(id).getName();
    }
    return "INVALID ROOM";
  }

  public String getRoomDescription(int id) {
    if (isValidRoomId(id)) {
      return rooms.get(id).getDescription();
    }
    return "INVALID ROOM";
  }

  public List<PromptOption> getRoomExits(int id) {
    if (isValidRoomId(id)) {
      int[] exits = rooms.get(id).getExits();
      String[] exitTags = rooms.get(id).getExitTags();
      if (exits == null) {
        return null;
      }
      LinkedList<PromptOption> exitPOs = new LinkedList<>();
      for (int i = 0; i < exits.length; i++) {
        exitPOs.add(new PromptOption(exitTags[i], new SelectableInt(exits[i])));
      }
      return exitPOs;
    }
    return null;
  }

  public void putMovableObject(Movable m, int roomId) {
    if (thingsInHere == null) {
      thingsInHere = new HashSet<>();
    }
    thingsInHere.add(m);
    m.currentStructure = this;
    m.currentRoom = roomId;
  }

  public void removeMovableObject(Movable go) {
    thingsInHere.remove(go);
  }

  /*private List<Movable> getGameObjectsInRoom(int id) {
    return getGameObjectsInRoom(id, null);
  }*/

  public List<Movable> getGameObjectsInRoom(int id, Movable except) {
    if (isValidRoomId(id)) {
      LinkedList<Movable> list = null;
      for (Movable m : this.thingsInHere) {
        if (m != except && m.currentRoom == id) {
          if (list == null) {
            list = new LinkedList<>();
          }
          list.add(m);
        }
      }
      return list;
    }
    return null;
  }

  private boolean isValidRoomId(int id) {
    return rooms != null && id >= 0 && id < rooms.size();
  }

  public String getNameToDisplayAsOption() { return getDistantName(); }
}
