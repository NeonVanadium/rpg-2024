package game;

import game.map.MapManager;
import game.prompts.Direction;
import game.prompts.Selectable;
import java.awt.Point;

public abstract class GameObject implements Selectable {
  protected String label;

  private double exactX = 10.0;
  private double exactY = 10.0;

  public String getLabel() {
    return label;
  }

  public Point getPosition() {
    return new Point((int) exactX, (int) exactY);
  }

  public double getX() { return exactX; }

  public double getY() { return exactY; }

  public void setPosition(double x, double y) {
    exactX = x;
    exactY = y;
  }

  public String toString() {
    return String.format("Name: %s, Location: %s, %s", label, exactX, exactY);
  }

  public boolean isFartherThan(Direction d, GameObject other) {
      if (d == Direction.NORTH) return this.exactY > other.exactY;
      else if (d == Direction.SOUTH) return this.exactY < other.exactY;
      else if (d == Direction.EAST) return this.exactX > other.exactX;
      else return this.exactX < other.exactX;
  }

  public boolean isInSameSpotAs(GameObject other) {
    // return this.exactX == other.exactX && this.exactY == other.exactY;
    return this.getPosition().distance(other.getPosition()) < MapManager.SIGNIFICANT_DISTANCE;
  }

  public abstract String getNameToDisplayAsOption();
}
