package game.map;

public final class Terrain {
  public final String symbol; // what short symbol represents this in the map file? usually one char.
  public final String name;
  public final int difficulty; // how hard is it to navigate this terrain? Higher -> harder.


  public Terrain(String symbol, String name,int difficulty) {
    this.symbol = symbol;
    this.name = name;
    this.difficulty = difficulty;
  }
}
