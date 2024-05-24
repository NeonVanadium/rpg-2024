package controller;

import java.util.List;
import java.util.Scanner;

public class ConsoleController extends AbstractController {

  private Scanner input = new Scanner(System.in);

  @Override
  public void setOptions(List<String> options) {
    this.options = options;
  }

  @Override
  public int getChoice() {
    if (options != null && options.size() != 0) {
      return pickOption();
    }
    if (options == null) {
      input.nextLine();
    }
    return -1;
  }

  public void getAnyInput() {
    getChoice();
  }

  public int pickOption() {
    int selected = -1;
    while (!isValidSelection(selected)) {
      String resp = input.nextLine();
      if (resp.isEmpty())  {
        return EMPTY_SELECTION;
      }
      selected = parseInt(resp);
      if (selected == -1) { // non-integer response. Try string matching.
        for (int i = 0; i < options.size(); i++) {
          if (options.get(i).toLowerCase().startsWith(resp.toLowerCase())) {
            selected = i;
          }
        }
      }
      if (!isValidSelection(selected)) {
        return INVALID_SELECTION;
      }
    }
    return selected;
  }





}
