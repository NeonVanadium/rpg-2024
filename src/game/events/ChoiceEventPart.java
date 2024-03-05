package game.events;

import game.Util;
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
}
