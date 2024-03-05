package game;

import controller.Controller;
import game.characters.CharacterManager;
import game.prompts.Direction;
import game.prompts.PromptOption;
import game.prompts.Selectable;
import game.prompts.SelectableInt;
import game.characters.GameCharacter;
import game.characters.Gender;
import game.events.EventHandler;
import game.map.GameMap;
import game.structure.Structure;
import game.structure.StructureManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import view.View;

public class GameMaster {

  private static View view;
  private static Controller controller;
  private static GameMap map;
  private static Player player = new Player();
  private static List<GameObject> visible;
  private static List<GameObject> interactable;
  private static boolean running = true;

  public static void init(View v, Controller c) {
    view = v;
    controller = c;
  }

  public static void start() {
    player.character = new GameCharacter("Amnesiac", Gender.SOMETHING_ELSE);

    EventHandler.loadEvents();
    CharacterManager.loadCharacters();
    StructureManager.loadStructures();

    map = new GameMap();
    map.putGameObject(StructureManager.getStructure("ARENA_TOWER"), 50, 50);
    map.putGameObject(CharacterManager.get("SENJA"), 42, 8);

    Structure startTower = StructureManager.getStructure("ARENA_TOWER");
    enterStructure(player.character, startTower, 5);
    enterStructure(CharacterManager.get("BEYN"), startTower, 3);

    while (running) {
      if (!EventHandler.hasQueuedEvent()) EventHandler.checkEventTriggers(player);
      if (EventHandler.hasQueuedEvent()) {
        view.clear();
        EventHandler.runQueuedEvent(view, controller);
      }
      else if (player.character.currentStructure == null) {
        mapExplorationLoop();
      } else {
        structureLoop();
      }
    }

  }

  /*
   * Handling choices.
   */
  public static PromptOption getChoiceFromOptions(List<PromptOption> options) {
    List<String> optionLabels = new LinkedList<>();
    int i = 1;
    for (PromptOption o : options) {
      view.print(String.format("%d) %s", i, o.getLabel()));
      optionLabels.add(o.getLabel());
      i++;
    }
    int choice = controller.setOptions(optionLabels);
    if (!view.isFinishedDrawing()) {
      view.hurryUp();
      choice = controller.setOptions(optionLabels);
    }
    return options.get(choice);
  }

  public static void enterToContinue() {
    view.promptAnyInput();
    controller.enterToContinue();
    if (!view.isFinishedDrawing()) {
      view.hurryUp();
      controller.enterToContinue();
    }
  }

  /*
   * Exploring the open world.
   */

  private static void mapExplorationLoop() {
    ArrayList<PromptOption> options = new ArrayList<>(5);
    for (Direction d : Direction.values()) {
      options.add(new PromptOption(d));
    }

    while (player.character.currentStructure == null) {
      view.clear();
      view.print(getSurroundingsDescription());
      determineOptionsForPlayer(options);

      Selectable selection = getChoiceFromOptions(options).getObject();

      if (selection instanceof Direction) {
        movePlayer((Direction) selection);
      } else if (selection instanceof Structure) {
        enterStructure(player.character, (Structure) selection);
      } else if (selection instanceof GameCharacter) {
        conversationLoop((GameCharacter) selection);
      } else {
        view.print("It doesn't respond to you.");
      }
    }
  }

  private static void determineOptionsForPlayer(List<PromptOption> options) {
    if (visible != null) {
      options.removeIf((PromptOption o) -> visible.contains(o.getObject()));
    }
    visible = map.visibleObjects(player.character, 30);
    interactable = map.visibleObjects(player.character, 10);

    if (visible != null && visible.size() > 0) {
      if (visible.size() == 1) {
        view.print("You see " + getSeenDescription(visible.get(0)) + ".");
      } else {
        StringBuilder youSee = new StringBuilder("You see ");
        youSee.append(commasAndAnds(visible, GameMaster::getSeenDescription));
        youSee.append(".");
        view.print(youSee.toString());
      }
    }

    if (interactable != null) for (GameObject o : interactable) {
      options.add(new PromptOption(getNameToDisplayAsOption(o).toUpperCase(), o));
    }
  }

