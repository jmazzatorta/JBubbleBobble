package view;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import controller.inputs.InputController;
import model.GameModel;
import model.interfaces.Updatable;
import model.states.GameStates;
import model.utils.MinorEvent;
import view.interfaces.EntityListener;
import view.interfaces.GameStateListener;
import view.panels.*; 


@SuppressWarnings("serial")
public class GameFrame extends JFrame implements EntityListener, GameStateListener {

    private CardLayout cardLayout;
    private JPanel cardPanel; 
    
   
    private PlayingPanel playingPanel;
    private StatsPanel statsPanel;
    private IntroPanel introPanel;
    private GameOverPanel gameOverPanel;
    private VictoryPanel victoryPanel;
    
    private GameStates currentState;

   
    public GameFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("JBubbleBobble");
        setLocationRelativeTo(null);
        
    }

    
    public void init(GameModel model, InputController inputController) {
       
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        setupKeyBindings(inputController);

        playingPanel = new PlayingPanel(model);
        statsPanel = new StatsPanel(model);
        introPanel = new IntroPanel(model);
        gameOverPanel = new GameOverPanel(model);
        victoryPanel = new VictoryPanel(model);
        
        this.currentState = GameStates.INTRO;

        cardPanel.add(introPanel, GameStates.INTRO.toString());
        cardPanel.add(playingPanel, GameStates.PLAYING.toString());
        cardPanel.add(gameOverPanel, GameStates.GAMEOVER.toString());
        cardPanel.add(statsPanel, GameStates.STATS_MANAGEMENT.toString());
        cardPanel.add(victoryPanel, GameStates.VICTORY.toString());

        setContentPane(cardPanel);
        pack();
        setVisible(true);
    }
    
    
    public void update() {
        switch (currentState) {
            case INTRO:
                introPanel.update();
                break;
            case PLAYING:
                playingPanel.update();
                break;
            case STATS_MANAGEMENT :
            	statsPanel.update();
            	break;
            // continuare
            default:
                break;
        }
    }
    
    
    private void setupKeyBindings(InputController controller) {
        InputMap inputMap = this.cardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.cardPanel.getActionMap();

        Runnable emptyAction = () -> {}; 
        
        bindKey(inputMap, actionMap, "ENTER",      controller::handleConfirm,   emptyAction);
        bindKey(inputMap, actionMap, "ESCAPE",     controller::handleCancel,    emptyAction);
        bindKey(inputMap, actionMap, "BACK_SPACE", controller::handleDeleteChar,emptyAction);
        bindKey(inputMap, actionMap, "PLUS",       controller::addPlayer,       emptyAction);
        bindKey(inputMap, actionMap, "ADD",        controller::addPlayer,       emptyAction); 

        bindKey(inputMap, actionMap, "A",     controller::p1_moveLeft_press,   controller::p1_moveLeft_release);
        bindKey(inputMap, actionMap, "D",     controller::p1_moveRight_press,  controller::p1_moveRight_release);
        bindKey(inputMap, actionMap, "W",     controller::p1_attack_press,     controller::p1_attack_release);
        bindKey(inputMap, actionMap, "SPACE", controller::p1_jump,             emptyAction);
        
        bindKey(inputMap, actionMap, "J",     controller::p2_moveLeft_press,   controller::p2_moveLeft_release);
        bindKey(inputMap, actionMap, "L",     controller::p2_moveRight_press,  controller::p2_moveRight_release);
        bindKey(inputMap, actionMap, "I",     controller::p2_attack_press,     controller::p2_attack_release);
        bindKey(inputMap, actionMap, "P", controller::p2_jump,             emptyAction); 

        bindKey(inputMap, actionMap, "LEFT",  controller::moveSelectionLeft,  emptyAction);
        bindKey(inputMap, actionMap, "RIGHT", controller::moveSelectionRight, emptyAction);
        bindKey(inputMap, actionMap, "UP",    controller::moveSelectionUp,    emptyAction);
        bindKey(inputMap, actionMap, "DOWN",  controller::moveSelectionDown,  emptyAction);

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (char c : chars.toCharArray()) {
            Action typeAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.handleTypedChar(e.getActionCommand().charAt(0));
                }
            };

            inputMap.put(KeyStroke.getKeyStroke(c), "type_" + c);
            inputMap.put(KeyStroke.getKeyStroke(Character.toLowerCase(c)), "type_" + c);
            actionMap.put("type_" + c, typeAction);
        }
    }
    
    private void bindKey(InputMap im, ActionMap am, String key, Runnable onPressed, Runnable onReleased) {
        Action pressAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPressed.run();
            }
        };
        
        Action releaseAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onReleased.run();
            }
        };
        
        im.put(KeyStroke.getKeyStroke(key), key + "_pressed");
        am.put(key + "_pressed", pressAction);
        
        im.put(KeyStroke.getKeyStroke("released " + key), key + "_released");
        am.put(key + "_released", releaseAction);
    }

 

    @Override
    public void onGameStateChanged(GameStates newState) {
    	this.currentState = newState;
        cardLayout.show(cardPanel, newState.toString());
    }

    @Override
    public <T> void onEntityAdded(T entity) {
        if (playingPanel != null && entity instanceof Updatable) {
            playingPanel.onEntityAdded((Updatable) entity);
        }
    }

    @Override
    public <T> void onEntityRemoved(T entity) {
        if (playingPanel != null && entity instanceof Updatable) {
            playingPanel.onEntityRemoved((Updatable) entity);
        }
    }

    @Override
    public void onMinorEvent(MinorEvent event) {
        if (playingPanel != null) {
            playingPanel.onMinorEvent(event);
        }
    }
}