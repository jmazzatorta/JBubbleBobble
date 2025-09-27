package model.entities;

public class PowerUp {
    private final int durationTicks;
    private int currentTick;

    public PowerUp(int durationTicks) {
        this.durationTicks = durationTicks;
        this.currentTick = 0;
    }

    public void tick() {
        if (currentTick < durationTicks) currentTick++;
    }

    public boolean isExpired() {
        return currentTick >= durationTicks;
    }

    public void reset() {
        currentTick = 0;
    }
}

