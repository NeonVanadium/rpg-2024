package game;

import java.awt.Point;

public abstract class GameObject {
  protected String name;

  protected Point position = new Point(10,10);

  public String getName() {
    return name;
  }

  public Point getPosition() {
    return position;
  }

  public void setPosition(int x, int y) {
    position.x = x;
    position.y = y;
  }

  public String toString() {
    return String.format("Name: %s, Location: %s, %s", name, position.getX(), position.getY());
  }
}
