import java.awt.Point;

public class GameCharacter {
  private String name;

  private Point position = new Point(10,10);

  public GameCharacter(String name) {
    this.name = name;
  }

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
