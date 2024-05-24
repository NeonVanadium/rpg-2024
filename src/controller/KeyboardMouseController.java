package controller;

import game.Util;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class KeyboardMouseController extends AbstractController {

  private GraphicControllerKeyboardListener keyListener;

  public KeyboardMouseController() {
    keyListener = new GraphicControllerKeyboardListener();
  }

  @Override
  public void setOptions(List<String> options) {
    this.options = options;
  }

  @Override
  public int getChoice() {
    if (options != null && !options.isEmpty()) {
      return pickOption();
    }
    if (options == null) {
      waitForInput();
    }
    return INVALID_SELECTION;
  }

  private int pickOption() {
    Character resp = null;
    while (resp == null) resp = waitForInput();
    int selected = parseInt(resp.toString());
    return isValidSelection(selected) ? selected : INVALID_SELECTION;
  }

  public void getAnyInput() {
    waitForInput();
  }

  private Character waitForInput() {
    while (!keyListener.hasQueuedInput()) {
      Util.sleep(100);
    }
    return keyListener.getQueuedInput();
  }

  public KeyAdapter getKeyAdapter() {
    return keyListener;
  }

  private class GraphicControllerKeyboardListener extends KeyAdapter {
    Character input;

    @Override
    public void keyPressed(KeyEvent e) {
      input = e.getKeyChar();
    }

    public boolean hasQueuedInput() {
      return input != null;
    }

    public Character getQueuedInput() {
      Character resp = input;
      input = null;
      return resp;
    }
  }
}
