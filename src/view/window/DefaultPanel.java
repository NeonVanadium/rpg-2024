package view.window;

import java.awt.Color;
import view.View;

public class DefaultPanel extends WolgonPanel implements View {

  private final Label title, continuePrompter;
  private final TypewriterLabel body;
  private boolean promptingInput = false;
  private int updatesSinceFlicker = 0;
  private final static String continuePromptText = "\\/";

  public DefaultPanel() {
    title = new Label("Title", "", Color.WHITE, 60f, AlignmentLocation.Left,
        AlignmentLocation.Top, "WHOLE", this);
    body = new TypewriterLabel("Body", "", Color.WHITE, 30f,
        "Title", this);
    continuePrompter = new Label("Continue Prompter", "", Color.LIGHT_GRAY, 20f, "Body", this);
  }

  @Override
  public void update() {
    if (body.doneTyping() && promptingInput) {
      updatesSinceFlicker++;
      int PROMPT_FLICKER_FREQUENCY = 15;
      if (updatesSinceFlicker == PROMPT_FLICKER_FREQUENCY) {
        if (continuePrompter.getText().isEmpty()) {
          continuePrompter.setText(continuePromptText);
        } else {
          continuePrompter.setText("");
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
    body.setText(body.getFullText() + s + "\n\n");
    update();
  }

  @Override
  public void clear() {
    title.setText("");
    body.setText("");
    update();
  }

  @Override
  public void promptAnyInput() {
    // TODO  replace with little animated blip or something.
    promptingInput = true;
  }

  private void disableContinuePrompter() {
    promptingInput = false;
    if (!continuePrompter.getText().isEmpty()) continuePrompter.setText("");
  }
}
