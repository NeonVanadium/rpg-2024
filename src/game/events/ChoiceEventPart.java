package game.events;

import game.ControlOrb;
import game.prompts.PromptOption;
import game.prompts.Selectable;
import game.prompts.SelectableString;
import java.util.LinkedList;
import java.util.List;

public class ChoiceEventPart implements EventPart{

  List<PromptOption> choices;

  public ChoiceEventPart(String rawLineText) {
    String[] unprocessedChoices = rawLineText.split(";");
    choices = new LinkedList<>();
    String[] promptAndResult;

    // start at 1 bc the zeroth is just the CHOICE label.
    for (int i = 1; i < unprocessedChoices.length; i++) {
      promptAndResult = unprocessedChoices[i].split(">", 2);
      Selectable result;
      if (promptAndResult[1].trim().contains(" ")) { // this means there's something more here than another event to go to--make a dedicated event part.
        result = EventPart.makePartBasedOnLine(promptAndResult[1].trim());
      } else {
        result = new SelectableString(promptAndResult[1].trim());
      }

      choices.add(new PromptOption(promptAndResult[0].trim(), result));
    }
  }

  @Override
  public void run(ControlOrb orb) {
    PromptOption chosen = orb.getChoiceFromOptions(choices);
    if (chosen.getObject() instanceof EventPart ep) {
      ep.run(orb);
    } else {
      String label = ((SelectableString)chosen.getObject()).value;
      // eventually might want these to have different behavior
      if (!label.equals("END") && !label.equals("CONT")) {
        EventManager.queueEventWithTitle(label);
      }
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
