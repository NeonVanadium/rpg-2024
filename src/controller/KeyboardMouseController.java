package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class KeyboardMouseController implements Controller, KeyListener {

  private List<String> options;
  private Object input = null;

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println(e.getKeyChar());
    input = e.getKeyChar();
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public int setOptions(List<String> options) {
    return 0;
  }

  @Override
  public void getAnyInput() {

  }
}
