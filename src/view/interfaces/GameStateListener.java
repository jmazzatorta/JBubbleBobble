package view.interfaces;

public interface GameStateListener {

    void onGameStateChanged(model.states.GameStates newState);
}