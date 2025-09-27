package model.items;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import static constants.Constants.*;

public class FoodRandomizer {

    private final HashMap<Range, String> foodTypes = new HashMap<>();
    private final ArrayList<Point> coordinates = new ArrayList<>();
    private final Random random = new Random();
    private int[][] tiles;

    public FoodRandomizer(int[][] tiles) {
        this.tiles = tiles;
        setItemTypes();
    }

    private void setItemTypes() {
        foodTypes.put(new Range(0, 25), "GreenPepper");
        foodTypes.put(new Range(25, 45), "RedTurnip");
        foodTypes.put(new Range(45, 60), "Cucumber");
        foodTypes.put(new Range(60, 75), "Corn");
        foodTypes.put(new Range(75, 85), "Persimmon");
        foodTypes.put(new Range(85, 90), "Banana");
        foodTypes.put(new Range(90, 94), "Peach");
        foodTypes.put(new Range(94, 97), "FrenchFries");
        foodTypes.put(new Range(97, 99), "Crystal");
        foodTypes.put(new Range(99, 100), "BigWatermelon");
    }

    public void updateMap() {
        coordinates.clear();
        int x = 2;
        int y = 3;
        int boundX = MAXSCREENCOL - 3;
        int boundY = MAXSCREENROW;

        while (y < boundY) {
            int tileNum = tiles[y][x];
            if (tileNum != 0 && tiles[y - 2][x] == 0) coordinates.add(new Point(x, y - 2));

            x++;
            if (x == boundX) {
                x = 0;
                y++;
            }
        }
    }

    public Point randomizeCoordinates() {
        if (coordinates.isEmpty()) return new Point(0, 0);
        int rIndex = random.nextInt(coordinates.size());
        return coordinates.get(rIndex);
    }

    public String randomizeItem() {
        int r = random.nextInt(100);
        Optional<String> type = foodTypes.entrySet()
                .stream()
                .filter(i -> i.getKey().contains(r))
                .map(i -> i.getValue())
                .findFirst();
        return type.orElse("GreenPepper");
    }

    private static class Range {
        private final int low;
        private final int high;

        private Range(int low, int high) {
            this.low = low;
            this.high = high;
        }

        private boolean contains(int number) {
            return number >= low && number < high;
        }
    }
}
