package view;

public interface View {

  void wait(int ms);

  void setTitle(String s);

  void print(String s);

  void clear();

  void promptAnyInput();

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
