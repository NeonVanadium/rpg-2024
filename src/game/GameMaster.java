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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

    Structure ARENA_TOWER = new Structure("ARENA_TOWER", "a tower");

    map = new GameMap();
    map.putGameObject(ARENA_TOWER, 50, 50);
    map.putGameObject(CharacterManager.getCharacterByLabel("BEYN"), 10, 0);
    map.putGameObject(CharacterManager.getCharacterByLabel("SENJA"), 42, 8);

    enterStructure(ARENA_TOWER);
    player.character.currentRoom = 1;

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
    return options.get(controller.setOptions(optionLabels));
  }

  public static void enterToContinue() {
    view.promptAnyInput();
    controller.enterToContinue();
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
        enterStructure((Structure) selection);
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
        view.print("You see " + getSeenDescription(visible.get(0)));
      } else {
        view.print("In the distance, you see:");
        for (GameObject o : visible) {
          view.print(" - " + getSeenDescription(o));
        }
      }
    }

    if (interactable != null) for (GameObject o : interactable) {
      options.add(new PromptOption(getNameToDisplayAsOption(o).toUpperCase(), o));
    }
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
      return "here.";
    } else {
      if (obj.isFartherThan(Direction.NORTH, player.character)) {
        if (obj.isFartherThan(Direction.EAST, player.character)) {
          return "to the Northeast.";
        } else if (obj.isFartherThan(Direction.WEST, player.character)) {
          return "to the Northwest.";
        }
        return "to the North";
      } else if (obj.isFartherThan(Direction.SOUTH, player.character)) {
        if (obj.isFartherThan(Direction.EAST, player.character)) {
          return "to the Southeast.";
        } else if (obj.isFartherThan(Direction.WEST, player.character)) {
          return "to the Southwest.";
        }
        return "to the South";
      } else if (obj.isFartherThan(Direction.EAST, player.character)) {
        return "to the East.";
      } else { // if (obj.isFartherThan(Direction.WEST, player.character)) {
        return "to the West.";
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

  private static boolean enterStructure(Structure s) {
    if (s.isEnterable()) {
      map.putGameObject(player.character, s.getX(), s.getY());
      player.character.currentStructure = s;
      player.character.currentRoom = 0;
      return true;
    }
    return false;
  }

  private static void leaveStructure() {
    player.character.currentStructure = null;
    player.character.currentRoom = -1;
  }

  private static void structureLoop() {
    view.clear();
    view.setTitle(player.character.currentStructure.getRoomName(player.character.currentRoom));
    view.print(player.character.currentStructure.getRoomDescription(player.character.currentRoom));

    PromptOption option =
        getChoiceFromOptions(player.character.currentStructure.getRoomExits(player.character.currentRoom));

    player.character.currentRoom = ((SelectableInt) option.getObject()).value;

    if (player.character.currentRoom == -1) {
      leaveStructure();
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