package game.characters;

import shared.Util;

import java.util.Map;

public class GameCharacter extends game.Movable {

  public static int DEFAULT_MOVE_SPEED = 10;

  protected Map<String, Integer> stats;
  protected Map<String, Attribute> attributes;

  private String descriptionOverride;

  public GameCharacter(String name, Map<String, Attribute> attributes, Map<String, Integer> stats, String describeOverride) {
    this.label = name; this.attributes = attributes; this.stats = stats;
    this.descriptionOverride = describeOverride;
  }

  public String getGenericDescription() {
    if (!hasAttribute("GENDER")) {
      return "someone";
    }
    return attributes.get("GENDER").description;
  }

  public String getDefiniteGenericDescription() {
    String gender = attributes.get("GENDER").name.toUpperCase();
    if (!gender.equals("MAN") && !gender.equals("WOMAN")) {
      return "someone";
    }
    return "the " + gender;
  }

  public String getDetailedDescription() {
    StringBuilder result = new StringBuilder();
    String start = !this.hasAttribute("GENDER") ? "They are " : attributes.get("GENDER").name.equals("FEMALE") ? "She is " : "He is ";
    result.append(start);
    if (descriptionOverride != null) {
      result.append(descriptionOverride);
    } else {
      result.append(Util.commasAndAnds(this.attributes.values().stream().toList(), (Attribute a) -> a.adjective));
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
    for (String attributeCategory : attributes.keySet()) {
      if (attributeCategory.equals(attributeName)) {
        return true;
      }
    }
    for (Attribute a : attributes.values()) {
      if (a != null && a.name.equals(attributeName)) {
        return true;
      }
    }
    return false;
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
    this.attributes.put(category, CharacterManager.getAttributeByCategoryAndLabel(category, attributeName));
    System.out.printf("Gave %s the attribute: %s -> %s%n\n", this.label, category, attributes.get(category));
  }

  public int getModifier(String statLabel) {
    return stats.get(statLabel);
  }

}


