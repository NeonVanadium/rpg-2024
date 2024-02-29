package game.structure;

import game.GameObject;

public class Structure extends GameObject {

  private Room[] rooms = new Room[]{ new Room("Vine-touched Platform", "A circular, stone platform high atop a strange, vine-covered tower.") };
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
    // because this is used in navigation.
    return getDistantName();
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

  private boolean isValidRoomId(int id) {
    return rooms != null && id >= 0 && id < rooms.length;
  }

}
