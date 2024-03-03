package game.events;

import game.prompts.PromptOption;
import game.prompts.SelectableString;
import java.util.LinkedList;
import java.util.List;

public class ChoiceEventPart extends EventPart{

  List<PromptOption> choices;

  public ChoiceEventPart(String rawLineText) {
    if (!rawLineText.startsWith(EventHandler.SPECIAL_PART_SYMBOL)) {
      throw new IllegalArgumentException("raw line text passed to Choice event part doesn't start with the special part symbol.");
    }
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
