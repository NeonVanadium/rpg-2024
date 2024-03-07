package game.events;

import game.ControlOrb;
import game.Player;
import game.characters.CharacterManager;

public class LeavePartyEventPart extends JoinPartyEventPart{

  public LeavePartyEventPart(String rawLine) {
    super(rawLine);
  }

  public void run(ControlOrb orb) {
    Player.removeFromParty(this.characterLabel, orb);
  }
}
