import controller.ConsoleController;
import controller.Controller;
import controller.KeyboardMouseController;
import game.GameMaster;
import view.ConsoleView;
import view.View;
import view.window.GraphicsView;

public class Main {

  private static final boolean USE_CONSOLE_CONTROLLER = true;
  private static final boolean USE_CONSOLE_VIEW = false;

  public static void main(String[] args) {
    View view;
    Controller controller;

    if (USE_CONSOLE_VIEW) {
      view = new ConsoleView();
    } else {
      view = new GraphicsView();
    }

    if (USE_CONSOLE_CONTROLLER) {
      controller = new ConsoleController();
    } else {
      controller = new KeyboardMouseController();
    }

    GameMaster.init(view, controller);
    GameMaster.start();
  }
}
