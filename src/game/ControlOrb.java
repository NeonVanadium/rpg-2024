package game;

import controller.Controller;
import game.prompts.PromptOption;
import java.util.LinkedList;
import java.util.List;
import view.View;

/**
 * Yeah the name's funny sue me.
 *
 * Pass the control orb to have access to view/controller and some methods that use both.
 */
public class ControlOrb implements View, Controller {

  View view;
  Controller controller;

  public ControlOrb(View view, Controller controller) {
    this.view = view;
    this.controller = controller;
  }

  public PromptOption getChoiceFromOptions(List<PromptOption> options) {
    List<String> optionLabels = new LinkedList<>();
    int i = 1;
    for (PromptOption o : options) {
      view.print(String.format("%d) %s", i, o.getLabel()));
      optionLabels.add(o.getLabel());
      i++;
    }
    int choice = controller.setOptions(optionLabels);
    while (choice == -1 || !view.isFinishedDrawing()) {
      if (view.isFinishedDrawing()) {
        System.out.println("Nothing entered.");
      }
      view.hurryUp();
      choice = controller.setOptions(optionLabels);
    }
    return options.get(choice);
  }

  public void enterToContinue() {
    view.promptAnyInput();
    controller.getAnyInput();
    if (!view.isFinishedDrawing()) {
      view.hurryUp();
      controller.getAnyInput();
    }
  }

  @Override
  public int setOptions(List<String> options) {
    return controller.setOptions(options);
  }

  @Override
  public void getAnyInput() {
    controller.getAnyInput();
  }

  @Override
  public void wait(int ms) {
    view.wait(ms);
  }

  @Override
  public void setTitle(String s) {
    view.setTitle(s);
  }

  @Override
  public void print(String s) {
    view.print(s);
  }

  @Override
  public void clear() {
    view.clear();
  }

  @Override
  public void promptAnyInput() {
    view.promptAnyInput();
  }

  @Override
  public boolean isFinishedDrawing() {
    return view.isFinishedDrawing();
  }

  @Override
  public void hurryUp() {
    view.hurryUp();
  }
}
