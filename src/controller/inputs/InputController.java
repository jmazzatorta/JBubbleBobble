package controller.inputs; 

import controller.inputs.*;
import model.GameModel;
import model.states.GameStates;
import model.utils.DirectionX;
import model.utils.DirectionY;

public class InputController {

    private final GameModel model;
    private final PlayingInputs playingInputs;
    private final IntroInputs introInputs;
    private final GameOverInputs gameOverInputs;
    private final VictoryInputs victoryInputs;
    private final StatsInputs statsInputs;

    public InputController(GameModel model, PlayingInputs playingInputs, IntroInputs introInputs,
                           GameOverInputs gameOverInputs, VictoryInputs victoryInputs, StatsInputs statsInputs) {
        this.model = model;
        this.playingInputs = playingInputs;
        this.introInputs = introInputs;
        this.gameOverInputs = gameOverInputs;
        this.victoryInputs = victoryInputs;
        this.statsInputs = statsInputs;
    }

    // Metodo per gestire l'azione "conferma" (es. Invio)
    public void handleConfirm() {
        switch (model.getStateManager().getState()) {
            case INTRO:      introInputs.confirm(); break;
            case GAMEOVER:   gameOverInputs.confirm(); break;
            case VICTORY:    victoryInputs.continueToNextLevel(); break;
            case STATS_MANAGEMENT: statsInputs.confirm(); break;
            default: break;
        }
    }

    // Metodo per gestire l'azione "annulla/indietro" (es. Esc)
    public void handleCancel() {
        if (model.getStateManager().getState() == GameStates.VICTORY) {
            victoryInputs.backToMainMenu();
        }
    }
    
    // Metodo per gestire l'input testuale
    public void handleTypedChar(char c) {
        if (model.getStateManager().getState() == GameStates.STATS_MANAGEMENT) {
            statsInputs.typeChar(c);
        }
    }

    public void handleDeleteChar() {
        if (model.getStateManager().getState() == GameStates.STATS_MANAGEMENT) {
            statsInputs.deleteChar();
        }
    }

    // --- Metodi che delegano direttamente a PlayingInputs ---
    public void p1_moveLeft_press()   { if (isState(GameStates.PLAYING)) playingInputs.p1_moveLeft_press(); }
    public void p1_moveLeft_release() { if (isState(GameStates.PLAYING)) playingInputs.p1_moveLeft_release(); }
    public void p1_moveRight_press()  { if (isState(GameStates.PLAYING)) playingInputs.p1_moveRight_press(); }
    public void p1_moveRight_release(){ if (isState(GameStates.PLAYING)) playingInputs.p1_moveRight_release(); }
    public void p1_jump()             { if (isState(GameStates.PLAYING)) playingInputs.p1_jump(); }
    public void p1_attack_press()     { if (isState(GameStates.PLAYING)) playingInputs.p1_attack_press(); }
    public void p1_attack_release()   { if (isState(GameStates.PLAYING)) playingInputs.p1_attack_release(); }
    public void addPlayer()           { if (isState(GameStates.PLAYING)) playingInputs.addPlayer(); }
    
    public void p2_moveLeft_press()   { if (isState(GameStates.PLAYING)) playingInputs.p2_moveLeft_press(); }
    public void p2_moveLeft_release()  { if (isState(GameStates.PLAYING)) playingInputs.p2_moveLeft_release(); }
    public void p2_moveRight_press()  { if (isState(GameStates.PLAYING)) playingInputs.p2_moveRight_press(); }
    public void p2_moveRight_release() { if (isState(GameStates.PLAYING)) playingInputs.p2_moveRight_release(); }
    public void p2_jump()              { if (isState(GameStates.PLAYING)) playingInputs.p2_jump(); }
    public void p2_attack_press()      { if (isState(GameStates.PLAYING)) playingInputs.p2_attack_press(); }
    public void p2_attack_release()    { if (isState(GameStates.PLAYING)) playingInputs.p2_attack_release(); }
    
    // --- Metodi che delegano direttamente a StatsInputs ---
    public void moveSelectionLeft()  { if (isState(GameStates.STATS_MANAGEMENT)) statsInputs.moveSelection(DirectionX.LEFT); }
    public void moveSelectionRight() { if (isState(GameStates.STATS_MANAGEMENT)) statsInputs.moveSelection(DirectionX.RIGHT); }
    public void moveSelectionUp()    { if (isState(GameStates.STATS_MANAGEMENT)) statsInputs.moveSelection(DirectionY.UP); }
    public void moveSelectionDown()  { if (isState(GameStates.STATS_MANAGEMENT)) statsInputs.moveSelection(DirectionY.DOWN); }


    private boolean isState(GameStates state) {
        return model.getStateManager().getState() == state;
    }
}