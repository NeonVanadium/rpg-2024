package game.combat;

import game.ControlOrb;
import game.Player;
import shared.Util;
import game.characters.CharacterManager;
import game.characters.CombatWrapper;
import game.characters.GameCharacter;
import game.prompts.PromptOption;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CombatManager {

  private static List<List<CombatWrapper>> enemyLists = new LinkedList<>();
  private static List<CombatWrapper> allies = new LinkedList<>();
  private static List<CombatWrapper> initiativeOrder;
  private static HashMap<String, Integer> namesSoFar; // used to check for duplicate names
  private static List<PromptOption> options = new LinkedList<>();
  private static CombatWrapper player;
  private static boolean needsEnterBeforePlayerAction = false;

  public static void startCombat(ControlOrb orb) {
    namesSoFar = new HashMap<>();
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
        if (isCombatOver()) {
          orb.enterToContinue();
          return;
        } else if (c.isAlive()) {
          if (c.isPlayer()) {
            handlePlayerAction(orb);
          } else {
            handleNPCAction(c, orb);
          }
        }
      }
    }
  }

  private static void handlePlayerAction(ControlOrb orb) {
    if (needsEnterBeforePlayerAction) {
      orb.enterToContinue();
    }

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

    handleAttack(player, target, 8, orb);

    orb.enterToContinue();
    orb.clear();
  }

  private static void handleNPCAction(CombatWrapper npc, ControlOrb orb) {
    turnStartChecks();

    List<CombatWrapper> potentialTargets = livingCombatantsOnTeamsOtherThan(getTeamOf(npc));

    if (potentialTargets.isEmpty()) {
      orb.print(npc.getNameToDisplayAsOption() + " has no valid targets. They stand idle.");
    } else {
      CombatWrapper target = potentialTargets.get(Util.random(potentialTargets.size()));
      handleAttack(npc, target, Util.random(5) + 1, orb);
    }

    needsEnterBeforePlayerAction = true;
  }

  private static void handleAttack(CombatWrapper attacker, CombatWrapper target, int dmg, ControlOrb orb) {
    String subjectVerb = attacker == player ? "You attack " : attacker.getNameToDisplayAsOption() + " attacks ";
    orb.print(subjectVerb + target.getNameToDisplayAsOption() + " for " + dmg + "!");
    target.hurt(dmg, orb);
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
    orb.clear();
    for (CombatWrapper c : allies) {
      getNameAndSetDifferentiator(c);
    }
    for (List<CombatWrapper> l : enemyLists) {
      if (l.size() == 1) {
        orb.print(l.get(0).getNameToDisplayAsOption() + " prepares to fight!");
      } else {
        orb.print(Util.commasAndAnds(l, CombatManager::getNameAndSetDifferentiator) + " prepare to fight!");
      }
    }
    orb.enterToContinue();
    orb.clear();
  }

  private static String getNameAndSetDifferentiator(CombatWrapper combatant) {
    String name = combatant.getNameToDisplayAsOption();
    if (namesSoFar.containsKey(combatant.getNameToDisplayAsOption())) {
      combatant.setDistinguishingNumber(namesSoFar.get(name) + 1);
      namesSoFar.replace(name, namesSoFar.get(name) + 1);
      return combatant.getNameToDisplayAsOption();
    } else {
      namesSoFar.put(name, 1);
      return name;
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
