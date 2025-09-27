package model.managers;

import model.GameModel;
import model.utils.MinorEvent;


public class MinorEventsManager {

    private GameModel gameModel;

    public MinorEventsManager(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void createScoreEvent(int x, int y, String type, String user, int scoredPoints) {
        if (gameModel != null) {
            gameModel.notifyMinorEvent(new MinorEvent(x, y, type, user, scoredPoints));
        }
    }

    public void createEvent(int x, int y, String type) {
        if (gameModel != null) {
            gameModel.notifyMinorEvent(new MinorEvent(x, y, type, null, -1));
        }
    }
    
}