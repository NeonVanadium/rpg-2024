package game;

import controller.Controller;
import game.characters.CharacterManager;
import game.map.MapManager;
import game.events.EventHandler;
import game.structure.StructureManager;
import view.View;

public class GameMaster {

  private static View view;
  private static Controller controller;
  private static ControlOrb orb;

  private static boolean running = true;

  public static void init(View v, Controller c) {
    view = v;
    controller = c;
    orb = new ControlOrb(view, controller);
  }

  public static void start() {
    EventHandler.loadEvents();
    CharacterManager.loadCharacters();
    StructureManager.loadStructures();
    MapManager.init();

    Player.character = CharacterManager.get("PLAYER");
    MapManager.putGameObject(StructureManager.getStructure("ARENA_TOWER"), 5, 5);
    MapManager.putGameObject(CharacterManager.get("SENJA"), 42, 8);
    StructureManager.enterStructure(Player.character, "ARENA_TOWER", 5);
    StructureManager.enterStructure(CharacterManager.get("BEYN"), "ARENA_TOWER", 3);

    gameLoop();
  }

  public static void gameLoop() {
    while (running) {
      if (!EventHandler.hasQueuedEvent()) EventHandler.checkEventTriggers();
      if (EventHandler.hasQueuedEvent()) {
        view.clear();
        EventHandler.runQueuedEvent(orb);
      }
      else if (Player.character.currentStructure == null) {
        MapManager.mapExplorationLoop(orb);
      } else {
        StructureManager.structureLoop(orb);
      }
    }
  }
}