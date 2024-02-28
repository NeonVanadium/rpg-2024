package game;

import java.awt.Point;

public abstract class GameObject {
  protected String name;

  private double exactX = 10.0;
  private double exactY = 10.0;

  public String getName() {
    return name;
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
    return String.format("Name: %s, Location: %s, %s", name, exactX, exactY);
  }
}
