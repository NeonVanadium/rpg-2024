package game.map;

import game.ControlOrb;
import game.GameMaster;
import game.GameObject;
import game.Util;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import game.events.EventHandler;
import game.prompts.Direction;
import game.prompts.PromptOption;
import game.prompts.Selectable;
import game.structure.Structure;
import game.structure.StructureManager;
import java.util.ArrayList;
import java.util.List;

public class MapManager {

  private static GameMap map;
  private static List<GameObject> visible;
  private static List<GameObject> interactable;

  public static void init() {
    map = new GameMap();
    map.putGameObject(StructureManager.getStructure("ARENA_TOWER"), 50, 50);
    map.putGameObject(CharacterManager.get("SENJA"), 42, 8);
  }

  /*
   * Exploring the open world.
   */

  public static void mapExplorationLoop(ControlOrb orb) {
    ArrayList<PromptOption> options = new ArrayList<>(5);
    for (Direction d : Direction.values()) {
      options.add(new PromptOption(d));
    }

    while (!EventHandler.hasQueuedEvent()
        && CharacterManager.get("PLAYER").currentStructure == null) {
      orb.clear();
      orb.print(getSurroundingsDescription());
      determineOptionsForPlayer(options, orb);

      Selectable selection = orb.getChoiceFromOptions(options).getObject();

      if (selection instanceof Direction) {
        movePlayer((Direction) selection);
      } else if (selection instanceof Structure) {
        StructureManager.enterStructure(CharacterManager.player(), ((Structure) selection).getName());
      } else if (selection instanceof GameCharacter) {
        EventHandler.queueEventWithTitle("CONV_TEMP");
      } else {
        orb.print("It doesn't respond to you.");
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
    if (visible != null) {
      options.removeIf((PromptOption o) -> visible.contains(o.getObject()));
    }
    visible = map.visibleObjects(CharacterManager.get("PLAYER"), 30);
    interactable = map.visibleObjects(CharacterManager.get("PLAYER"), 10);

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
}
