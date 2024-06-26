package game.events;

import game.ControlOrb;

public interface EventPart {
  void run(ControlOrb orb);

  boolean pauseAfter();

  boolean pauseBefore();
}
