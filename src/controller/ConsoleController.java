package controller;

import java.util.Scanner;

public class ConsoleController implements Controller {

  private Scanner input = new Scanner(System.in);

  private String[] options;

  public int setOptions(String[] options) {
    this.options = options;
    if (options != null && options.length != 0) {
      return pickOption();
    }
    return -1;
  }

  public int pickOption() {
    //System.out.println("Option picker started.");
    int selected = -1;
    while (!(selected > 0 && selected < options.length + 1)) {
      selected = input.nextInt();
      //System.out.println("Selected is " + selected);
    }
    System.out.println("Picked " + options[selected - 1]);
    return selected - 1;
  }

}
