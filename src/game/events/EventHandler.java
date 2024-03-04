package game.events;

import controller.Controller;
import game.GameMaster;
import game.Player;
import game.Util;
import game.characters.CharacterManager;
import game.prompts.PromptOption;
import game.prompts.SelectableString;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import view.View;

/**
 * A class to handle event-related tasks, including parsing them from the file.
 */
public class EventHandler {


  private static Map<String, Event> events;
  private static Collection<String> completedEvents;

  private static Event eventBeingBuilt; // only used in loadevents/processline.
  private static String eventToRun;
  private static final String eventFilesPath = "resources\\events\\";

  /**
   * Load events from all files in resources/events.
   */
  public static void loadEvents() {
    events = new HashMap<>();
    completedEvents = new HashSet<>();
    eventBeingBuilt = null;
    for (String fileName : new File(eventFilesPath).list()) {
      Util.parseFileAndDoEachLine(eventFilesPath + fileName, EventHandler::processLine);
      if (eventBeingBuilt != null) {
        events.put(eventBeingBuilt.title, eventBeingBuilt);
        eventBeingBuilt = null;
      }
    }
  }

  private static void processLine(String line) {
    if (!line.isBlank() && !line.startsWith("//")) {
      if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
        if (eventBeingBuilt != null) {
          events.put(eventBeingBuilt.title, eventBeingBuilt);
        }
        eventBeingBuilt = new Event(line.substring(Util.ENTRY_START_SYMBOL.length()));
      } else if (eventBeingBuilt != null) {
        eventBeingBuilt.addPart(line);
      }
    }
  }

  private static void queueEvent(String title) {
    if (!completedEvents.contains(title)) {
      eventToRun = title;
      System.out.println("Queued event " + title);
    }
  }

  public static boolean hasQueuedEvent() {
    return eventToRun != null;
  }

  public static void runQueuedEvent(View view, Controller controller) {
    Event e = events.get(eventToRun);
    completedEvents.add(eventToRun);

    Class prevType = null;

    for (EventPart eventPart : e.getEventParts()) {
      if (eventPart instanceof TextEventPart) {
        if (prevType == TextEventPart.class) {
          GameMaster.enterToContinue();
        }
        view.clear();
        view.print(((TextEventPart) eventPart).text);
      } else if (eventPart instanceof ChoiceEventPart) {
        // TODO : I really don't want this method to be public, find a way to get this without exposing anything on the game master
        PromptOption chosen = GameMaster.getChoiceFromOptions(((ChoiceEventPart) eventPart).choices);
        String label = ((SelectableString)chosen.getObject()).value;
        if (!label.equals("END")) {
          queueEvent(label);
        }
      } else if (eventPart instanceof GotoEventPart) {
        if (prevType == TextEventPart.class) {
          GameMaster.enterToContinue();
        }
        queueEvent(((GotoEventPart) eventPart).nextEvent);
      }
      prevType = eventPart.getClass();
    }
    if (eventToRun.equals(e.title)) {
      GameMaster.enterToContinue();
      eventToRun = null;
    }
  }

  // jank! gross! jank! this should be automatic! temp until I feel like designing a cool way
  // to do this from the text file! jank!
  public static void checkEventTriggers(Player player) {
    if (!completedEvents.contains("INTRO")) queueEvent("INTRO");
    else if (player.character.inStructure("ARENA_TOWER") && player.character.currentRoom == CharacterManager.get("BEYN").currentRoom) {
      queueEvent("MEET_BEYN");
    } else if (player.character.currentStructure == null) {
      queueEvent("INTO_THE_WILDERNESS");
    }
  }
}
