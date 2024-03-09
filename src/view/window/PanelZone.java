package view.window;

public class PanelZone implements IRectangle {

	private WolgonPanel panel;
	private PanelZone parent; // the parent of this Zone. Can be null.
	private PanelZone[] children; // all the direct children of this Zone
	private boolean isSplitHorizontally; // if there is a division (eg children isn't null), determines if the cut was horz.
	private double splitStartFraction; // at what fraction (of the x or why axis) is the zone split

	public PanelZone(WolgonPanel panel) {
		this.parent = null;
		this.panel = panel;
		this.panel.addZone("WHOLE", this);
	}

	public PanelZone(PanelZone parent, String name) {
		this.parent = parent;
		this.panel = parent.panel;
		this.panel.addZone(name, this);
	}

	public int getWidth() {
		if (parent == null) {
			return panel.getWidth();
		}
		else {
			if (parent.isSplitHorizontally) {
				return parent.getWidth();
			}
			else if (isTopOrLeftZone()) {
				return (int) (parent.getWidth() * parent.splitStartFraction);
			}
			else {
				return (int) (parent.getWidth() - (parent.getWidth() * parent.splitStartFraction));
			}
		}
	}

	public int getBufferedWith() {
		return getWidth() - (2 * WolgonPanel.BUFFER); // buffer on left and right sides.
	}

	public int getHeight() {
		if (parent == null) {
			return panel.getHeight();
		}
		else {
			if (!parent.isSplitHorizontally) {
				return parent.getHeight();
			}
			else if (isTopOrLeftZone()) {
				return (int) (parent.getHeight() * parent.splitStartFraction);
			}
			else {
				return (int) (parent.getHeight() - (parent.getHeight() * parent.splitStartFraction));
			}
		}
	}

	public int getX() {
		if (parent == null) {
			return panel.getX();
		}
		else {
			if (parent.isSplitHorizontally || isTopOrLeftZone()) {
				return parent.getX();
			}
			else {
				return (int) (parent.getX() + (parent.getWidth() * parent.splitStartFraction));
			}
		}
	}

	public int getY() {
		if (parent == null) {
			return panel.getY();
		}
		else {
			if (!parent.isSplitHorizontally || isTopOrLeftZone()) {
				return parent.getY();
			}
			else {
				return (int) (parent.getY() + (parent.getHeight() * parent.splitStartFraction));
			}
		}
	}

	private boolean isTopOrLeftZone() { 
		return this == parent.children[0];
	}

	public void splitVertically(double splitStartFraction, String leftName, String rightName) {
		this.isSplitHorizontally = false;
		splitHelper(splitStartFraction, leftName, rightName);
	}

	public void splitHorizontally(double splitStartFraction, String topName, String bottomName) {
		this.isSplitHorizontally = true;
		splitHelper(splitStartFraction, topName, bottomName);
	}

	// abstraction helper for the above two methods
	private void splitHelper(double splitStartFraction, String name1, String name2) {
		this.children = new PanelZone[2];
		this.splitStartFraction = splitStartFraction;
		this.children[0] = new PanelZone(this, name1);
		this.children[1] = new PanelZone(this, name2);
	}
}