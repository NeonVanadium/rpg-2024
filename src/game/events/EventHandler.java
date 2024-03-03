package game.events;

import controller.Controller;
import game.GameMaster;
import game.Player;
import game.prompts.PromptOption;
import game.prompts.SelectableString;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import view.View;

/**
 * A class to handle event-related tasks, including parsing them from the file.
 */
public class EventHandler {

  protected static final String EVENT_START_SYMBOL = ">> ";
  protected static final String SPECIAL_PART_SYMBOL = "> ";
  private static Map<String, Event> events;
  private static Collection<String> completedEvents;

  private static String eventToRun;

  /**
   * Load events from events.txt.
   */
  public static void loadEvents() {
    events = new HashMap<>();
    completedEvents = new HashSet<>();
    Event curEvent = null;
    try {
      BufferedReader br = new BufferedReader(new FileReader("resources\\events.txt"));

      String line;
      while((line = br.readLine()) != null) {// Reads lines of text until there are no more
        if (!line.isBlank()) {
          if (line.startsWith(EVENT_START_SYMBOL)) {
            if (curEvent != null) {
              events.put(curEvent.title, curEvent);
            }
            curEvent = new Event(line.substring(EVENT_START_SYMBOL.length()));
          } else if (curEvent != null) {
            curEvent.addPart(line);
          }
        }
      }
      br.close();
      if (curEvent != null) {
        events.put(curEvent.title, curEvent);
      }
    } catch (IOException e) {
      e.printStackTrace();
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
    for (EventPart eventPart : e.getEventParts()) {
      if (eventPart instanceof TextEventPart) {
        view.clear();
        view.print(((TextEventPart) eventPart).text);
        GameMaster.enterToContinue();
      } else if (eventPart instanceof ChoiceEventPart) {
        // TODO : I really don't want this method to be public, find a way to get this
        // functionality without exposing anything on the game master
        PromptOption chosen = GameMaster.getChoiceFromOptions(((ChoiceEventPart) eventPart).choices);
        String label = ((SelectableString)chosen.getObject()).value;
        if (!label.equals("END")) {
          queueEvent(label);
        }
      }
    }
    if (eventToRun.equals(e.title)) {
      eventToRun = null;
    }

  }

  // jank! gross! jank! this should be automatic! temp until I feel like designing a cool way
  // to do this from the text file! jank!
  public static void checkEventTriggers(Player player) {
    if (!completedEvents.contains("INTRO")) queueEvent("INTRO");
    else if (player.character.inStructure("ARENA_TOWER") && player.character.currentRoom != 1) {
      queueEvent("MEET_BEYN");
    }
  }
}
