package game.events;

import game.ControlOrb;

public record TextEventPart(String text) implements EventPart {

  @Override
  public void run(ControlOrb orb) {
    orb.clear();
    orb.print(text);
  }
}
