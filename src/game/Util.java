package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Util {

  /**
   * Used in reading from the formatted text files for events, characters etc.
   */
  public static final String ENTRY_START_SYMBOL = ">> ";
  public static final String SPECIAL_PART_SYMBOL = "> ";
  public static final String COMPONENT_DELINIATOR = ";";

  /**
   * Reads each line from a file, and calls the provided consumer on each line.
   */
  public static void parseFileAndDoEachLine(String filepath, Consumer<String> action) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(filepath));
      String line;
      while((line = br.readLine()) != null) {// Reads lines of text until there are no more
        action.accept(line);
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Given a list of objects and a function to get strings from them, adds commas and ands to make
   * a nice and readable list of them.
   */
  public static <T extends Object> String commasAndAnds(List<T> list, Function<T, String> toStr) {
    if (list.isEmpty()) return null;
    if (list.size() == 1) return toStr.apply(list.get(0));
    if (list.size() == 2) return toStr.apply(list.get(0)) + " and " + toStr.apply(list.get(1));

    StringBuilder builder = new StringBuilder();
    int i = 0;
    Iterator<T> iterator = list.listIterator();
    while (iterator.hasNext()) {
      i++;
      if (i != 1) {
        builder.append(", ");
      }
      if (i == list.size()) {
        builder.append("and ");
      }
      builder.append(toStr.apply(iterator.next()));
    }
    return builder.toString();
  }

  public static String centerText(String toCenter, String relativeTo) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < relativeTo.length(); i++) {
      result.append(' ');
      if (i == relativeTo.length() / 2) {
        result.append(toCenter);
      }
    }
    return result.toString();
  }
}
