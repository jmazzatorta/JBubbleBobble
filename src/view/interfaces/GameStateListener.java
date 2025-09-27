package view.interfaces;

import java.util.Stack;

public interface GameStateListener {

    /**
     * Chiamato quando un nuovo livello sta per iniziare.
     * La View userà lo stack di righe per creare un'animazione di transizione.
     * @param nextLevelRows Lo stack di dati delle tile per il nuovo livello.
     */
    void onLevelChangeStart(Stack<int[]> nextLevelRows);

    /**
     * Chiamato quando il gioco passa a uno stato diverso (es. GameOver, Victory).
     * @param newState Il nuovo stato del gioco.
     */
    void onGameStateChanged(model.states.GameStates newState);
}