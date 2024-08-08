package view.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.Timer;

import controller.PipelineToController;
import view.View;

public class GraphicsManager {

  // the frame which holds a WolgonPanel object.
  private final JFrame frame = new JFrame();
  private final int TICK_FREQUENCY = 20; //update is called automatically every this many ms
  private final Dimension START_SIZE = new Dimension(1000, 800);

  protected final Rectangle bounds = new Rectangle(0, 0, 1000, 800);
  protected final String resourceFolder;
  private WolgonPanel panel;

  public GraphicsManager(String resourcePath) {
    resourceFolder = resourcePath;
    setupFrame();
    Timer t = new Timer(TICK_FREQUENCY, (e) -> panel.update());
    t.start();
  }

  private void setupFrame() {
    frame.setBounds(bounds);
    frame.setSize(START_SIZE);
    frame.setBackground(Color.BLACK);
    frame.setTitle("Untitled RPG 2024");
    setPanel(new DefaultPanel(resourceFolder + "/topics.txt"));
    frame.setFocusable(true);
    frame.setFocusTraversalKeysEnabled(false);
    frame.setVisible(true);
  }

  public void addKeyListener(KeyListener k) {
    this.frame.addKeyListener(k);
  }

  /*public void addMouseListener(MouseListener m) {
    panel.addMouseListener(m);
  }*/

  public void addControllerPipeline(PipelineToController pipeline) {
    this.panel.setPipelineToController(pipeline);
  }

  private void setPanel(WolgonPanel p) {
    frame.getContentPane().removeAll();
    panel = p;
    frame.getContentPane().add(panel);
    frame.revalidate();
  }

  public View getView() {
    return this.panel;
  }
}
