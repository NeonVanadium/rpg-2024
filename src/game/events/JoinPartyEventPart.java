package game.events;

public class JoinPartyEventPart implements EventPart{
  String characterLabel;

  public JoinPartyEventPart(String rawLine) {
    this.characterLabel = rawLine.split(" ")[1].trim();
  }
}
