import controller.ConsoleController;
import controller.Controller;
import controller.KeyboardMouseController;
import game.GameMaster;
import view.ConsoleView;
import view.View;
import view.window.GraphicsManager;

public class Main {

  private static final boolean USE_CONSOLE_CONTROLLER = false;
  private static final boolean USE_CONSOLE_VIEW = false;
  private static final boolean playingRedport = true; // temp while there's exactly two games.
  public static final String RESOURCE_FOLDER = playingRedport ? "redport_resources\\" : "resources\\";

  public static void main(String[] args) {
    View view;
    Controller controller;
    GraphicsManager graphicsManager;

    if (USE_CONSOLE_VIEW) {
      view = new ConsoleView();
    } else {
      graphicsManager = new GraphicsManager();
      view = graphicsManager.getView();
    }

    if (USE_CONSOLE_CONTROLLER) {
      controller = new ConsoleController();
      graphicsManager.addKeyListener(new KeyboardMouseController().getKeyAdapter());
    } else {
      controller = new KeyboardMouseController();
      graphicsManager.addKeyListener(((KeyboardMouseController)controller).getKeyAdapter());
    }

    GameMaster.init(view, controller, RESOURCE_FOLDER);
    GameMaster.start();
  }
}
