package game.events;

import game.ControlOrb;
import game.characters.CharacterManager;

import java.awt.*;

public class YellEventPart extends SayEventPart {
  public YellEventPart(String rawLine) {
    super(rawLine);
  }

  @Override
  protected String format() {
    return CharacterManager.getKnownName(speaker) + ": ~r\"" + line + "\"~x";
  }
}
