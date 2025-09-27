package controller.inputs;
import model.GameModel;
import model.states.GameStates;

public class VictoryInputs {
    private final GameModel model;
    public VictoryInputs(GameModel model) { this.model = model; }

    public void continueToNextLevel() {
        model.getStateManager().setState(GameStates.PLAYING);
    }

    public void backToMainMenu() {
        model.getStateManager().setState(GameStates.STATS_MANAGEMENT);
    }
}