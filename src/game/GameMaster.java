package game;

import controller.Controller;
import game.characters.CharacterManager;
import game.map.MapManager;
import game.prompts.PromptOption;
import game.events.EventHandler;
import game.structure.Structure;
import game.structure.StructureManager;
import java.util.LinkedList;
import java.util.List;
import view.View;

public class GameMaster {

  private static View view;
  private static Controller controller;
  private static Player player = new Player();
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

    player.character = CharacterManager.get("PLAYER");
    StructureManager.enterStructure(player.character, "ARENA_TOWER", 5);
    StructureManager.enterStructure(CharacterManager.get("BEYN"), "ARENA_TOWER", 3);

    gameLoop();
  }

  public static void gameLoop() {
    while (running) {
      if (!EventHandler.hasQueuedEvent()) EventHandler.checkEventTriggers(player);
      if (EventHandler.hasQueuedEvent()) {
        view.clear();
        EventHandler.runQueuedEvent(orb);
      }
      else if (player.character.currentStructure == null) {
        MapManager.mapExplorationLoop(orb);
      } else {
        StructureManager.structureLoop(orb);
      }
    }
  }
}