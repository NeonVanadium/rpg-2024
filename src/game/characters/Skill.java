package game.characters;

import game.Util;
import java.util.Arrays;

public class Skill {
  public final String name, description;
  public final String[] relevantStats;
  public Skill(String rawLine) {
    String[] parts = rawLine.substring(rawLine.indexOf(" ")).split(Util.COMPONENT_DELINEATOR);
    this.name = parts[0].trim();
    this.description = parts[2].trim();
    if (!parts[1].contains(",")) {
      relevantStats = new String[] { parts[1].trim() };
    } else {
      relevantStats = parts[1].trim().split(",");
      for (int i = 0; i < relevantStats.length; i++) {
        relevantStats[i] = relevantStats[i].trim();
      }
    }
  }

  public String toString() {
    return String.format("%s (%s): %s", this.name,
        Util.commasAndAnds(Arrays.stream(this.relevantStats).toList(), String::toString),
        this.description);
  }
}
