package model.items;

import static constants.Constants.*;
import model.entities.players.Player;
import model.managers.TilesManager;

public class Food extends Item {

	private final String type;
    private final int scoreValue;

    public Food(String type, int x, int y, int side, int score, TilesManager tilesManager) {
        super(x, y, side, tilesManager);
        this.type = type;
        this.scoreValue = score;
    }

    public Food(String type, int x, int y, int score, TilesManager tilesManager) {
        this(type, x, y, 2 * TILESIZE, score, tilesManager);
    }

    @Override
    public void collect(Player collector) {
        if (!readyToRemove) {
            collector.addScore(scoreValue);
            readyToRemove = true;
        }
    }

    public int getScore() { return scoreValue; }
    
    @Override
    public String getTypeData() {
    	return type.toUpperCase();
    };
    
}
