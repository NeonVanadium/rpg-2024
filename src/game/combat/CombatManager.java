package game.combat;

import game.ControlOrb;
import game.Player;
import game.Util;
import game.characters.CharacterManager;
import game.characters.GameCharacter;
import java.util.LinkedList;
import java.util.List;

public class CombatManager {

  public static List<List<GameCharacter>> enemyLists = new LinkedList<>();
  public static List<GameCharacter> allies = new LinkedList<>();

  public static void runCombat(ControlOrb orb) {
    allies.addAll(Player.getPartyMembers());
    allies.add(Player.character);

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

    for (GameCharacter c : allies) {
      orb.print(CharacterManager.getKnownName(c.getLabel()) + " whoops em.");
    }

    orb.enterToContinue();
    endCombat();
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
