package game.structure;

import game.Util;
import java.util.HashMap;
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

}
