package view;

import java.awt.*;
import java.util.List;

public interface View {

  void wait(int ms);

  void setTitle(String s);

  void print(String s);

  void showTypedCharacter(Character c);

  void showOptions(List<String> options);

  void clear();

  void promptAnyInput();

  /**
   * prompt the user to enter more than a single key-press.
   */
  void promptTextInput();

  void setDayLength(int ticksPerDay, int hoursPerDay);

  void setTime(int curTime);

  /**
   * True if the view has finished drawing or whatever.
   * @return
   */
  boolean isFinishedDrawing();

  /**
   * Instacomplete typed text etc.
   */
  void hurryUp();

}
