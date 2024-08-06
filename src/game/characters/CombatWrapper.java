package game.characters;

import game.ControlOrb;
import game.GameObject;
import game.Player;

/**
 * A wrapper around a game character that handles combat information, like HP and mana.
 * Made so we don't have to modify big important GameCharacter objects during combat,
 * and to separate out combat functionality from other GameCharacter code.
 */
public class CombatWrapper extends GameObject implements game.prompts.Selectable {
  private GameCharacter character;
  private int health;
  private int energy;
  private int distinguishingNumber;

  public CombatWrapper(GameCharacter character) {
    this.character = character;
    this.health = 10;
    this.energy = 10; // TODO: Derive from stats, but make it configurable.
    this.distinguishingNumber = -1;
  }

  public CombatWrapper(GameCharacter character, int distinguishingNumber) {
    this.character = character;
    this.health = 10;
    this.energy = 10; // TODO: Derive from stats, but make it configurable.
    this.distinguishingNumber = distinguishingNumber;
  }

  public boolean isAlive() {
    return this.health > 0;
  }

  public void hurt(int damage, ControlOrb orb) {
    this.health -= damage;
    if (this.health <= 0) {
      orb.print(getNameToDisplayAsOption() + " falls unconscious!");
    }
  }

  public void spend(int expenditure) {
    this.energy -= expenditure;
  }

  @Override
  public String getNameToDisplayAsOption() {
    return character.getNameToDisplayAsOption();
  }

  public String getNameHealthAndEnergy() {
    return String.format("%s (%d Health, %d Energy)", getNameToDisplayAsOption(), this.health, this.energy);
  }

  public boolean isPlayer() {
    return character == Player.character;
  }

  public int getModifier(String statLabel) {
    return character.getModifier(statLabel);
  }
}
