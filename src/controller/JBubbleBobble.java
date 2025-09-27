package controller;

import javax.swing.SwingUtilities;

import controller.inputs.GameOverInputs;
import controller.inputs.InputController;
import controller.inputs.IntroInputs;
import controller.inputs.PlayingInputs;
import controller.inputs.StatsInputs;
import controller.inputs.VictoryInputs;
import model.GameModel;
import view.GameFrame;


public class JBubbleBobble {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            GameModel model = GameModel.newBuilder().build();
            
            PlayingInputs playingInputs = new PlayingInputs(model);
            IntroInputs introInputs = new IntroInputs(model); 
            GameOverInputs gameOverInputs = new GameOverInputs(model); 
            VictoryInputs victoryInputs = new VictoryInputs(model);   
            StatsInputs statsInputs = new StatsInputs(model);    
            
            InputController inputController = new InputController(model, playingInputs, introInputs, 
                    gameOverInputs, victoryInputs, statsInputs);

            GameFrame view = new GameFrame();
            GameController controller = new GameController(model, view);
            
            

            // --- 2. COLLEGAMENTO DEI COMPONENTI (WIRING) ---

            view.init(model, inputController);

            model.setEntityListener(view);
            
            model.addGameStateListener(view);
            model.addGameStateListener(model.getAudioManager());



            // --- 3. AVVIO DEL GIOCO ---

            controller.startGame();
        });
    }
}