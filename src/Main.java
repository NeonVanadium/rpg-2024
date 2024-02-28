import src.controller.ConsoleController;
import src.controller.Controller;
import src.map.GameMap;
import src.view.ConsoleInterface;
import src.view.UserInterface;

public class Main {

  private static final UserInterface view = new ConsoleInterface();
  private static final Controller controller = new ConsoleController();

  public static void main(String[] args) {
    view.setTitle("Untitled game");
    GameCharacter test = new GameCharacter("Bein");
    GameMap map = new GameMap();
    int choice;

    int xMod, yMod;



    while (true) {
      choice = giveOptions(new String[]{"North", "South", "East", "West"});
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
      while (map.moveCharacter(test, 10 * xMod, 10 * yMod)) {
        view.wait(500);
        view.print(test.toString());
      }
    }
  }

  private static int giveOptions(String[] ops) {
    view.print("The land stretches out before you...");
    for (int i = 0; i < ops.length; i++) {
      view.print(String.format("%d) %s", i+1, ops[i]));
    }
    return controller.setOptions(ops);
  }
}
