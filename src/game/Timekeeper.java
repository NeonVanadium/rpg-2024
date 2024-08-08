package game;

import view.View;

import javax.swing.*;

public class Timekeeper {
    private static final int TICK_FREQUENCY = 200; // how many milliseconds does it take for time to pass?
    private static final int TICKS_PER_HOUR = 500;
    private static final int HOURS_PER_DAY = 24; // maybe some fantasy worlds want different time frames idk
    private static final int TICKS_PER_DAY = TICKS_PER_HOUR * HOURS_PER_DAY;
    private static final int START_HOUR = 10;
    private int tick = START_HOUR * TICKS_PER_HOUR;

    private Timer t;
    private View view;

    public Timekeeper(View view) {
        this.view = view;
        view.setDayLength(TICKS_PER_DAY, HOURS_PER_DAY);
        view.setTime(tick);
        t = new Timer(TICK_FREQUENCY, (e) -> tick());
        t.start();
    }

    public void pause() {
        t.stop();
    }

    public void resume() {
        System.out.println("Resuming time flow.");
        t.start();
    }

    private void tick() {
        tick = (tick + 1) % (TICKS_PER_DAY);
        view.setTime(tick);
    }
}
