import controller.Controller;
import controller.KeyboardMouseController;
import game.GameMaster;
import view.View;
import view.window.GraphicsManager;

public class Main {

  public static void main(String[] args) {
    String resourceFolder = args[0] + "\\";

    View view;
    GraphicsManager graphicsManager = new GraphicsManager(resourceFolder);
    view = graphicsManager.getView();

    KeyboardMouseController controller = new KeyboardMouseController();
    graphicsManager.addKeyListener(controller.getKeyAdapter());
    graphicsManager.addControllerPipeline(controller.makePipeline());

    GameMaster.init(view, controller, resourceFolder);
    GameMaster.start();
  }
}
