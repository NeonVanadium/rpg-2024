package view;

/**
 * Stores day/night related info for the view.
 */
public class ViewTime {
    public final int ticksPerDay, hoursPerDay, ticksPerHour, darkHours, dawnTime, duskTime;

    public ViewTime(int ticksPerDay, int hoursPerDay) {
        this.ticksPerDay = ticksPerDay;
        this.hoursPerDay = hoursPerDay;
        this.ticksPerHour = ticksPerDay / hoursPerDay;
        this.darkHours = hoursPerDay / 3;
        this.dawnTime = (3 * darkHours) / 4;
        this.duskTime = (hoursPerDay - (darkHours / 4));
    }

    public String tickToMinute(int curTime) {
        int val = (curTime % ticksPerHour) / (ticksPerHour / 60);
        return val < 10 ? "0" + val : String.valueOf(val);
    }

    public double tickToHour(int curTime) {
        return (double) curTime / ticksPerHour;
    }
}
