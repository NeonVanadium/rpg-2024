package game.events;

import game.ControlOrb;
import game.Util;
import game.combat.CombatManager;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FightEventPart implements EventPart {

  List<String[]> enemyGroups;

  public FightEventPart(String rawLine) {
    String enemies = rawLine.substring(rawLine.indexOf(' ')); // cut out the fight label
    String[] teams = enemies.split(Util.COMPONENT_DELINEATOR);
    enemyGroups = new LinkedList<>();

    for (String s : teams) {
      enemyGroups.add(s.split(","));
    }
  }

  @Override
  public void run(ControlOrb orb) {
    for (String[] labels : enemyGroups) {
      CombatManager.addEnemiesByLabels(Arrays.stream(labels).toList());
    }
    CombatManager.runCombat(orb);
  }
}
