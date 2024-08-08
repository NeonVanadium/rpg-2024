package controller;

import shared.Util;

import java.awt.event.*;
import java.util.List;

public class KeyboardMouseController extends AbstractController {

  private GraphicControllerKeyboardListener keyListener;

  protected static Character input;

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

  private boolean hasQueuedInput() {
    return input != null;
  }

  private Character getQueuedInput() {
    Character resp = input;
    input = null;
    return resp;
  }

  private Character waitForInput() {
    while (!hasQueuedInput()) {
      Util.sleep(100);
    }
    return getQueuedInput();
  }

  public KeyAdapter getKeyAdapter() {
    return keyListener;
  }

  public PipelineToController makePipeline() {
    return new PipelineToController() {
      @Override
      public void insertValue(Character s) {
        KeyboardMouseController.input = s;
      }
    };
  }

  private class GraphicControllerKeyboardListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      input = e.getKeyChar();
    }
  }

  /*private class GraphicControllerMouseListener extends MouseAdapter {
  }*/
}
