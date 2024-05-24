package view;

import java.util.List;

public class ConsoleView implements View {

  @Override
  public void wait(int ms) {

  }

  @Override
  public void setTitle(String msg) {
    print("<<<" + msg + ">>>");
  }

  @Override
  public void print(String msg) {
    System.out.println(msg);
  }

  @Override
  public void showOptions(List<String> options) {
    for (String s : options) {
      print(s);
    }
  }

  @Override
  public void clear() {
    // nothing.
  }

  @Override
  public void promptAnyInput() {
    print("<ENTER to continue>");
  }

  @Override
  public boolean isFinishedDrawing() {
    return true;
  }

  @Override
  public void hurryUp() {

  }


}
