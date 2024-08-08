package controller;

/**
 * Doing my best to separate view and controller, but the view unfortunately contains buttons, which need to send things to the controller to work.
 * This pipeline is an insert-only container that can pipe values to the controller.
 */
public abstract class PipelineToController {

    public abstract void insertValue(Character s);

}
