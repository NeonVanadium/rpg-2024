package controller;

import java.util.Scanner;
import view.View;

public class ConsoleController implements Controller {

  private View view;
  private Scanner input = new Scanner(System.in);

  private String[] options;

  public ConsoleController(View view) {
    this.view = view;
  }

  public int setOptions(String[] options) {
    this.options = options;
    if (options != null && options.length != 0) {
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
    view.print("Picked " + options[selected - 1]);
    return selected - 1;
  }

  private boolean isValidSelection(int s) {
    return s > 0 && s < options.length + 1;
  }

}
