package model.utils;

public class Range {
    private final int low;
    private final int high;

    public Range(int low, int high) {
        this.low = low;
        this.high = high;
    }

    public boolean contains(int value) {
        return value >= low && value < high;
    }
}
