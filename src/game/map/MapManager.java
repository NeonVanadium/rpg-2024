package game.map;

import game.ControlOrb;
import game.GameObject;
import game.Util;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import game.events.EventManager;
import game.prompts.Direction;
import game.prompts.PromptOption;
import game.prompts.Selectable;
import java.util.ArrayList;
import java.util.List;

public class MapManager {

  private static GameMap map;
  private static List<GameObject> visible;
  private static List<GameObject> interactable;
  private static String[] terrainLabels = new String[5];

  public static void init() {
    map = new GameMap();
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


    terrainLabels[0] = map.getTerrainTypeAt(player.getX(), player.getY()) + " here";
    /*terrainLabels[1] = map.getTerrainTypeAt(player.getX(), player.getY() + map.tileSize);// + " to the north";
    terrainLabels[3] = map.getTerrainTypeAt(player.getX() + map.tileSize, player.getY());// + " to the east";
    terrainLabels[2] = map.getTerrainTypeAt(player.getX(), player.getY() - map.tileSize);// + " to the south";
    terrainLabels[4] = map.getTerrainTypeAt(player.getX() - map.tileSize, player.getY());// + " to the west";

    String terrainCompassMiddle = terrainLabels[4] + " <- " + map.getTerrainTypeAt(player.getX(), player.getY()) + " -> " + terrainLabels[3];
    //String terrainCompassBottom = terrainLabels[2] + "\n\\/"; // (terrainCompassMiddle.length() - terrainLabels[1].length() / 2);

    orb.print(Util.centerText(terrainLabels[1], terrainCompassMiddle));
    orb.print(Util.centerText("/\\", terrainCompassMiddle));
    orb.print(terrainCompassMiddle);
    orb.print(Util.centerText("\\/", terrainCompassMiddle));
    orb.print(Util.centerText(terrainLabels[2], terrainCompassMiddle));*/

    orb.print("The terrain is " + terrainLabels[0] + ".");

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
    return "The land stretches out before you.";
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
