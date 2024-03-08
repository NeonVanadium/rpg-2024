package game.events;

import game.ControlOrb;
import game.Player;
import game.Util;
import game.characters.CharacterManager;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A class to handle event-related tasks, including parsing them from the file.
 */
public class EventManager {
  private static Map<String, Event> events;
  private static Collection<String> completedEvents;

  private static Event eventBeingBuilt; // only used in loadevents/processline.
  private static String eventPrefix;
  // to reduce name bloat in complicated event files,
  // this prefix will be added to all subevents so that they can be, in the file,
  // called "2" or "DENIED" instead of "MEET_GUY_2" and "MEET_GUY_DENIED" etc.

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
      Util.parseFileAndDoEachLine(eventFilesPath + fileName, EventManager::processLine);
      if (eventBeingBuilt != null) {
        events.put(eventBeingBuilt.title, eventBeingBuilt);
        eventBeingBuilt = null;
      }
    }
  }

  private static void processLine(String line) {
    if (!line.isBlank()) {
      if (line.startsWith(Util.ENTRY_START_SYMBOL) || line.startsWith(Util.SPECIAL_PART_SYMBOL)) {
        if (eventBeingBuilt != null) {
          events.put(eventBeingBuilt.title, eventBeingBuilt);
        }

        // convenience prefix stuff
        String eventTitle = line.substring(line.indexOf(" ")).trim();
        // TODO : This will cause issues when linking to events that don't take the prefix. Need a workaround. This is currently BROKEN.
        // TODO : Possibly add this convenience just for things in specific files (i.e. not small_events etc) rather than by >> vs >
        if (line.startsWith(Util.ENTRY_START_SYMBOL)) {
          eventPrefix = eventTitle + '_';
        } else if (line.startsWith(Util.SPECIAL_PART_SYMBOL)) {
          eventTitle = getEventPrefix() + eventTitle;
        }

        eventBeingBuilt = new Event(eventTitle);
      } else if (eventBeingBuilt != null) {
        eventBeingBuilt.addPart(line);
      }
    }
  }

  public static String getEventPrefix() {
    return eventPrefix;
  }

  private static void queueEventIfNotRunBefore(String title) {
    if (!completedEvents.contains(title)) {
      queueEventWithTitle(title);
    }
  }

  public static void queueEventWithTitle(String title) {
    eventToRun = title;
    System.out.println("Queued event " + title);
  }

  public static boolean hasQueuedEvent() {
    return eventToRun != null;
  }

  public static void runQueuedEvent(ControlOrb orb) {
    Event e = events.get(eventToRun);
    completedEvents.add(eventToRun);

    Class prevType = null;

    for (EventPart eventPart : e.getEventParts()) {
      maybeEnterToContinue(eventPart, prevType, orb);
      eventPart.run(orb);
      if (eventToRun != null && !eventToRun.equals(e.title)) break; //enables Gotos
      else if (eventPart instanceof IfEventPart && ((IfEventPart) eventPart).checkCondition()) {
        prevType = ((IfEventPart) eventPart).ifYes.getClass();
      } else {
        prevType = eventPart.getClass();
      }
    }
    if (eventToRun.equals(e.title)) {
      if (prevType != ChoiceEventPart.class) orb.enterToContinue();
      eventToRun = null;
    }
  }

  private static void maybeEnterToContinue(EventPart curPart, Class prevType, ControlOrb orb) {
    if ((curPart instanceof TextEventPart || curPart instanceof SayEventPart ||
        curPart instanceof GotoEventPart ||
        (curPart instanceof IfEventPart && ((IfEventPart) curPart).checkCondition())) &&
        (prevType == TextEventPart.class || prevType == SayEventPart.class)) {
      orb.enterToContinue();
    }
  }

  // jank! gross! jank! this should be automatic! temp until I feel like designing a cool way
  // to do this from the text file! jank!
  public static void checkEventTriggers() {
    //if (!completedEvents.contains("INTRO")) queueEventIfNotRunBefore("INTRO");
    if (Player.character.inStructure("ARENA_TOWER")
        && Player.character.currentRoom == CharacterManager.get("BEYN").currentRoom) {
      queueEventIfNotRunBefore("MEET_BEYN");
    } else if (Player.character.currentStructure == null) {
      queueEventIfNotRunBefore("INTO_THE_WILDERNESS");
    }
  }

  public static boolean isEventCompleted(String title) {
    return completedEvents.contains(title);
  }
}
