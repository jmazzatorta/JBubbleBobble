package controller.inputs;
import model.GameModel;
import model.managers.StatisticsManager;
import model.utils.DirectionX;
import model.utils.DirectionY;

public class StatsInputs {
    private final GameModel model;
    public StatsInputs(GameModel model) { this.model = model; }

    public void confirm() {
        model.getStatisticsManager().endPhase();
    }

    public void moveSelection(DirectionX dir) {
        if (model.getStatisticsManager().getCreationPhase() == StatisticsManager.Phases.AVATAR_SELECTION) {
            model.getStatisticsManager().moveSelection(dir);
        }
    }
    
    public void moveSelection(DirectionY dir) {
        if (model.getStatisticsManager().getCreationPhase() == StatisticsManager.Phases.AVATAR_SELECTION) {
            model.getStatisticsManager().moveSelection(dir);
        }
    }

    public void typeChar(char c) {
        if (model.getStatisticsManager().getCreationPhase() == StatisticsManager.Phases.NAME_SELECTION) {
            if (Character.isLetterOrDigit(c)) {
                model.getStatisticsManager().setTypedChar(c);
            }
        }
    }

    public void deleteChar() {
        if (model.getStatisticsManager().getCreationPhase() == StatisticsManager.Phases.NAME_SELECTION) {
            model.getStatisticsManager().deleteChar();
        }
    }
}