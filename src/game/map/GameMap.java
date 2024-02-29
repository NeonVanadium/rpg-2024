package game.map;

import game.characters.GameCharacter;
import game.GameObject;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class GameMap {
  private int width = 50;
  private int height = 50;

  private List<GameObject>[][] objectsByLocation = new LinkedList[height][width];
  private List<GameObject> objects = new LinkedList<>();


  public void moveCharacter(GameCharacter c, int x, int y) {
    int newX = (int) c.getPosition().getX() + x;
    int newY = (int) c.getPosition().getY() + y;

    if (newX < 0) {
      System.out.println("West boundary hit.");
      newY = 0;
    } else if (newX > width) {
      System.out.println("East boundary hit.");
      newX = width;
    } else if (newY < 0) {
      System.out.println("South boundary hit.");
      newY = 0;
    } else if (newY > height) {
      System.out.println("North boundary hit.");
      newY = height;
    }
    putGameObject(c, newX, newY);
  }

  public void putGameObject(GameObject obj, double x, double y) {
    if (!objects.contains(obj)) {
      objects.add(obj);
    }
    obj.setPosition(x, y);
  }

  public List<GameObject> searchObjects(Point from, int range) {
    LinkedList<GameObject> found = new LinkedList<>();
    // TODO: dangerous pass by reference.
    for (GameObject o : objects) {
      if (o.getPosition().distance(from.x, from.y) < range) {
        found.add(o);
      }
    }
    return found;
  }

  public List<GameObject> visibleObjects(GameCharacter obj, int range) {
    LinkedList<GameObject> found = new LinkedList<>();
    // TODO: dangerous pass by reference.
    for (GameObject o : objects) {
      if (obj != o && o.getPosition().distance(obj.getPosition().x, obj.getPosition().y) <= range) {
        found.add(o);
      }
    }
    return found;
  }
}
