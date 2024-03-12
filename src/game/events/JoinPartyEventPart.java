package game.events;

import game.ControlOrb;
import game.Player;

public class JoinPartyEventPart implements EventPart{
  String characterLabel;

  public JoinPartyEventPart(String rawLine) {
    this.characterLabel = rawLine.split(" ")[1].trim();
  }

  @Override
  public void run(ControlOrb orb) {
    Player.addToParty(characterLabel, orb);
  }

  @Override
  public boolean pauseAfter() {
    return true;
  }

  @Override
  public boolean pauseBefore() {
    return false;
  }
}
