package map;

import java.awt.Rectangle;
import src.GameCharacter;

public class GameMap {
  private Rectangle area = new Rectangle(50, 50);

  public boolean moveCharacter(GameCharacter c, int x, int y) {
    int newX = (int) c.getPosition().getX() + x;
    int newY = (int) c.getPosition().getY() + y;

    if (newX < 0) {
      System.out.println("West boundary hit.");
    } else if (newX > area.width) {
      System.out.println("East boundary hit.");
    } else if (newY < 0) {
      System.out.println("South boundary hit.");
    } else if (newY > area.height) {
      System.out.println("North boundary hit.");
    } else {
      c.setPosition(newX, newY);
      return true;
    }

    return false;

  }
}
