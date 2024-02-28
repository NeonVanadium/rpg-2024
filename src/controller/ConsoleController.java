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
    return -1;
  }

  public int pickOption() {
    int selected = -1;
    while (!isValidSelection(selected)) {
      selected = input.nextInt();
      if (!isValidSelection(selected)) {
        view.print("Not a valid input.");
      }
    }
    view.print("Picked " + options.get(selected - 1));
    return selected - 1;
  }

  private boolean isValidSelection(int s) {
    return s > 0 && s < options.size() + 1;
  }

}
