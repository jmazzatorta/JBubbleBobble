package model.states;

import model.GameModel; // Importa GameModel

public class StateManager {
    
    private GameStates currentState;
    private GameModel gameModel; 

    public StateManager() {
        this.currentState = GameStates.INTRO; 
    }

    // NUOVO: Metodo per l'iniezione del GameModel
    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }
    
    public GameStates getState() {
        return currentState;
    }

    /**
     * MODIFICATO: Ora questo metodo notifica anche il cambio di stato.
     * @param newState il nuovo stato del gioco.
     */
    public void setState(GameStates newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            
            if (gameModel != null) {
                gameModel.notifyGameStateChanged(newState);
            }
        }
    }
}