  private static <T extends Object> String commasAndAnds(List<T> list, Function<T, String> toStr) {
    if (list.isEmpty()) return null;
    if (list.size() == 1) return toStr.apply(list.get(0));
    if (list.size() == 2) return toStr.apply(list.get(0)) + " and " + toStr.apply(list.get(1));

    StringBuilder builder = new StringBuilder();
    int i = 0;
    Iterator<T> iterator = list.listIterator();
    while (iterator.hasNext()) {
      i++;
      if (i != 1) {
        builder.append(", ");
      }
      if (i == list.size()) {
        builder.append("and ");
      }
      builder.append(toStr.apply(iterator.next()));
    }
    return builder.toString();
  }

  private static String getSurroundingsDescription() {
    return "The land stretches out before you.";
  }

  /**
   * The description shown when the player can see this object in the open world.
   */
  private static String getSeenDescription(GameObject obj) {
    return getNameToDisplayAsOption(obj) + " " + getRelativeDirectionString(obj);
  }

  private static String getRelativeDirectionString(GameObject obj) {
    if (obj.isInSameSpotAs(player.character)) {
      return "here";
    } else {
      if (obj.isFartherThan(Direction.NORTH, player.character)) {
        if (obj.isFartherThan(Direction.EAST, player.character)) {
          return "to the Northeast";
        } else if (obj.isFartherThan(Direction.WEST, player.character)) {
          return "to the Northwest";
        }
        return "to the North";
      } else if (obj.isFartherThan(Direction.SOUTH, player.character)) {
        if (obj.isFartherThan(Direction.EAST, player.character)) {
          return "to the Southeast";
        } else if (obj.isFartherThan(Direction.WEST, player.character)) {
          return "to the Southwest";
        }
        return "to the South";
      } else if (obj.isFartherThan(Direction.EAST, player.character)) {
        return "to the East";
      } else { // if (obj.isFartherThan(Direction.WEST, player.character)) {
        return "to the West";
      }
    }
  }

  private static String getNameToDisplayAsOption(GameObject obj) {
    if (obj instanceof Structure) {
      return ((Structure) obj).getDistantName();
    } else if (obj instanceof GameCharacter) {
      return ((GameCharacter) obj).getGenericDescription();
    } else {
      return obj.getName();
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
    map.moveCharacter(player.character, GameCharacter.DEFAULT_MOVE_SPEED * xMod,
        GameCharacter.DEFAULT_MOVE_SPEED * yMod);
  }


  /*
   * Exploring structures.
   */
  private static void enterStructure(GameCharacter c, Structure s, int roomId) {
    if (s.isEnterable()) {
      c.setPosition(s.getX(), s.getY());
      map.removeFromBoard(c);
      s.putMovableObject(c);
      c.currentStructure = s;
      c.currentRoom = roomId;
    }
  }

  private static void enterStructure(GameCharacter c, Structure s) {
    enterStructure(c, s, 0);
  }

  /**
   * Removes the given character from its current structure, if there is one.
   * @param c
   */
  private static void leaveStructure(GameCharacter c) {
    c.currentStructure.removeMovableObject(c);
    c.currentStructure = null;
    c.currentRoom = -1;
    map.putGameObject(player.character);
  }

  private static void structureLoop() {
    Structure s = player.character.currentStructure;
    int roomId = player.character.currentRoom;
    view.clear();
    view.setTitle(s.getRoomName(roomId));
    view.print(s.getRoomDescription(roomId));

    List<Movable> objectsInRoom = s.getGameObjectsInRoom(roomId, player.character);
    if (objectsInRoom != null) {
      view.print("You see " + commasAndAnds(s.getGameObjectsInRoom(roomId, player.character),
          GameMaster::getSeenDescription));
    }

    PromptOption option =
        getChoiceFromOptions(s.getRoomExits(roomId));

    player.character.currentRoom = ((SelectableInt) option.getObject()).value;

    if (player.character.currentRoom == -1) {
      leaveStructure(player.character);
    }
  }

  /*
   * Conversations
   */
  public static void conversationLoop(GameCharacter c) {
    view.clear();
    view.print(c.name + " looks at you.");
    enterToContinue();
  }
}