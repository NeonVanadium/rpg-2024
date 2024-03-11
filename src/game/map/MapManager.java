package game.map;

import game.ControlOrb;
import game.GameMaster;
import game.GameObject;
import game.Player;
import game.Util;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import game.events.EventManager;
import game.prompts.Direction;
import game.prompts.PromptOption;
import game.prompts.Selectable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapManager {

  private static GameMap map;
  private static List<GameObject> visible;
  private static List<GameObject> interactable;
  private static Map<String, Terrain> terrainTypes;
  private static boolean loadingTerrain = false;

  public static void init() {
    LinkedList<Terrain[]> tileRows = new LinkedList<>();
    terrainTypes = new HashMap<>();
    Util.parseFileAndDoEachLine(GameMaster.RESOURCE_FOLDER + "map.txt", (line) -> MapManager.processLine(line, tileRows));
    map = new GameMap(tileRows);
  }

  private static void processLine(String line, LinkedList<Terrain[]> tileRows) {
    if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
      // silly implmentation, but defaults to false, the first >> marks the terrain section,
      // and the second marks the map section
      loadingTerrain = !loadingTerrain;
    } else {
      if (loadingTerrain) {
        processTerrainLine(line);
      } else {
        tileRows.add(processMapLine(line));
      }
    }
  }

  private static void processTerrainLine(String line) {
    String[] parts = line.split(Util.COMPONENT_DELINEATOR);
    String symbol = parts[0].trim();
    terrainTypes.put(symbol, new Terrain(symbol, parts[1].trim(), Integer.parseInt(parts[2].trim())));
  }

  private static Terrain[] processMapLine(String line) {
    String[] tiles = line.split(",");
    Terrain[] row = new Terrain[tiles.length];
    int i = 0;
    for (String tile : tiles) {
      row[i] = terrainTypes.get(tile);
      if (row[i] == null) {
        throw new IllegalArgumentException("Tile " + tile + " in column " + i + " not found!");
      }
      i++;
    }
    return row;
  }


  /*
   * Exploring the open world.
   */

  public static void mapExplorationLoop(ControlOrb orb) {
    ArrayList<PromptOption> options = new ArrayList<>(5);
    for (Direction d : Direction.values()) {
      options.add(new PromptOption(d));
    }

    while (!EventManager.hasQueuedEvent()
        && CharacterManager.get("PLAYER").currentStructure == null) {
      orb.clear();
      orb.print(getSurroundingsDescription());
      determineOptionsForPlayer(options, orb);

      Selectable selection = orb.getChoiceFromOptions(options).getObject();

      if (selection instanceof Direction) {
        movePlayer((Direction) selection);
      } else {
        orb.respondToPlayerChoice(selection);
      }
    }
  }

  private static void movePlayer(Direction d) {
    int xMod, yMod;
    if (d == Direction.NORTH) {
      xMod = 0;
      yMod = 1;
    } else if (d == Direction.SOUTH) {
      xMod = 0;
      yMod = -1;
    } else if (d == Direction.EAST) {
      xMod = 1;
      yMod = 0;
    } else {
      xMod = -1;
      yMod = 0;
    }
    map.moveCharacter(CharacterManager.player(), GameCharacter.DEFAULT_MOVE_SPEED * xMod,
        GameCharacter.DEFAULT_MOVE_SPEED * yMod);
  }

  private static void determineOptionsForPlayer(List<PromptOption> options, ControlOrb orb) {
    GameCharacter player = CharacterManager.player();

    if (visible != null) {
      options.removeIf((PromptOption o) -> visible.contains(o.getObject()));
    }
    visible = map.visibleObjects(player, 30);
    interactable = map.visibleObjects(player, 10);

    if (visible != null && visible.size() > 0) {
      if (visible.size() == 1) {
        orb.print("You see " + getSeenDescription(visible.get(0)) + ".");
      } else {
        StringBuilder youSee = new StringBuilder("You see ");
        youSee.append(Util.commasAndAnds(visible, MapManager::getSeenDescription));
        youSee.append(".");
        orb.print(youSee.toString());
      }
    }

    if (interactable != null) for (GameObject o : interactable) {
      options.add(new PromptOption(o.getNameToDisplayAsOption().toUpperCase(), o));
    }
  }

  private static String getSurroundingsDescription() {
    return "The terrain is "
        + Util.lowercaseFirstCharacter(
            map.getTerrainTypeAt(Player.character.getX(), Player.character.getY())) + " here.";
  }

  /**
   * The description shown when the player can see this object in the open world.
   */
  private static String getSeenDescription(GameObject obj) {
    return obj.getNameToDisplayAsOption() + " " + getRelativeDirectionString(obj);
  }

  private static String getRelativeDirectionString(GameObject obj) {
    if (obj.isInSameSpotAs(CharacterManager.player())) {
      return "here";
    } else {
      if (obj.isFartherThan(Direction.NORTH, CharacterManager.player())) {
        if (obj.isFartherThan(Direction.EAST, CharacterManager.player())) {
          return "to the Northeast";
        } else if (obj.isFartherThan(Direction.WEST, CharacterManager.player())) {
          return "to the Northwest";
        }
        return "to the North";
      } else if (obj.isFartherThan(Direction.SOUTH, CharacterManager.player())) {
        if (obj.isFartherThan(Direction.EAST, CharacterManager.player())) {
          return "to the Southeast";
        } else if (obj.isFartherThan(Direction.WEST, CharacterManager.player())) {
          return "to the Southwest";
        }
        return "to the South";
      } else if (obj.isFartherThan(Direction.EAST, CharacterManager.player())) {
        return "to the East";
      } else { // if (obj.isFartherThan(Direction.WEST, player.character)) {
        return "to the West";
      }
    }
  }

  public static void removeFromBoard(GameObject go) {
    map.removeFromBoard(go);
  }

  public static void putGameObject(GameObject go) {
    map.putGameObject(go);
  }

  public static void putGameObject(GameObject go, double x, double y) {
    map.putGameObject(go, x, y);
  }
}
