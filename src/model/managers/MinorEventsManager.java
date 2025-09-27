package model.managers;

import model.GameModel;
import model.utils.MinorEvent;

/**
 * MODIFICATO: Ora è una semplice classe di utility.
 * Non estende più BaseManager perché non gestisce una lista di entità.
 * Il suo unico scopo è creare e notificare eventi istantanei.
 */
public class MinorEventsManager {

    private GameModel gameModel; // Mantiene un riferimento al Model per inviare notifiche

    public MinorEventsManager(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * Permette di iniettare il GameModel dopo la costruzione,
     * come richiesto dal GameModelBuilder.
     */
    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * Crea un evento di punteggio e lo notifica immediatamente.
     */
    public void createScoreEvent(int x, int y, String type, String user, int scoredPoints) {
        if (gameModel != null) {
            gameModel.notifyMinorEvent(new MinorEvent(x, y, type, user, scoredPoints));
        }
    }

    /**
     * Crea un evento generico e lo notifica immediatamente.
     */
    public void createEvent(int x, int y, String type) {
        if (gameModel != null) {
            gameModel.notifyMinorEvent(new MinorEvent(x, y, type, null, -1));
        }
    }
    
}