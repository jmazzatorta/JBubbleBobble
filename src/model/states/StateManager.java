package model.states;

import model.GameModel; 

public class StateManager {
    
    private GameStates currentState;
    private GameModel gameModel; 

    public StateManager() {
        this.currentState = GameStates.INTRO; 
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }
    
    public GameStates getState() {
        return currentState;
    }

    public void setState(GameStates newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            
            if (gameModel != null) {
                gameModel.notifyGameStateChanged(newState);
            }
        }
    }
}