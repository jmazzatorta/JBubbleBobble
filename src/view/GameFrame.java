package view;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.Stack;

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
import view.panels.*; // Importa tutti i pannelli


public class GameFrame extends JFrame implements EntityListener, GameStateListener {


    // Campi per le dipendenze e per i pannelli interni.
    private CardLayout cardLayout;
    private JPanel cardPanel; // Un JPanel che gestisce il CardLayout
    
    // Riferimenti ai pannelli specifici per inoltrare gli eventi
    private PlayingPanel playingPanel;
    private StatsPanel statsPanel;
    private IntroPanel introPanel;
    private GameOverPanel gameOverPanel;
    private VictoryPanel victoryPanel;
    
    private GameStates currentState;

    /**
     * --- MODIFICATO ---
     * Il costruttore ora è pubblico e si occupa solo di impostare
     * le proprietà base della finestra, senza logica complessa.
     */
    public GameFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("JBubbleBobble");
        setLocationRelativeTo(null);
        
    }

    /**
     * --- NUOVO ---
     * Metodo di inizializzazione per l'iniezione delle dipendenze.
     * Viene chiamato dal 'main' dopo la creazione dell'oggetto.
     * @param model il modello di gioco, per passarlo ai pannelli.
     * @param keyHandler il gestore degli input da collegare alla finestra.
     */
    public void init(GameModel model, InputController inputController) {
        // Inizializza il layout e il pannello contenitore
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        setupKeyBindings(inputController);

        // --- MODIFICA CHIAVE ---
        // Creiamo le istanze dei pannelli QUI, passando il model.
        // Niente più Singleton!
        playingPanel = new PlayingPanel(model);
        statsPanel = new StatsPanel(model);
        introPanel = new IntroPanel(model);
        gameOverPanel = new GameOverPanel(model);
        victoryPanel = new VictoryPanel(model);
        
        this.currentState = GameStates.INTRO;

        // Aggiungiamo i pannelli (le "carte") al contenitore
        cardPanel.add(introPanel, GameStates.INTRO.toString());
        cardPanel.add(playingPanel, GameStates.PLAYING.toString());
        cardPanel.add(gameOverPanel, GameStates.GAMEOVER.toString());
        cardPanel.add(statsPanel, GameStates.STATS_MANAGEMENT.toString());
        cardPanel.add(victoryPanel, GameStates.VICTORY.toString());

        // Finalizza la finestra
        setContentPane(cardPanel);
        pack();
        setVisible(true);
    }
    
    /**
     * Aggiorna lo stato della View.
     * Questo metodo viene chiamato 60 volte al secondo dal GameController.
     * Delega l'aggiornamento solo al pannello attualmente visibile.
     */
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
    
    
    // --- IMPLEMENTAZIONE KEY BINDINGS --- 
    
    private void setupKeyBindings(InputController controller) {
        InputMap inputMap = this.cardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.cardPanel.getActionMap();

        Runnable emptyAction = () -> {}; // Azione vuota per i tasti non gestiti in rilascio
        
        // --- Tasti Azione Generali ---
        bindKey(inputMap, actionMap, "ENTER",      controller::handleConfirm,   emptyAction);
        bindKey(inputMap, actionMap, "ESCAPE",     controller::handleCancel,    emptyAction);
        bindKey(inputMap, actionMap, "BACK_SPACE", controller::handleDeleteChar,emptyAction);
        bindKey(inputMap, actionMap, "PLUS",       controller::addPlayer,       emptyAction);
        bindKey(inputMap, actionMap, "ADD",        controller::addPlayer,       emptyAction); // Per il + del tastierino numerico

        // --- Tasti Movimento P1 (WASD) ---
        bindKey(inputMap, actionMap, "A",     controller::p1_moveLeft_press,   controller::p1_moveLeft_release);
        bindKey(inputMap, actionMap, "D",     controller::p1_moveRight_press,  controller::p1_moveRight_release);
        bindKey(inputMap, actionMap, "W",     controller::p1_attack_press,     controller::p1_attack_release);
        bindKey(inputMap, actionMap, "SPACE", controller::p1_jump,             emptyAction);
        
     // --- Tasti Movimento P2 (J, L, I, SHIFT) ---
        bindKey(inputMap, actionMap, "J",     controller::p2_moveLeft_press,   controller::p2_moveLeft_release);
        bindKey(inputMap, actionMap, "L",     controller::p2_moveRight_press,  controller::p2_moveRight_release);
        bindKey(inputMap, actionMap, "I",     controller::p2_attack_press,     controller::p2_attack_release);
        bindKey(inputMap, actionMap, "U_GRAVE", controller::p2_jump,             emptyAction); // Il salto rimane SHIFT

        // --- Tasti Movimento Stats (Frecce) ---
        bindKey(inputMap, actionMap, "LEFT",  controller::moveSelectionLeft,  emptyAction);
        bindKey(inputMap, actionMap, "RIGHT", controller::moveSelectionRight, emptyAction);
        bindKey(inputMap, actionMap, "UP",    controller::moveSelectionUp,    emptyAction);
        bindKey(inputMap, actionMap, "DOWN",  controller::moveSelectionDown,  emptyAction);

        // --- Gestione Speciale per l'Input Testuale ---
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (char c : chars.toCharArray()) {
            // Crea un'azione che "cattura" il carattere premuto
            Action typeAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.handleTypedChar(e.getActionCommand().charAt(0));
                }
            };
            // Associa sia il tasto maiuscolo che minuscolo
            inputMap.put(KeyStroke.getKeyStroke(c), "type_" + c);
            inputMap.put(KeyStroke.getKeyStroke(Character.toLowerCase(c)), "type_" + c);
            actionMap.put("type_" + c, typeAction);
        }
    }
    
    // Metodo helper per ridurre la verbosità
    private void bindKey(InputMap im, ActionMap am, String key, Runnable onPressed, Runnable onReleased) {
        // Azione per la pressione
        Action pressAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPressed.run();
            }
        };
        
        // Azione per il rilascio
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


    // --- IMPLEMENTAZIONE DEI LISTENER --- 

    @Override
    public void onGameStateChanged(GameStates newState) {
    	this.currentState = newState;
        cardLayout.show(cardPanel, newState.toString());
    }

    @Override
    public void onLevelChangeStart(Stack<int[]> nextLevelRows) {
        // GameFrame non gestisce questo evento, lo inoltra al pannello di gioco.
        if (playingPanel != null) {
            //playingPanel.onLevelChangeStart(nextLevelRows);
        }
    }

    @Override
    public <T> void onEntityAdded(T entity) {
        // Inoltra l'evento al pannello di gioco.
        if (playingPanel != null && entity instanceof Updatable) {
            playingPanel.onEntityAdded((Updatable) entity);
        }
    }

    @Override
    public <T> void onEntityRemoved(T entity) {
        // Inoltra l'evento al pannello di gioco.
        if (playingPanel != null && entity instanceof Updatable) {
            playingPanel.onEntityRemoved((Updatable) entity);
        }
    }

    @Override
    public void onMinorEvent(MinorEvent event) {
        // Inoltra l'evento al pannello di gioco.
        if (playingPanel != null) {
            playingPanel.onMinorEvent(event);
        }
    }
}