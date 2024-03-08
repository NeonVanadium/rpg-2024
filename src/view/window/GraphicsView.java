package view.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import view.View;

public class GraphicsView implements View {

  // the frame which holds a WolgonPanel object.
  private final JFrame frame = new JFrame();
  private final int TICK_FREQUENCY = 10; //update is called automatically every this many ms
  protected final Rectangle bounds = new Rectangle(0, 0, 1000, 800);
  private final Dimension START_SIZE = new Dimension(1000, 800);

  private WolgonPanel panel;

  public GraphicsView() {
    setupFrame();
    Timer t = new Timer(TICK_FREQUENCY, (e) -> panel.update());
    t.start();
  }

  public void addKeyListener(KeyListener l) {
    frame.addKeyListener(l);
  }

  private void setupFrame() {
    frame.setBounds(bounds);
    frame.setSize(START_SIZE);
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

  @Override
  public boolean isFinishedDrawing() {
    return panel.isFinishedDrawing();
  }

  @Override
  public void hurryUp() {
    panel.hurryUp();
  }
}
