package game;

import controller.Controller;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import game.combat.CombatManager;
import game.map.MapManager;
import game.events.EventManager;
import game.prompts.Selectable;
import game.structure.Structure;
import game.structure.StructureManager;
import shared.Util;
import view.View;

public class GameMaster {

  private static View view;
  private static Controller controller;
  private static ControlOrb orb;
  private static String resourceFolder;
  private static Timekeeper time;

  private static boolean running = true;

  public static void init(View v, Controller c, String resourceFolder) {
    view = v;
    controller = c;
    GameMaster.resourceFolder = resourceFolder;
    orb = new ControlOrb(view, controller, GameMaster::processPlayerMove);
    time = new Timekeeper(view);
  }

  public static String getResourceFolder() {
    return resourceFolder;
  }

  public static void start() {
    TagAndTopicManager.initTopicsAndAttributes();
    EventManager.loadEvents();
    CharacterManager.loadCharacters();
    StructureManager.loadStructures();
    MapManager.init();
    Util.parseFileAndDoEachLine(GameMaster.resourceFolder + "blocking.txt",
        GameMaster::readLineOfBlocking);
    gameLoop();
  }

  public static void gameLoop() {
    while (running) {
      if (CombatManager.isCombatPending()) CombatManager.startCombat(orb);
      if (!EventManager.hasQueuedEvent()) EventManager.checkEventTriggers();
      if (EventManager.hasQueuedEvent()) {
        view.clear();
        time.pause();
        EventManager.runQueuedEvent(orb);
        time.resume();
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

    String subject = parts[0].trim();
    String structOrX = parts[1].trim();
    placementHelper(subject, structOrX, parts[2].trim());
  }

  public static void placementHelper(String subject, String structOrX, String roomOrY) {
    GameObject go = null;
    // The Subject is placing a Character
    if (CharacterManager.contains(subject)) {
      go = CharacterManager.get(subject);
      // ...in a structure
      if (StructureManager.getStructure(structOrX) != null) {
        StructureManager.enterStructure((CharacterManager.get(subject)),
            structOrX, Integer.parseInt(roomOrY));
        return;
      } else if (((Movable) go).currentStructure != null) {
        StructureManager.leaveStructure((Movable) go);
      }

      // the map placement is handled after the structure check.
    }
    else if (StructureManager.getStructure(subject) != null) { // subject is a structure
      go = StructureManager.getStructure(subject);
    }
    MapManager.putGameObject(go, Integer.parseInt(structOrX), Integer.parseInt(roomOrY));
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
    else if (selection instanceof GameCharacter) {
      String title = "CONV_" + ((GameCharacter) selection).getLabel();
      if (EventManager.hasEventWithTitle(title)) EventManager.queueEventWithTitle(title);
      else EventManager.queueEventWithTitle("CONV_TEMP");
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