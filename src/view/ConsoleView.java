package view;

public class ConsoleView implements View {

  private static final int CHAR_PRINT_DELAY = 100;

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
    /*for (int i = 0; i < msg.length(); i++) {
      System.out.print(msg.charAt(i));
      wait(CHAR_PRINT_DELAY);
    }*/
    System.out.println(msg);
  }

  @Override
  public void clear() {
    // nothing.
  }


}
