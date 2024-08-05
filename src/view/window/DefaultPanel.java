package view.window;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import view.View;

public class DefaultPanel extends WolgonPanel implements View {

  private final Label title, continuePrompter;
  private final Label body;
  private final Label[] optionLabels;
  private boolean promptingInput = false;
  private int updatesSinceFlicker = 0;
  private final static String continuePromptText = "\\/";
  private static final int optionLabelCount = 8;
  private static final AudioManager audioManager = new AudioManager();

  public DefaultPanel() {
    title = new Label("Title", Color.WHITE, 30f, AlignmentLocation.Left,
        AlignmentLocation.Top, "WHOLE", this);
    body = new Label("Body", Color.WHITE, 30f, "Title", this);
    optionLabels = new Label[optionLabelCount];
    initOptionLabels();
    continuePrompter = new Label("Continue Prompter", Color.LIGHT_GRAY, 20f, "Body", this);
  }

  @Override
  public void update() {
    if (!body.doneTyping()) {
      audioManager.playBlip();
    } else {
      audioManager.signalDoneTyping();
    }
    if (/*body.doneTyping() &&*/ promptingInput) {
      updatesSinceFlicker++;
      int PROMPT_FLICKER_FREQUENCY = 15;
      if (updatesSinceFlicker == PROMPT_FLICKER_FREQUENCY) {
        if (continuePrompter.getText().isEmpty()) {
          continuePrompter.setText(continuePromptText);
        } else {
          continuePrompter.clear();
        }
        updatesSinceFlicker = 0;
      }
    }
    repaint();
  }

  @Override
  public void wait(int ms) {}

  @Override
  public void setTitle(String s) {
    title.setText(s);
    update();
  }

  @Override
  public void print(String s) {
    if (promptingInput) disableContinuePrompter();
    body.setText(body.getText() + s + "\n\n");
    update();
  }

  @Override
  public void showOptions(List<String> options) {
    if (options == null) {
      clearOptions();
    } else {
      disableContinuePrompter();
      int i = 0;
      for (String s : options) {
        optionLabels[i].setText(s);
        i++;
      }
    }

  }

  @Override
  public void clear() {
    title.clear();
    body.clear();
    clearOptions();
    disableContinuePrompter();
    update();
  }

  @Override
  public void promptAnyInput() {
    promptingInput = true;
  }

  @Override
  public void setColor(Color c) {
    //body.setTextColor(c);
  }

  @Override
  public boolean isFinishedDrawing() {
    return body.doneTyping();
  }

  @Override
  public void hurryUp() {
    body.instacomplete();
  }

  private void initOptionLabels() {
    String prevLabel = "Body";
    String thisLabel;
    for (int i = 0; i < optionLabelCount; i++) {
      thisLabel = "Opt" + i;
      optionLabels[i] = new Label(thisLabel, Color.WHITE, 30f, prevLabel, this);
      prevLabel = thisLabel;
    }
  }

  private void clearOptions() {
    for (int i = 0; i < optionLabelCount; i++) {
      optionLabels[i].clear();
    }
  }

  private void disableContinuePrompter() {
    if (promptingInput) {
      audioManager.playUserEnter();
    }
    promptingInput = false;
    if (!continuePrompter.getText().isEmpty()) continuePrompter.setText("");
  }
}
