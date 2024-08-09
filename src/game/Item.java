package game;

import shared.Util;

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

  @Override
  public String getDefiniteDescription() {
    return "the " + description;
  }

  @Override
  public String getIndefiniteDescription() {
    return Util.addIndefiniteArticle(description);
  }
}
