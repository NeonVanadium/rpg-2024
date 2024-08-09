package game.characters;

/**
 * An attribute is a single example of a trait, like a race or class, that applies to a character.
 */
public class Attribute {
  public final String name, adjective, description;

  public Attribute(String name, String adjective, String description) {
    this.name = name;
    this.adjective = adjective;
    this.description = description;
  }

  public boolean hasDescription() {
    return description != null && !description.isBlank();
  }

  public boolean hasAdjective() {
    return description != null && !description.isBlank();
  }

  public String toString() {
    return name + " (" + adjective + "): " + description;
  }
}
