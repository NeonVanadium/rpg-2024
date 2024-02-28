package game;

import controller.Controller;
import game.Prompts.Direction;
import game.Prompts.PromptOption;
import game.map.GameMap;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import view.View;

public class GameMaster {

  private final View view;
  private final Controller controller;
  private GameMap map;
  private Player player = new Player();
  private List<GameObject> visible;

  public GameMaster(View view, Controller controller) {
    this.view = view;
    this.controller = controller;
  }

  public void start() {
    player.character = new GameCharacter("Amnesiac");
    GameCharacter beyn = new GameCharacter("Beyn");

    map = new GameMap();
    map.putGameObject(player.character, 25, 25);
    map.putGameObject(new Structure("Tower"), 50, 50);
    map.putGameObject(beyn, 10, 0);

    view.setTitle("Untitled game");
    MapExplorationLoop();
  }


  private void MapExplorationLoop() {
    ArrayList<PromptOption> options = new ArrayList<>(5);
    for (Direction d : Direction.values()) {
      options.add(new PromptOption(d));
    }

    while (player.currentStructure == null) {
      determineOptionsForPlayer(options);

      view.print("You are at " + player.character.getPosition().x + ", "
          + player.character.getPosition().y);
      view.print("The land stretches out before you...");

      Object selection = getChoiceFromOptions(options).getObject();

      if (selection instanceof Direction) {
        movePlayer((Direction) selection);
      } else if (selection instanceof Structure) {
        enterStructure((Structure) selection);
      } else {
        view.print("That sure is a " + selection.getClass());
      }
    }
  }

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

  private void enterStructure(Structure s) {
    view.print(player.character.name + " enters " + s.name);
    player.character.setPosition(s.getX(), s.getY());
    player.currentStructure = s;
  }

  private void determineOptionsForPlayer( List<PromptOption> options) {
    if (visible != null) {
      options.removeIf((PromptOption o) -> visible.contains(o.getObject()));
    }
    visible = map.visibleObjects(player.character, 10);
    if (visible != null && visible.size() > 0) {
      view.print("You see:");
      for (GameObject o : visible) {
        view.print("-- " + o.name);
        options.add(new PromptOption(o.name, o));
      }
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
}
