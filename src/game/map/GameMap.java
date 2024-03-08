package game.map;

import game.Player;
import game.characters.CharacterManager;
import game.characters.Movable;
import game.GameObject;
import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GameMap {

  private Terrain[][] map = new Terrain[][]{
      {Terrain.p, Terrain.p, Terrain.p, Terrain.d, Terrain.d},
      {Terrain.p, Terrain.p, Terrain.p, Terrain.d, Terrain.d},
      {Terrain.j, Terrain.p, Terrain.p, Terrain.p, Terrain.p},
      {Terrain.j, Terrain.j, Terrain.p, Terrain.p, Terrain.p},
      {Terrain.j, Terrain.j, Terrain.p, Terrain.p, Terrain.p},
  };

  public final int tileSize = 10;

  private final int width = map[0].length * tileSize;
  private final int height = map.length * tileSize;

  //private List<GameObject>[][] objectsByTile = new LinkedList[height][width];
  private Set<GameObject> objects = new HashSet<>();


  public void moveCharacter(Movable c, int x, int y) {
    int newX = (int) c.getPosition().getX() + x;
    int newY = (int) c.getPosition().getY() + y;

    if (newX < 0) {
      System.out.println("West boundary hit.");
      newX = 0;
    } else if (newX >= width) {
      System.out.println("East boundary hit.");
      newX = width;
    } else if (newY < 0) {
      System.out.println("South boundary hit.");
      newY = 0;
    } else if (newY >= height) {
      System.out.println("North boundary hit.");
      newY = height;
    }
    putGameObject(c, newX, newY);

  }

  public void putGameObject(GameObject obj, double x, double y) {
    putGameObject(obj);
    obj.setPosition(x, y);
    if (obj == CharacterManager.player()) {
      for (Movable member : Player.getPartyMembers()) {
        putGameObject(member, x, y);
      }
    }
  }

  public void putGameObject(GameObject obj) {
    objects.add(obj);
  }

  public void removeFromBoard(GameObject obj) {
    objects.remove(obj);
  }

  public List<GameObject> searchObjects(Point from, int range, GameObject except) {
    LinkedList<GameObject> found = new LinkedList<>();
    // TODO: dangerous pass by reference.
    for (GameObject o : objects) {
      if (o != except && o.getPosition().distance(from.x, from.y) < range) {
        found.add(o);
      }
    }
    return found;
  }

  public List<GameObject> visibleObjects(Movable obj, int range) {
    return searchObjects(obj.getPosition(), range, obj);
  }

  public String getTerrainTypeAt(double x, double y) {
    if (!areValidCoordinates(x, y)) {
      return "impassable";
    }

    int roundedX = (int) (x / tileSize);
    int roundedY = (int) (y / tileSize);

    // the map is flipped on the y axis. Yep I'm doing this.

    int trueY = map.length - roundedY - 1;

    return map[trueY][roundedX].name;
  }

  private boolean areValidCoordinates(double x, double y) {
    return !(x < 0 || x >= width || y < 0 || y >= height);
  }
}
