package game.structure;

import game.ControlOrb;
import game.GameObject;
import game.Movable;
import game.Util;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import game.map.MapManager;
import game.prompts.PromptOption;
import game.prompts.SelectableInt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureManager {

  private static final String structuresFilePath = "resources\\structures_and_rooms.txt";
  private static Map<String, Structure> structures;
  private static Structure structBeingBuilt;

  public static void loadStructures() {
    structures = new HashMap<>();
    structBeingBuilt = null;
    Util.parseFileAndDoEachLine(structuresFilePath, StructureManager::processLine);
    if (structBeingBuilt != null) {
      structures.put(structBeingBuilt.getName(), structBeingBuilt);
      structBeingBuilt = null;
    }
  }

  public static void processLine(String line) {
    if (!line.isBlank() && !line.startsWith("//")) {
      if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
        if (structBeingBuilt != null) {
          structures.put(structBeingBuilt.getName(), structBeingBuilt);
        }
        // the label and the distant-name
        String[] splitLine = line.substring(Util.ENTRY_START_SYMBOL.length()).split("-");
        structBeingBuilt = new Structure(splitLine[0].trim(), splitLine[1].trim());
      } else if (structBeingBuilt != null) {
        structBeingBuilt.addRoom(line);
      }
    }
  }

  public static Structure getStructure(String label) {
    return structures.get(label);
  }

  public static void enterStructure(GameCharacter c, String structureLabel, int roomId) {
    Structure s = getStructure(structureLabel);
    if (s.isEnterable()) {
      c.setPosition(s.getX(), s.getY());
      MapManager.removeFromBoard(c);
      s.putMovableObject(c);
      c.currentStructure = s;
      c.currentRoom = roomId;
    }
  }

  public static void enterStructure(GameCharacter c, String structureLabel) {
    enterStructure(c, structureLabel, 0);
  }

  /**
   * Removes the given character from its current structure, if there is one.
   * @param c
   */
  private static void leaveStructure(GameCharacter c) {
    c.currentStructure.removeMovableObject(c);
    c.currentStructure = null;
    c.currentRoom = -1;
    MapManager.putGameObject(c);
  }

  public static void structureLoop(ControlOrb orb) {
    Structure s = CharacterManager.player().currentStructure;
    int roomId = CharacterManager.player().currentRoom;
    orb.clear();
    orb.setTitle(s.getRoomName(roomId));
    orb.print(s.getRoomDescription(roomId));

    List<Movable> objectsInRoom = s.getGameObjectsInRoom(roomId, CharacterManager.player());
    if (objectsInRoom != null) {
      orb.print("You see " + Util.commasAndAnds(s.getGameObjectsInRoom(roomId, CharacterManager.player()),
          GameObject::getNameToDisplayAsOption) + ".");
    }

    PromptOption option =
        orb.getChoiceFromOptions(s.getRoomExits(roomId));

    CharacterManager.player().currentRoom = ((SelectableInt) option.getObject()).value;

    if (CharacterManager.player().currentRoom == -1) {
      leaveStructure(CharacterManager.player());
    }
  }

}
