package view.window;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import view.View;

public class GraphicsView implements View {

  // the frame which holds a WolgonPanel object.
  private final JFrame frame = new JFrame();
  private final int TICK_FREQUENCY = 15; //update is called automatically every this many ms
  protected final Rectangle bounds = new Rectangle(0, 0, 1000, 800);

  private WolgonPanel panel;

  public GraphicsView() {
    setupFrame();
    Timer t = new Timer(TICK_FREQUENCY, (e) -> panel.update());
    t.start();
  }

  private void setupFrame() {
    frame.setBounds(bounds);
    frame.setBackground(Color.BLACK);
    frame.setTitle("Untitled RPG 2024");
    setPanel(new DefaultPanel());
    frame.setVisible(true);
  }

  public void setPanel(WolgonPanel p) {
    frame.getContentPane().removeAll();
    panel = p;
    frame.getContentPane().add(panel);
    frame.revalidate();
  }

  @Override
  public void wait(int ms) {

  }

  @Override
  public void setTitle(String s) {
    panel.setTitle(s);
  }

  @Override
  public void print(String s) {
    panel.print(s);
  }

  @Override
  public void clear() {
    panel.clear();
  }

  @Override
  public void promptAnyInput() {
    panel.promptAnyInput();
  }
}
