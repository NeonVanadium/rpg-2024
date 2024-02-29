package game;

import controller.Controller;
import game.Prompts.Direction;
import game.Prompts.PromptOption;
import game.Prompts.Selectable;
import game.Prompts.SelectableInt;
import game.characters.GameCharacter;
import game.characters.Gender;
import game.map.GameMap;
import game.structure.Structure;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import view.View;

public class GameMaster {

  private final View view;
  private final Controller controller;
  private GameMap map;
  private Player player = new Player();
  private List<GameObject> visible;
  private List<GameObject> interactable;
  private boolean running = true;

  public GameMaster(View view, Controller controller) {
    this.view = view;
    this.controller = controller;
  }

  public void start() {
    player.character = new GameCharacter("Amnesiac", Gender.SOMETHING_ELSE);
    GameCharacter beyn = new GameCharacter("Beyn", Gender.MAN);
    GameCharacter senja = new GameCharacter("Senja", Gender.WOMAN);

    map = new GameMap();
    map.putGameObject(player.character, 25, 25);
    map.putGameObject(new Structure("Southeast Tower", "a tower"), 50, 50);
    map.putGameObject(beyn, 10, 0);
    map.putGameObject(senja, 42, 8);

    view.setTitle("Untitled game");
    while (running) {
      if (player.currentStructure == null) {
        MapExplorationLoop();
      } else {
        StructureLoop();
      }
    }

  }

  /*
   * Handling choices.
   */

  private PromptOption getChoiceFromOptions(List<PromptOption> options) {
    List<String> optionLabels = new LinkedList<>();
    int i = 1;
    for (PromptOption o : options) {
      view.print(String.format("%d) %s", i, o.getLabel()));
      optionLabels.add(o.getLabel());
      i++;
    }
    return options.get(controller.setOptions(optionLabels));
  }

  private void enterToContinue() {
    view.print("<Enter to continue>");
    controller.setOptions(null);
  }

  /*
   * Exploring the open world.
   */

  private void MapExplorationLoop() {
    ArrayList<PromptOption> options = new ArrayList<>(5);
    for (Direction d : Direction.values()) {
      options.add(new PromptOption(d));
    }

    while (player.currentStructure == null) {
      view.print("The land stretches out before you...");
      determineOptionsForPlayer(options);
      //view.print("You are at " + player.character.getPosition().x + ", " + player.character.getPosition().y);

      Selectable selection = getChoiceFromOptions(options).getObject();

      if (selection instanceof Direction) {
        movePlayer((Direction) selection);
      } else if (selection instanceof Structure) {
        enterStructure((Structure) selection);
      } else if (selection instanceof GameCharacter) {
        ConversationLoop((GameCharacter) selection);
      } else {
        view.print("It doesn't respond to you.");
      }
    }
  }

  private void determineOptionsForPlayer(List<PromptOption> options) {
    if (visible != null) {
      options.removeIf((PromptOption o) -> visible.contains(o.getObject()));
    }
    visible = map.visibleObjects(player.character, 50);
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

  /**
   * The description shown when the player can see this object in the open world.
   */
  private String getSeenDescription(GameObject obj) {
    return getNameToDisplayAsOption(obj) + " " + getRelativeDirectionString(obj);
  }

  private String getRelativeDirectionString(GameObject obj) {
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

  private String getNameToDisplayAsOption(GameObject obj) {
    if (obj instanceof Structure) {
      return ((Structure) obj).getDistantName();
    } else if (obj instanceof GameCharacter) {
      return ((GameCharacter) obj).getGenericDescription();
    } else {
      return obj.getName();
    }
  }

  private void movePlayer(Direction d) {
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

  private boolean enterStructure(Structure s) {
    if (s.isEnterable()) {
      view.print(player.character.name + " enters " + s.name);
      player.character.setPosition(s.getX(), s.getY());
      player.currentStructure = s;
      player.currentRoom = 0;
      return true;
    }
    return false;
  }

  private void leaveStructure() {
    player.currentStructure = null;
    player.currentRoom = -1;
    enterToContinue();
  }

  private void StructureLoop() {
    while (player.currentStructure != null) {
      view.setTitle(player.currentStructure.getRoomName(player.currentRoom));
      view.print(player.currentStructure.getRoomDescription(player.currentRoom));

      PromptOption option =
          getChoiceFromOptions(player.currentStructure.getRoomExits(player.currentRoom));

      player.currentRoom = ((SelectableInt)option.getObject()).value;

      if (player.currentRoom == -1) {
        leaveStructure();
      }
    }
  }

  /*
   * Conversations
   */
  public void ConversationLoop(GameCharacter c) {
    view.print(c.name + " looks at you.");
    enterToContinue();
  }
}
