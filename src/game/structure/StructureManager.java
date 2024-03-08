package game.structure;

import game.ControlOrb;
import game.GameObject;
import game.Item;
import game.Player;
import game.Util;
import game.characters.CharacterManager;
import game.characters.Movable;
import game.map.MapManager;
import game.prompts.PromptOption;
import game.prompts.Selectable;
import game.prompts.SelectableInt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureManager {

  private static final String structuresFilePath = "resources\\structures_and_rooms.txt";
  private static Map<String, Structure> structures;
  private static Structure structBeingBuilt;

  public static void loadStructures() {
    if (structures == null) {
      structures = new HashMap<>();
      structBeingBuilt = null;
      Util.parseFileAndDoEachLine(structuresFilePath, StructureManager::processLine);
      if (structBeingBuilt != null) {
        structures.put(structBeingBuilt.getLabel(), structBeingBuilt);
        structBeingBuilt = null;
      }
    }
    structures.get("ARENA_TOWER").putMovableObject(new Item("IRON_SWORD", "Iron Sword",
        "a rusty iron sword"), 4);
  }

  public static void processLine(String line) {
    if (!line.isBlank()) {
      if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
        if (structBeingBuilt != null) {
          structures.put(structBeingBuilt.getLabel(), structBeingBuilt);
        }
        // the label and the distant-name
        String[] splitLine = line.substring(Util.ENTRY_START_SYMBOL.length()).split("-");
        structBeingBuilt = new Structure(splitLine[0].trim(), splitLine[1].trim());
      } else if (structBeingBuilt != null) {
        structBeingBuilt.addRoom(line);
      }
    }
  }

  public static void structureLoop(ControlOrb orb) {
    Structure s = CharacterManager.player().currentStructure;
    int roomId = CharacterManager.player().currentRoom;
    orb.clear();
    orb.setTitle(s.getRoomName(roomId));
    orb.print(s.getRoomDescription(roomId));

    List<game.Movable> objectsInRoom = s.getGameObjectsInRoom(roomId, CharacterManager.player());
    if (objectsInRoom != null) {
      orb.print("You see " + Util.commasAndAnds(s.getGameObjectsInRoom(roomId, CharacterManager.player()),
          GameObject::getNameToDisplayAsOption) + ".");
    }

    List<PromptOption> options = s.getRoomExits(roomId);

    if (objectsInRoom != null) for (game.Movable m : objectsInRoom) {
      options.add(new PromptOption(m.getNameToDisplayAsOption(), m));
    }

    Selectable selection = orb.getChoiceFromOptions(options).getObject();

    if (selection instanceof SelectableInt) {
      setRoom(CharacterManager.player(), ((SelectableInt) selection).value);
    } else {
      orb.respondToPlayerChoice(selection);
    }

    if (CharacterManager.player().currentRoom == -1) {
      leaveStructure(CharacterManager.player());
    }
  }

  public static Structure getStructure(String label) {
    return structures.get(label);
  }

  public static void enterStructure(Movable c, String structureLabel, int roomId) {
    Structure s = getStructure(structureLabel);
    if (s.isEnterable()) {
      c.setPosition(s.getX(), s.getY());
      MapManager.removeFromBoard(c);
      s.putMovableObject(c, roomId);

      if (c == CharacterManager.player()) {
        for (Movable m : Player.getPartyMembers()) {
          enterStructure(m, structureLabel, roomId);
        }
      }
    }
  }

  public static void enterStructure(Movable c, String structureLabel) {
    enterStructure(c, structureLabel, 0);
  }

  /**
   * Removes the given character from its current structure, if there is one.
   * @param c
   */
  private static void leaveStructure(Movable c) {
    c.currentStructure.removeMovableObject(c);
    c.currentStructure = null;
    c.currentRoom = -1;
    MapManager.putGameObject(c);
    if (c == CharacterManager.player()) {
      for (Movable m : Player.getPartyMembers()) {
        leaveStructure(m);
      }
    }
  }

  private static void setRoom(Movable c, int newRoomId) {
    c.currentRoom = newRoomId;
    if (c == CharacterManager.player()) {
      for (Movable m : Player.getPartyMembers()) {
        setRoom(m, newRoomId);
      }
    }
  }
}
