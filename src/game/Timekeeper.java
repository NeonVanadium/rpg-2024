package game;

import view.View;

import javax.swing.*;

public class Timekeeper {
    private static final int TICK_FREQUENCY = 20; // how many milliseconds does it take for time to pass?
    private static final int TICKS_PER_HOUR = 120;
    private static final int HOURS_PER_DAY = 24; // maybe some fantasy worlds want different time frames idk
    private static final int TICKS_PER_DAY = TICKS_PER_HOUR * HOURS_PER_DAY;
    private static final int START_HOUR = 10;
    private int tick = START_HOUR * TICKS_PER_HOUR;

    private View view;

    public Timekeeper(View view) {
        this.view = view;
        view.setDayLength(TICKS_PER_DAY);
        Timer t = new Timer(TICK_FREQUENCY, (e) -> tick());
        t.start();
    }

    private void tick() {
        tick = (tick + 1) % (TICKS_PER_DAY);
        view.setTime(tick);
    }
}
