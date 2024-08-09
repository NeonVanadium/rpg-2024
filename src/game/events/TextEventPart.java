package game.events;

import game.ControlOrb;
import game.TagAndTopicManager;
import shared.Util;

public record TextEventPart(String text) implements EventPart {

  @Override
  public void run(ControlOrb orb) {
    orb.clear();
    orb.print(handleReplaceCharacters());
  }

  /**
   * Exists to allow [SKILL passed] tags to be added to text messages that depend on them.
   */
  public void appendRun(String toAppend, ControlOrb orb) {
    orb.clear();
    orb.print(toAppend + handleReplaceCharacters());
  }

  private String handleReplaceCharacters() {
    if (!text.contains(Util.TEXT_INSERT_SYMBOL)) return text;
    StringBuilder builder = new StringBuilder();
    for (String section : text.split(Util.TEXT_INSERT_SYMBOL)) {
      if (section.isEmpty()) continue;
      if (builder.isEmpty() && !text.startsWith(Util.TEXT_INSERT_SYMBOL)) { // first section, only one that could not have the symbol.
        builder.append(section);
      } else {
        String replace = section.substring(0, indexOfFirstNonLetterWithNoLetterAfterIt(section));
        builder.append(section.replace(replace, TagAndTopicManager.lookup(replace)));
      }
    }
    return builder.toString();
  }

  private int indexOfFirstNonLetterWithNoLetterAfterIt(String in) {
    int i = 0;
    if (in != null && !in.isEmpty())
      while (Character.isAlphabetic(in.charAt(i))
              || (i < in.length() - 1 && Character.isAlphabetic(in.charAt(i + 1)) )) {
      i++;
    }
    return i;
  }

  @Override
  public boolean pauseAfter() {
    return true;
  }

  @Override
  public boolean pauseBefore() {
    return true;
  }
}
