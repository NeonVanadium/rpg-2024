package game.events;

import game.ControlOrb;
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
import java.util.Set;

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
      if (eventPart instanceof TextEventPart) {
        maybeEnterToContinue(prevType, orb);
        orb.clear();
        orb.print(((TextEventPart) eventPart).text);
      }
      else if (eventPart instanceof SayEventPart) {
        maybeEnterToContinue(prevType, orb);
        orb.clear();
        orb.print(((SayEventPart) eventPart).format());
      }
      else if (eventPart instanceof ChoiceEventPart) {
        PromptOption chosen = orb.getChoiceFromOptions(((ChoiceEventPart) eventPart).choices);
        String label = ((SelectableString)chosen.getObject()).value;
        if (!label.equals("END")) {
          queueEventIfNotRunBefore(label);
        }
      }
      else if (eventPart instanceof GotoEventPart) {
        maybeEnterToContinue(prevType, orb);
        queueEventIfNotRunBefore(((GotoEventPart) eventPart).nextEvent);
      }
      else if (eventPart instanceof JoinPartyEventPart) {
        Player.addToParty(((JoinPartyEventPart) eventPart).characterLabel, orb);
      }
      else if (eventPart instanceof SetNameEventPart) {
        SetNameEventPart setNameEventPart = (SetNameEventPart) eventPart;
        CharacterManager.setKnownName(setNameEventPart.characterLabel, setNameEventPart.newName);
      }
      else {
        System.out.println(eventPart.getClass() + " is missing an implementation in the event handler.");
      }
      prevType = eventPart.getClass();
    }
    if (eventToRun.equals(e.title)) {
      orb.enterToContinue();
      eventToRun = null;
    }
  }

  private static void maybeEnterToContinue(Class prevType, ControlOrb orb) {
    if (prevType == TextEventPart.class || prevType == SayEventPart.class) {
      orb.enterToContinue();
    }
  }

  // jank! gross! jank! this should be automatic! temp until I feel like designing a cool way
  // to do this from the text file! jank!
  public static void checkEventTriggers(Player player) {
    if (!completedEvents.contains("INTRO")) queueEventIfNotRunBefore("INTRO");
    else if (player.character.inStructure("ARENA_TOWER") && player.character.currentRoom == CharacterManager.get("BEYN").currentRoom) {
      queueEventIfNotRunBefore("MEET_BEYN");
    } else if (player.character.currentStructure == null) {
      queueEventIfNotRunBefore("INTO_THE_WILDERNESS");
    }
  }
}
