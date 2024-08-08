package game;

import controller.Controller;
import game.prompts.PromptOption;
import game.prompts.Selectable;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import shared.Util;
import view.View;

/**
 * Yeah the name's funny sue me.
 *
 * Pass the control orb to have access to view/controller and some methods that use both.
 */
public class ControlOrb {

  View view;
  Controller controller;
  Consumer<Selectable> processMove;

  public ControlOrb(View view, Controller controller, Consumer<Selectable> processMove) {
    this.view = view;
    this.controller = controller;
    this.processMove = processMove;
  }

  public PromptOption getChoiceFromOptions(List<PromptOption> options) {
    List<String> optionLabels = new LinkedList<>();

    // format the options
    int i = 1;
    for (PromptOption o : options) {
      optionLabels.add(String.format("%d) %s", i, Util.capitalizedAndPerioded(o.getLabel())));
      i++;
    }

    controller.setOptions(optionLabels);
    view.showOptions(optionLabels);
    int choice = controller.getChoice();

    while (choice == -1 || !view.isFinishedDrawing()) {
      if (view.isFinishedDrawing()) {
        System.out.println("Nothing entered.");
      }
      view.hurryUp();
      choice = controller.getChoice();
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

  public void respondToPlayerChoice(Selectable selection) {
    processMove.accept(selection);
  }

  public String getTextInput() {
    view.promptTextInput();
    String userInput = "";
    while (userInput.isBlank()) {
      userInput = controller.getTextInput();
    }
    return userInput;
  }

  public void getAnyInput() {
    controller.getAnyInput();
  }

  public void wait(int ms) {
    view.wait(ms);
  }

  public void setTitle(String s) {
    view.setTitle(s);
  }

  public void print(String s) {
    view.print(s);
  }

  public void clear() {
    view.clear();
  }

  public void promptAnyInput() {
    view.promptAnyInput();
  }

  public boolean isFinishedDrawing() {
    return view.isFinishedDrawing();
  }

  public void hurryUp() {
    view.hurryUp();
  }
}
