package view.window;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import view.View;

public class WindowView implements View {

  // the frame which holds an AWolgonPanel object.
  private static final JFrame FRAME = new JFrame();
  private static WolgonPanel panel;
  public static final Rectangle BOUNDS = new Rectangle(0, 0, 1000, 800);

  public WindowView() {
    setupFrame();
  }

  private void setupFrame() {
    FRAME.setBounds(BOUNDS);
    FRAME.setBackground(Color.BLACK);
    FRAME.setTitle("Untitled RPG 2024");
    panel = new DefaultPanel();
    setPanel(panel);
    FRAME.setVisible(true);
  }

  public void setPanel(WolgonPanel p) {
    FRAME.getContentPane().removeAll();
    FRAME.getContentPane().add(p);
    FRAME.revalidate();
  }

  @Override
  public void wait(int ms) {

  }

  @Override
  public void setTitle(String s) {

  }

  @Override
  public void print(String s) {
    panel.getLabel("TEST").setText(panel.getLabel("TEST").getText() + s + "\n");
    panel.update();
  }

  @Override
  public void clear() {
    panel.getLabel("TEST").setText("");
    panel.update();
  }
}
