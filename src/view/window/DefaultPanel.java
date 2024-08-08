package view.window;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shared.Util;

public class DefaultPanel extends WolgonPanel {

  private final Label title, body, clock, continuePrompter, typePrompter;
  private final Label[] optionLabels;
  private boolean promptingInput = false, promptingTextInput = false;
  private int updatesSinceFlicker = 0;
  private final static String continuePromptText = "\\/";
  private static final int optionLabelCount = 8;
  private static final AudioManager audioManager = new AudioManager();
  private static final Set<String> keyTopics = new HashSet<>(); // some words to highlight if they show up in text.

  public DefaultPanel(String topicPath) {
    super();
    if (keyTopics.isEmpty()) Util.parseFileAndDoEachLine(topicPath, this::getTermToHighlightInText);
    for (String s : keyTopics) {
      System.out.println(s);
    }
    Label.setKeywordList(keyTopics);

    title = new Label("Title", Color.WHITE, 30f, AlignmentLocation.Left,
        AlignmentLocation.Top, "WHOLE", this);
    clock = new Label("Clock", Color.WHITE, 20f, AlignmentLocation.Right, AlignmentLocation.Bottom, "WHOLE", this);
    clock.setTypewriter(false);
    body = new Label("Body", Color.WHITE, 30f, "Title", this);
    optionLabels = new Label[optionLabelCount];
    initOptionLabels();
    continuePrompter = new Label("Continue Prompter", Color.LIGHT_GRAY, 20f, "Body", this);
    typePrompter = new Label("Type Prompter", Color.LIGHT_GRAY, 20f, "Body", this);
  }

  private void getTermToHighlightInText(String rawLine) {
    keyTopics.add(rawLine.trim());
  }


  @Override
  public void update() {
    if (!body.doneTyping()) {
      audioManager.playBlip();
    } else {
      audioManager.signalDoneTyping();
    }
    if (promptingInput) {
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
    if (promptingInput) disablePrompters();
    body.setText(body.getText() + s + "\n\n");
    update();
  }

  @Override
  public void showOptions(List<String> options) {
    if (options == null) {
      clearOptions();
    } else {
      disablePrompters();
      int i = 0;
      for (String s : options) {
        optionLabels[i].setText(s);
        optionLabels[i].setClickValue(i + 1 + "");
        i++;
      }
    }

  }

  @Override
  public void clear() {
    title.clear();
    body.clear();
    clearOptions();
    disablePrompters();
    update();
  }

  @Override
  public void promptAnyInput() {
    promptingInput = true;
  }

  public void promptTextInput() {
    typePrompter.setText("Type your response...");
    promptingTextInput = true;
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

  private void disablePrompters() {
    if (promptingInput) {
      audioManager.playUserEnter();
    }
    promptingInput = false;
    if (!continuePrompter.getText().isEmpty()) continuePrompter.setText("");

    if (promptingTextInput) {
      typePrompter.setText("");
    }
  }

  @Override
  public void setTime(int curTime) {
    super.setTime(curTime);
    clock.setText(String.format("%d:%s", (int) time.tickToHour(curTime), time.tickToMinute(curTime)));
  }
}
