package view;

import java.awt.*;
import java.util.List;

public interface View {

  void wait(int ms);

  void setTitle(String s);

  void print(String s);

  void showOptions(List<String> options);

  void clear();

  void promptAnyInput();

  /**
   * Set the text color.
   */
  void setColor(Color c);

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
