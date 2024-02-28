package game;

import controller.Controller;
import game.map.GameMap;
import view.View;

public class GameMaster {
  private final View view;
  private final Controller controller;

  private GameMap map;
  private GameCharacter playerCharacter;

  public GameMaster(View view, Controller controller) {
    this.view = view;
    this.controller = controller;
  }

  public void start() {
    view.setTitle("Untitled game");
    playerCharacter = new GameCharacter("Beyn");
    map = new GameMap();
    NavigationChoice();
  }


  private void NavigationChoice() {
    int choice;
    int xMod, yMod;
    while (true) {
      choice = giveOptions(new String[] {"North", "South", "East", "West"});
      if (choice == 0) {
        xMod = 0;
        yMod = 1;
      } else if (choice == 1) {
        xMod = 0;
        yMod = -1;
      } else if (choice == 2) {
        xMod = 1;
        yMod = 0;
      } else {
        xMod = -1;
        yMod = 0;
      }
      while (map.moveCharacter(playerCharacter, 10 * xMod, 10 * yMod)) {
        view.wait(500);
        view.print(playerCharacter.toString());
      }
    }
  }

  private int giveOptions(String[] ops) {
    view.print("The land stretches out before you...");
    for (int i = 0; i < ops.length; i++) {
      view.print(String.format("%d) %s", i+1, ops[i]));
    }
    return controller.setOptions(ops);
  }
}
