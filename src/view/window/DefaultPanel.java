package view.window;

import java.awt.Color;
import view.View;

public class DefaultPanel extends WolgonPanel implements View {

  private Label title, body;

  public DefaultPanel() {
    title = new Label("Title", "", Color.WHITE, 60f, AlignmentLocation.Left,
        AlignmentLocation.Top, "WHOLE", this);
    body = new Label("Body", "", Color.WHITE, 30f,
        "Title", this);
  }

  @Override
  public void update() {
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
    body.setText(body.getText() + s + "\n");
    update();
  }

  @Override
  public void clear() {
    title.setText("");
    body.setText("");
    update();
  }
}
