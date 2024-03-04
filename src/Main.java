import controller.ConsoleController;
import controller.Controller;
import game.GameMaster;
import view.ConsoleView;
import view.View;
import view.window.GraphicsView;

public class Main {

  public static void main(String[] args) {
    View view = new GraphicsView();
    Controller controller = new ConsoleController(view);
    GameMaster.init(view, controller);
    GameMaster.start();
  }
}
