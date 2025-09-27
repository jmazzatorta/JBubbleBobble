package controller.inputs;
import model.GameModel;
import model.states.GameStates;

public class IntroInputs {
    private final GameModel model;
    public IntroInputs(GameModel model) { this.model = model; }
    
    public void confirm() {
        model.getLevelManager().initFirstLevel();
        model.getStateManager().setState(GameStates.PLAYING);
    }
}