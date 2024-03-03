package controller;

import java.util.List;

public interface Controller {
  int setOptions(List<String> options);

  /**
   * Get any input from the user to proceed.
   */
  void enterToContinue();
}
