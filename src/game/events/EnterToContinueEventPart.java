package game.events;

import game.ControlOrb;

public class EnterToContinueEventPart implements EventPart{
  public EnterToContinueEventPart() {};

  @Override
  public void run(ControlOrb orb) {
    orb.enterToContinue();
  }
}
