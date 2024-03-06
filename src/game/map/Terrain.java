package game.map;

public final class Terrain {
  public final String name;
  public final int difficulty; // how hard is it to navigate this terrain? Higher -> harder.
  public final char representation;

  public static final Terrain j = new Terrain("jungle", 'j', 10);
  public static final Terrain d = new Terrain("desert", 'd', 1);
  public static final Terrain p = new Terrain("plains", 'p', 0);

  public Terrain(String name, char representation, int difficulty) {
    this.name = name;
    this.difficulty = difficulty;
    this.representation = representation;
  }
}
