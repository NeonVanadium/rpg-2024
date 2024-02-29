package controller;

import java.util.List;
import java.util.Scanner;
import view.View;

public class ConsoleController implements Controller {

  private View view;
  private Scanner input = new Scanner(System.in);

  private List<String> options;

  public ConsoleController(View view) {
    this.view = view;
  }

  public int setOptions(List<String> options) {
    this.options = options;
    if (options != null && options.size() != 0) {
      return pickOption();
    }
    if (options == null) {
      input.nextLine();
    }
    return -1;
  }

  public int pickOption() {
    int selected = -1;
    while (!isValidSelection(selected)) {
      String resp = input.nextLine();
      selected = parseInt(resp);
      if (selected == -1) { // non-integer response. Try string matching.
        for (int i = 0; i < options.size(); i++) {
          if (options.get(i).toLowerCase().startsWith(resp.toLowerCase())) {
            selected = i;
          }
        }
      }
      if (!isValidSelection(selected)) {
        view.print("Not a valid input.");
      }
    }
    view.print("Picked " + options.get(selected));
    return selected;
  }

  private boolean isValidSelection(int s) {
    return s >= 0 && s < options.size();
  }

  /**
   * Parse int from a response string. Doesn't support negatives. If parsing fails,
   * returns -1.
   */
  private int parseInt(String s) {
    if (Character.isDigit(s.charAt(0))) {
      try {
        return Integer.parseInt(s) - 1;
      } catch (Exception e) {}
    }
    return -1;
  }

}
