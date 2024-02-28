import controller.ConsoleController;
import controller.Controller;
import game.GameMaster;
import view.ConsoleView;
import view.View;

public class Main {
  public static void main(String[] args) {
    View view = new ConsoleView();
    Controller controller = new ConsoleController(view);
    GameMaster gm = new GameMaster(view, controller);
    gm.start();
  }
}
