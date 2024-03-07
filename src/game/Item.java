package game;

public class Item extends Movable {
  public final String label, simpleName, description;

  public Item(String label, String simpleName, String description) {
    this.label = label;
    this.simpleName = simpleName;
    this.description = description;
  }

  @Override
  public String getNameToDisplayAsOption() {
    return description;
  }
}
