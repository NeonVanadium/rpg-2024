package game.combat;

import game.ControlOrb;
import game.Player;
import game.Util;
import game.characters.CharacterManager;
import game.characters.CombatWrapper;
import game.characters.GameCharacter;
import game.prompts.PromptOption;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CombatManager {

  private static List<List<CombatWrapper>> enemyLists = new LinkedList<>();
  private static List<CombatWrapper> allies = new LinkedList<>();
  private static List<CombatWrapper> initiativeOrder;
  private static List<CombatWrapper> dudesWhoAreNotDead; // I can be funny it's a personal project.
  private static List<PromptOption> options = new LinkedList<>();
  private static CombatWrapper player;

  public static void startCombat(ControlOrb orb) {
    setupAllies();
    orb.setTitle("Fight!");
    nameAndIntroduceCombatants(orb);
    determineInitiative();
    combatLoop(orb);
    endCombat();
  }

  private static void combatLoop(ControlOrb orb) {
    while (!isCombatOver()) {
      for (CombatWrapper c : initiativeOrder) {
        if (c.isAlive()) {
          if (c.isPlayer()) {
            handlePlayerAction(orb);
          } else {
            handleNPCAction(c, orb);
          }
        }
      }

      orb.enterToContinue();
    }
  }

  private static void handlePlayerAction(ControlOrb orb) {
    options.clear();

    for (CombatWrapper c : livingCombatantsOnTeamsOtherThan(allies)) {
      if (c.isAlive()) {
        options.add(new PromptOption(c.getNameHealthAndEnergy(), c));
      }
    }

    orb.enterToContinue();
    orb.clear();
    orb.print("Select target:");
    CombatWrapper target = (CombatWrapper) orb.getChoiceFromOptions(options).getObject();

    orb.clear();

    orb.print("You attack " + target.getNameToDisplayAsOption() + "!");

    target.hurt(7, orb);

    orb.enterToContinue();
  }

  private static void handleNPCAction(CombatWrapper npc, ControlOrb orb) {
    turnStartChecks();
    orb.clear();

    List<CombatWrapper> potentialTargets = livingCombatantsOnTeamsOtherThan(getTeamOf(npc));

    if (potentialTargets.isEmpty()) {
      orb.print(npc.getNameToDisplayAsOption() + " has no valid targets. They stand idle.");
    } else {
      CombatWrapper target = potentialTargets.get(Util.random(potentialTargets.size()));

      orb.print(npc.getNameToDisplayAsOption() + " attacks " + target.getNameToDisplayAsOption() + "!");

      target.hurt(Util.random(5) + 1, orb);
    }

    //orb.enterToContinue();
  }

  private static List<CombatWrapper> getTeamOf(CombatWrapper c) {
    if (allies.contains(c))
      return allies;
    else {
      for (List<CombatWrapper> team : enemyLists) {
        if (team.contains(c)) {
          return team;
        }
      }
    }
    return null;
  }

  private static void turnStartChecks() {
    //checkLiving();
  }

  private static List<CombatWrapper> livingCombatantsOnTeamsOtherThan(List<CombatWrapper> thisOne) {
    List<CombatWrapper> result = new LinkedList<>();
    if (thisOne != allies) {
      result.addAll(allies);
    }
    for (List<CombatWrapper> l : enemyLists) {
      if (l != thisOne) result.addAll(l);
    }
    return result.stream().filter(CombatWrapper::isAlive).toList();
  }

  private static void determineInitiative() {
    initiativeOrder = new LinkedList<>();
    initiativeOrder.addAll(allies);
    for (List<CombatWrapper> l : enemyLists) initiativeOrder.addAll(l);
    initiativeOrder.sort(Comparator.comparingInt(a -> a.getModifier("REFLEXES")));
  }

  private static boolean isCombatOver() {
    if (!player.isAlive()) return true; // player ded
    else {
      for (List<CombatWrapper> l : enemyLists) {
        for (CombatWrapper c : l) {
          if (c.isAlive()) {
            return false; // at least one enemy remains.
          }
        }
      }
    }
    return true; // player alive, no enemies remain.
  }

  private static void endCombat() {
    allies.clear();
    enemyLists.clear();
  }

  /*private static void checkLiving() {
    if (dudesWhoAreNotDead == null) {
      // on startup
      dudesWhoAreNotDead = new LinkedList<>();
      dudesWhoAreNotDead.addAll(allies.stream().filter(CombatWrapper::isAlive).toList());
      for (List<CombatWrapper> l : enemyLists) {
        dudesWhoAreNotDead.addAll(l.stream().filter(CombatWrapper::isAlive).toList());
      }
    } else {
      dudesWhoAreNotDead.removeIf(c -> !c.isAlive());
    }
  }*/

  /**
   * Handles names for combatants (handling duplicates, etc) and introduces them.
   * @param orb The ORB!
   */
  private static void nameAndIntroduceCombatants(ControlOrb orb) {
    for (List<CombatWrapper> l : enemyLists) {
      orb.clear();
      if (l.size() == 1) {
        orb.print(l.get(0).getNameToDisplayAsOption() + " prepares to fight!");
      } else {
        orb.print(Util.commasAndAnds(l, CombatWrapper::getNameToDisplayAsOption) + " prepare to fight!");
      }
      orb.enterToContinue();
    }
  }

  private static void setupAllies() {
    for (GameCharacter c : Player.getPartyMembers()) {
      allies.add(new CombatWrapper(c));
    }
    player = new CombatWrapper(Player.character);
    allies.add(player);
  }

  public static void addEnemiesByLabels(Iterable<String> labels) {
    List<CombatWrapper> enemies = new LinkedList<>();
    for (String label : labels) {
      enemies.add(new CombatWrapper(CharacterManager.get(label.trim())));
    }
    enemyLists.add(enemies);
  }

  public static boolean isCombatPending() {
    return enemyLists != null && !enemyLists.isEmpty();
  }

}
