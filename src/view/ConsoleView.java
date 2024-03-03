package view;

public class ConsoleView implements View {

  @Override
  public void wait(int ms) {
    try {
      Thread.sleep(ms);
    } catch (Exception e) {
      System.out.println("how???");
    }
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
  public void clear() {
    // nothing.
  }

  @Override
  public void promptAnyInput() {
    print("<ENTER to continue>");
  }


}
