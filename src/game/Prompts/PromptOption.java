package game.Prompts;

public class PromptOption {
  String label;
  Object object;

  public PromptOption(String label, Object o) {
    this.label = label;
    this.object = o;
  }

  public PromptOption(Object o) {
    this.label = o.toString();
    this.object = o;
  }

  public String getLabel() { return label; }

  public Object getObject() { return object; }
}

