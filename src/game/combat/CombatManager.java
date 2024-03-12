package game.combat;

import game.ControlOrb;
import game.Player;
import game.Util;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CombatManager {

  private static List<List<GameCharacter>> enemyLists = new LinkedList<>();
  private static List<GameCharacter> allies = new LinkedList<>();
  private static List<GameCharacter> initiativeOrder;

  public static void runCombat(ControlOrb orb) {
    allies.addAll(Player.getPartyMembers());
    allies.add(Player.character);

    orb.setTitle("Fight!");

    for (List<GameCharacter> l : enemyLists) {
      orb.clear();
      if (l.size() == 1) {
        orb.print(l.get(0).getNameToDisplayAsOption() + " prepares to fight!");
      } else {
        orb.print(Util.commasAndAnds(l, GameCharacter::getNameToDisplayAsOption) + " prepare to fight!");
      }
      orb.enterToContinue();
    }

    orb.clear();
    determineInitiative();

    for (GameCharacter c : initiativeOrder) {
      orb.print(c.getNameToDisplayAsOption() + " attacks!");
    }

    orb.enterToContinue();
    endCombat();
  }

  private static void determineInitiative() {
    initiativeOrder = new LinkedList<>();
    initiativeOrder.addAll(allies);
    for (List<GameCharacter> l : enemyLists) initiativeOrder.addAll(l);
    initiativeOrder.sort(Comparator.comparingInt(a -> a.roll("INITIATIVE")));
  }

  private static void endCombat() {
    allies.clear();
    enemyLists.clear();
  }

  public static void addEnemies(List<GameCharacter> enemies) {
    enemyLists.add(enemies);
  }

  public static void addEnemiesByLabels(Iterable<String> labels) {
    List<GameCharacter> enemies = new LinkedList<>();
    for (String label : labels) {
      enemies.add(CharacterManager.get(label.trim()));
    }
    enemyLists.add(enemies);
  }

  public static boolean isCombatPending() {
    return enemyLists != null && !enemyLists.isEmpty();
  }

}
