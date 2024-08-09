package game.characters;

import game.TagAndTopicManager;
import shared.Util;

import java.util.Map;

public class GameCharacter extends game.Movable {

  public static int DEFAULT_MOVE_SPEED = 10;

  protected Map<String, Integer> stats;
  protected Map<String, String> attributes;
  private String descriptionOverride;

  public GameCharacter(String name, Map<String, String> attributes, Map<String, Integer> stats, String describeOverride) {
    this.label = name; this.attributes = attributes; this.stats = stats;
    this.descriptionOverride = describeOverride;
  }

  public String getGenericDescription() {
    if (!hasAttribute("GENDER")) {
      return "someone";
    }
    return TagAndTopicManager.getAttributeDescription(getAttributeForCategory("GENDER"));
  }

  public String getDefiniteGenericDescription() {
    String gender = getAttributeForCategory("GENDER").toUpperCase();
    if (!gender.equals("MAN") && !gender.equals("WOMAN")) {
      return "someone";
    }
    return "the " + gender;
  }

  public String getDetailedDescription() {
    StringBuilder result = new StringBuilder();
    String start = !this.hasAttribute("GENDER") ? "They are " : getAttributeForCategory("GENDER").equals("FEMALE") ? "She is " : "He is ";
    result.append(start);
    if (descriptionOverride != null) {
      result.append(descriptionOverride);
    } else {
      result.append(Util.commasAndAnds(this.attributes.values().stream().toList(), TagAndTopicManager::getAttributeAdjective));
    }
    result.append('.');
    return result.toString();
  }

  @Override
  public String getNameToDisplayAsOption() {
    if (CharacterManager.getKnownName(this.label).equals(CharacterManager.UNKNOWN_NAME)) {
      return getGenericDescription();
    } else {
      return CharacterManager.getKnownName(this.label);
    }
  }

  public boolean hasAttribute(String attributeName) {
    return attributes.containsKey(attributeName) || attributes.containsValue(attributeName);
  }

  public String getAttributeForCategory(String s) {
    if (s.equals("NAME")) {
      return CharacterManager.getKnownName(this.label);
    } else {
      return attributes.get(s);
    }
  }

  /**
   * For one-offs at runtime, not specified in the attributes file.
   */
  public void addAttribute(String category) {
    this.attributes.put(category, null);
    System.out.printf("Gave %s the one-off attribute \"%s\"%n\n", this.label, category);
  }

  /**
   * For adding pre-existing attributes from the attributes file by name.
   */
  public void addAttribute(String category, String attributeName) {
    this.attributes.put(category, attributeName);
    System.out.printf("Gave %s the attribute: %s -> %s%n\n", this.label, category, attributes.get(category));
  }

  public int getModifier(String statLabel) {
    return stats.get(statLabel);
  }

}


