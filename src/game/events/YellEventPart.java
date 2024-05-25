package game.events;

import game.ControlOrb;
import game.characters.CharacterManager;

import java.awt.*;

public class YellEventPart extends SayEventPart {
  public YellEventPart(String rawLine) {
    super(rawLine);
  }

  @Override
  public void run(ControlOrb orb) {
    orb.clear();
    orb.setColor(Color.RED);
    orb.print(format());
  }

  /*@Override
  protected String format() {
    return CharacterManager.getKnownName(speaker) + ": ^r\"" + line + "\"^r";
  }*/
}
