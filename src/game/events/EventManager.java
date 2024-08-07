package game.events;

import game.ControlOrb;
import game.GameMaster;
import shared.Util;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

/**
 * A class to handle event-related tasks, including parsing them from the file.
 */
public class EventManager {
  private static Map<String, Event> events;
  private static Collection<String> topLevelEvents;
  private static Collection<String> completedEvents;

  private static Event eventBeingBuilt; // only used in loadevents/processline.

  private static Stack<Event> eventStack = new Stack<>(); // only used when the insert event is called
  private static int insertDepth = 0; // how many inserts deep are we running currently?
  private static final String eventFilesPath = GameMaster.getResourceFolder() + "events\\";

  /**
   * Load events from all files in resources/events.
   */
  public static void loadEvents() {
    events = new HashMap<>();
    completedEvents = new HashSet<>();
    topLevelEvents = new HashSet<>();

    eventBeingBuilt = null;
    for (String fileName : new File(eventFilesPath).list()) {
      Util.parseFileAndDoEachLine(eventFilesPath + fileName, EventManager::processLine);
      if (eventBeingBuilt != null) {
        addFinishedEvent();
        eventBeingBuilt = null;
      }
    }
  }

  private static void processLine(String line) {
    if (!line.isBlank()) {
      if (line.startsWith(Util.ENTRY_START_SYMBOL) || line.startsWith(Util.SPECIAL_PART_SYMBOL)) {
        if (eventBeingBuilt != null) { // this concludes a previous event
          addFinishedEvent();
        }
        eventBeingBuilt = new Event(line);
      } else if (eventBeingBuilt != null) {
        eventBeingBuilt.addPart(line);
      }
    }
  }

  private static void addFinishedEvent() {
    events.put(eventBeingBuilt.title, eventBeingBuilt);
    if (eventBeingBuilt.hasConditions()) { // if a top-level event (with trigger conditions)
      topLevelEvents.add(eventBeingBuilt.title);
    }
  }

  private static void queueEventIfNotRunBefore(String title) {
    if (!completedEvents.contains(title)) {
      queueEventWithTitle(title);
    }
  }

  public static void queueEventWithTitle(String title) {
    eventStack.push(events.get(title));
    System.out.println("Queued event " + title);
  }

  public static boolean hasQueuedEvent() {
    return !eventStack.isEmpty();
  }

  public static boolean hasEventWithTitle(String title) {
    return events.get(title) != null;
  }

  public static void runQueuedEvent(ControlOrb orb) {
    Event e = eventStack.peek();
    runEvent(e, orb);
  }

  private static void runEvent(Event e, ControlOrb orb) {
    EventPart prevPart = null;

    for (EventPart eventPart : e.getEventParts()) {
      maybeEnterToContinue(eventPart, prevPart, orb);
      eventPart.run(orb);
      if (eventStack.size() - 1 > insertDepth) { // enables event inserts
        insertDepth++;
        runEvent(eventStack.peek(), orb);
        insertDepth--;
      } else if (eventStack.peek() != e){
        prevPart = null;
        break; //enables Gotos
      }
      prevPart = eventPart;
    }
    if (eventStack.peek() == e) {
      if (prevPart.pauseAfter()) orb.enterToContinue();
      completedEvents.add(e.title);
      eventStack.pop();
    }
  }

  private static void maybeEnterToContinue(EventPart curPart, EventPart prevPart, ControlOrb orb) {
    if ((prevPart != null && prevPart.pauseAfter()) && curPart.pauseBefore()) {
      orb.enterToContinue();
    }
  }

  public static void checkEventTriggers() {
    for (String title : topLevelEvents) {
      if (!completedEvents.contains(title) && events.get(title).conditionsMet()) {
        queueEventWithTitle(title);
      }
    }
  }

  public static boolean isEventCompleted(String title) {
    return completedEvents.contains(title);
  }

  protected static void pushToEventInsertStack(String eventName) {
    eventStack.push(events.get(eventName));
    System.out.println("Pushed event " + eventName + " to insert stack.");
  }
}
