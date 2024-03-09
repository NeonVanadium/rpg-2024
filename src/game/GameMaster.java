package game;

import controller.Controller;
import game.characters.CharacterManager;
import game.characters.Movable;
import game.map.MapManager;
import game.events.EventManager;
import game.prompts.Selectable;
import game.structure.Structure;
import game.structure.StructureManager;
import view.View;

public class GameMaster {

  private static View view;
  private static Controller controller;
  private static ControlOrb orb;
  public static final String RESOURCE_FOLDER = "redport_resources\\";

  private static boolean running = true;

  public static void init(View v, Controller c) {
    view = v;
    controller = c;
    orb = new ControlOrb(view, controller, GameMaster::processPlayerMove);
  }

  public static void start() {
    EventManager.loadEvents();
    CharacterManager.loadCharacters();
    StructureManager.loadStructures();
    MapManager.init();
    Util.parseFileAndDoEachLine(GameMaster.RESOURCE_FOLDER + "blocking.txt",
        GameMaster::readLineOfBlocking);
    gameLoop();
  }

  public static void gameLoop() {
    while (running) {
      if (!EventManager.hasQueuedEvent()) EventManager.checkEventTriggers();
      if (EventManager.hasQueuedEvent()) {
        view.clear();
        EventManager.runQueuedEvent(orb);
      }
      else if (Player.character.currentStructure == null) {
        MapManager.mapExplorationLoop(orb);
      } else {
        StructureManager.structureLoop(orb);
      }
    }
  }

  private static void readLineOfBlocking(String str) {
    String[] parts = str.split(" ");
    GameObject go = null;
    String subject = parts[0].trim();
    String structOrX = parts[1].trim();
    int lastPart = Integer.parseInt(parts[2].trim());

    // The Subject is placing a Character
    if (CharacterManager.get(subject) != null) {
      go = CharacterManager.get("PLAYER");
      // ...in a structure
      if (StructureManager.getStructure(structOrX) != null) {
        StructureManager.enterStructure((CharacterManager.get(subject)),
            structOrX, lastPart);
        return;
      }
      // the map placement is handled after the structure check.
    }
    else if (StructureManager.getStructure(subject) != null) { // subject is a structure
      go = StructureManager.getStructure(subject);
    }
    MapManager.putGameObject(go, Integer.parseInt(structOrX), lastPart);
  }

  /**
   * Individual handlers may have their own special logic for their own type, but certain
   * things are shared regardless of whether we're in a structure or open world.
   * These are accessed through the Orb, but this feels like GameMaster logic to me.
   */
  private static void processPlayerMove(Selectable selection) {
    if (selection instanceof Structure) {
      StructureManager.enterStructure(CharacterManager.player(), ((Structure) selection).label);
    }
    else if (selection instanceof Movable) {
      String title = "CONV_" + ((Movable) selection).getLabel();
      if (EventManager.hasEventWithTitle(title)) EventManager.queueEventWithTitle("CONV_TEMP");
    }
    else if (selection instanceof Item) {
      orb.clear();
      orb.print("Perhaps one day you'll be able to pick up " + ((Item) selection).description + " such as this.");
      orb.enterToContinue();
    }
    else {
      orb.clear();
      orb.print("It doesn't respond to you.");
      orb.enterToContinue();
    }
  }
}