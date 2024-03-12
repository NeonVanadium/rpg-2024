package game.events;

import game.ControlOrb;
import game.GameMaster;

public class MoveEventPart implements EventPart{

  String subject, dest1, dest2;

  public MoveEventPart(String rawLine) {
    String[] split = rawLine.split(" ");
    subject = split[1];
    dest1 = split[2];
    dest2 = split[3];
  }

  @Override
  public void run(ControlOrb orb) {
    GameMaster.placementHelper(subject, dest1, dest2);
  }

  @Override
  public boolean pauseAfter() {
    return false;
  }

  @Override
  public boolean pauseBefore() {
    return false;
  }
}
