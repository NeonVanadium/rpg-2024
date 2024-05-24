package game.events;

import game.ControlOrb;

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
}
