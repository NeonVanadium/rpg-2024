package view.window;

import java.awt.Color;

public class DefaultPanel extends WolgonPanel {

  public DefaultPanel() {
    new Label("TEST", "", Color.WHITE, 30f, AlignmentLocation.Left, AlignmentLocation.Top, "WHOLE", this);
  }

  @Override
  public void update() {
    repaint();
  }
}
