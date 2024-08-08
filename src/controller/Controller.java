package controller;

import java.util.List;

public interface Controller {
  /**
   * Sets the possible choices the user has.
   */
  void setOptions(List<String> options);

  /**
   * Gets the user's choice from the currently set options. setOptions() must be called first.
   */
  int getChoice();

  /**
   * Get any input from the user to proceed.
   */
  void getAnyInput();

  /**
   * Get text input.
   */
  String getTextInput();
}
