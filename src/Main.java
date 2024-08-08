import controller.ConsoleController;
import controller.Controller;
import controller.KeyboardMouseController;
import game.GameMaster;
import view.View;
import view.window.GraphicsManager;

public class Main {

  private static final boolean USE_CONSOLE_CONTROLLER = false;

  public static void main(String[] args) {
    String resourceFolder = args[0] + "\\";

    View view;
    Controller controller;
    GraphicsManager graphicsManager = new GraphicsManager(resourceFolder);
    view = graphicsManager.getView();

    if (USE_CONSOLE_CONTROLLER) {
      controller = new ConsoleController();
      graphicsManager.addKeyListener(new KeyboardMouseController().getKeyAdapter());
    } else {
      controller = new KeyboardMouseController();
      graphicsManager.addKeyListener(((KeyboardMouseController)controller).getKeyAdapter());
      graphicsManager.addControllerPipeline(((KeyboardMouseController) controller).makePipeline());
    }

    GameMaster.init(view, controller, resourceFolder);
    GameMaster.start();
  }
}
