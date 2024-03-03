package game.prompts;

public class PromptOption {
  String label;
  Selectable object;

  public PromptOption(String label, Selectable o) {
    this.label = label;
    this.object = o;
  }

  public PromptOption(Selectable o) {
    this.label = o.toString();
    this.object = o;
  }

  public String getLabel() { return label; }

  public Selectable getObject() { return object; }
}

