package controller;

import java.util.List;

public abstract class AbstractController implements Controller {
  public final int EMPTY_SELECTION = -5;
  public final int INVALID_SELECTION = -1;

  protected List<String> options;

  protected boolean isValidSelection(int s) {
    return s >= 0 && s < options.size();
  }

  /**
   * Parse int from a response string. Doesn't support negatives. If parsing fails,
   * returns -1.
   */
  protected int parseInt(String s) {
    if (!s.isEmpty() && Character.isDigit(s.charAt(0))) {
      try {
        return Integer.parseInt(s) - 1;
      } catch (Exception e) {}
    }
    return -1;
  }
}
