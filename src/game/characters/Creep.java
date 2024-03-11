package game.characters;

/**
 * A repeatable stock character that will probably just show up in combat.
 */
public class Creep extends GameCharacter{

  String name;

  public Creep(String name) {
    super(name.toUpperCase(), Gender.SOMETHING_ELSE);
    this.name = name; // we uppercase the name to be the label
  }

  @Override
  public String getNameToDisplayAsOption() {
    return name;
  }

  public Creep clone() {
    return new Creep(this.name);
  }
}
