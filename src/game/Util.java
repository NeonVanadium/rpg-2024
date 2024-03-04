package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

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
}
