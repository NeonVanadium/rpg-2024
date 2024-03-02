import controller.ConsoleController;
import controller.Controller;
import game.GameMaster;
import view.View;
import view.window.WindowView;

public class Main {
  public static void main(String[] args) {
    View view = new WindowView();
    Controller controller = new ConsoleController(view);
    GameMaster gm = new GameMaster(view, controller);
    gm.start();
  }
}
