package game.map;

import game.Player;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import game.GameObject;
import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GameMap {

  private final Terrain[][] map;

  private final int tileSize = 10;

  //private List<GameObject>[][] objectsByTile = new LinkedList[height][width];
  private Set<GameObject> objects = new HashSet<>();

  public GameMap(List<Terrain[]> tileRows) {
    this.map = new Terrain[tileRows.size()][];
    int index = 0;
    for (Terrain[] row : tileRows) {
      map[index] = row;
      index++;
    }
  }

  private int getWidth() {
    return map[0].length * tileSize;
  }

  private int getHeight() {
    return map.length * tileSize;
  }


  public void moveCharacter(GameCharacter c, int x, int y) {
    int newX = (int) c.getPosition().getX() + x;
    int newY = (int) c.getPosition().getY() + y;

    if (newX < 0) {
      System.out.println("West boundary hit.");
      newX = 0;
    } else if (newX >= getWidth()) {
      System.out.println("East boundary hit.");
      newX = getWidth();
    } else if (newY < 0) {
      System.out.println("South boundary hit.");
      newY = 0;
    } else if (newY >= getHeight()) {
      System.out.println("North boundary hit.");
      newY = getHeight();
    }
    putGameObject(c, newX, newY);

  }

  public void putGameObject(GameObject obj, double x, double y) {
    putGameObject(obj);
    obj.setPosition(x, y);
    if (obj == CharacterManager.player()) {
      for (GameCharacter member : Player.getPartyMembers()) {
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

  public List<GameObject> visibleObjects(GameCharacter obj, int range) {
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
    return !(x < 0 || x >= getWidth() || y < 0 || y >= getHeight());
  }
}
