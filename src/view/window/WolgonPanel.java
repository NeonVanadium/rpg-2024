package view.window;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import view.View;

// an abstract panel designed for a specific portion of gameplay (world, menu, etc)
abstract class WolgonPanel extends JPanel implements IRectangle, View {

	private static final long serialVersionUID = 1L;

	private HashMap<String, PanelZone> zones = new HashMap<String, PanelZone>();
	private HashMap<String, Label> labels = new HashMap<String, Label>();
	
	// initialized by other classes only. Names of the labels that need to be removed on update
	protected String[] tempLabelNames; 

	// if the panel was resized last frame. Starts true so all positioners are called on startup.
	private boolean resized = true; 
	private Label hoveredOver; // the label currently hovered over by the mouse, if any.

	public static final int BUFFER = 30; // size of the whitespace buffer for the edges of all zones
	public static final float DEFAULT_FONT_SIZE = 30f;

	//protected UserTypeLabel typeBox; // a pointer to the on-panel typable label, if present
	protected Label hoverTextBox; // a label to hold any text that pops up when a button is hovered over


	public WolgonPanel() {
		this.setFocusable(true);
		this.setBackground(Color.BLACK);

		// this zone represents the entire panel
		new PanelZone(this);

		this.addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {	
				if (hoveredOver != null) {
					hoveredOver.runFunction();
				}
			}		
		});

		this.addMouseMotionListener( new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				// Optimize this more maybe
				for (Label l : labels.values()) {
					if (l.contains(e.getPoint())) {
						if (l != hoveredOver) {
							if (hoveredOver != null) hoveredOver.unhover();
							hoveredOver = l;
							hoveredOver.hover();
						}
					}
					else if (l == hoveredOver) {
						hoveredOver = null;
						l.unhover();
					}
				}
			}

			public void mouseDragged(MouseEvent e) {
				//nothing
			}
		});

		this.addComponentListener( new ComponentAdapter() {  	
			public void componentResized(ComponentEvent e) {
				setResized(true);
			}	
		});	
	}

	// redraws the panel and does whatever needs be done each frame
	public abstract void update();

	//draws the panel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);       

		g.setFont(g.getFont().deriveFont(DEFAULT_FONT_SIZE));
		g.setColor(Color.WHITE);

		for (Label l : labels.values()) {
			l.draw(g);
		}		
	}

	protected void addZone(String key, PanelZone value) {
		zones.put(key, value);
	}

	protected PanelZone getZone(String key) {
		return zones.get(key);
	}

	// adds an existing label to the table (Rhymes!)
	protected void addLabel(String key, Label value) {
		labels.put(key, value);
	}

	// gets a label from the table (Rhymes again!)
	protected Label getLabel(String key) {
		return labels.get(key);
	}

	//for use by subclasses
	protected void setResized(Boolean b) {
		resized = b;
	}

	/*protected Point getLastMousePosition() {
		return lastMousePosition;
	}

	protected void setLastMousePosition(Point to) {
		lastMousePosition = to;
	}*/

	public void removeTemporaryLabels() {
		if (tempLabelNames != null) for (String name : tempLabelNames) {
			labels.remove(name);
		}
	}
	
	//
	// IRECTANGLE METHODS
	//
	
	public int getWidth() {
		return getBounds().width;
	}
	
	public int getHeight() {
		return getBounds().height;
	}
	
	// the following two return zero since a panel acts as the baseline for Zone positioning,
	// thus its top corner must be (0, 0)
	
	public int getX() { return 0; }
	
	public int getY() { return 0; }

}
