package controller;

import shared.Util;

import java.awt.event.*;
import java.util.List;

public class KeyboardMouseController extends AbstractController {

  private final GraphicControllerKeyboardListener keyListener;
  private static final int CHECK_INPUT_DELAY = 100; // in ms

  protected static Character input;
  protected static String enteredText = null;
  protected static StringBuilder textInputBuilder;

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

  public Character getAnyInput() {
    return waitForInput();
  }

  @Override
  public String getTextInput() {
    textInputBuilder = new StringBuilder();
    while (enteredText == null) {
      Util.sleep(CHECK_INPUT_DELAY);
    }
    return enteredText;
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
      Util.sleep(CHECK_INPUT_DELAY);
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
}
