package game.events;

import game.ControlOrb;
import game.prompts.PromptOption;
import game.prompts.SelectableString;
import java.util.LinkedList;
import java.util.List;

public class ChoiceEventPart implements EventPart{

  List<PromptOption> choices;

  public ChoiceEventPart(String rawLineText) {
    String[] unprocessedChoices = rawLineText.split(";");
    choices = new LinkedList<>();
    String[] promptToEvent;
    // start at 1 bc the zeroth is just the CHOICE label.
    for (int i = 1; i < unprocessedChoices.length; i++) {
      promptToEvent = unprocessedChoices[i].split(">");
      choices.add(new PromptOption(promptToEvent[0].trim(), new SelectableString(promptToEvent[1].trim())));
    }
  }

  @Override
  public void run(ControlOrb orb) {
    PromptOption chosen = orb.getChoiceFromOptions(choices);
    String label = ((SelectableString)chosen.getObject()).value;
    // eventually might want these to have different behavior
    if (!label.equals("END") && !label.equals("CONT")) {
      EventManager.queueEventWithTitle(label);
    }
  }

  @Override
  public boolean pauseAfter() {
    return false;
  }

  @Override
  public boolean pauseBefore() {
    return false;
  }
}
