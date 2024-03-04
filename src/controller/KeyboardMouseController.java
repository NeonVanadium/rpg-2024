package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import view.View;

public class KeyboardMouseController implements Controller, KeyListener {

  private View view;
  private List<String> options;
  private Object input = null;

  public KeyboardMouseController(View view) {
    this.view = view;
  }

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
  public void enterToContinue() {

  }
}
