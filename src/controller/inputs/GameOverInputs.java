package controller.inputs;
import model.GameModel;
import model.states.GameStates;

public class GameOverInputs {
    private final GameModel model;
    public GameOverInputs(GameModel model) { this.model = model; }

    public void confirm() {
        model.getStateManager().setState(GameStates.STATS_MANAGEMENT);
    }
}