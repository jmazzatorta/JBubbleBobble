package controller;

import model.GameModel;
import view.GameFrame;
import view.interfaces.EntityListener; 
import static constants.Constants.*;

/**
 * Il coordinatore del gioco.
 * Riceve Model e View dall'esterno (Dependency Injection)
 * e il suo unico scopo è gestire il game loop e il ciclo di vita del gioco.
 */
public class GameController implements Runnable{

    private final GameModel model;
    private final GameFrame view;
    private volatile boolean running;
    
    Thread gameThread;

    /**
     * Il costruttore riceve le sue dipendenze (Model e View)
     * dall'esterno, invece di crearle da solo.
     * @param model il modello di dati del gioco.
     * @param view la finestra principale del gioco.
     */
    public GameController(GameModel model, GameFrame view) {
        this.model = model;
        this.view = view;
        this.running = false;
    }

    /** Avvia il gioco */
    public void startGame() {
        running = true;
        model.getLevelManager().initFirstLevel();
        
        gameThread = new Thread(this);
		gameThread.start();
    }
    
    
	@Override
	public void run() {
		
		double drawInterval= 1_000_000_000/FPS;
		double nextDrawTime= System.nanoTime() + drawInterval;
		
		while(gameThread != null && running) {
			long currentTime = System.nanoTime();

            updateModel();
            updateView(); 
            render(); 
            
            try {
				double remainingTime= nextDrawTime - currentTime;
				remainingTime= remainingTime/1000000;
				
				if (remainingTime < 0) remainingTime=0;
				
				Thread.sleep((long) remainingTime);
				
				nextDrawTime+= drawInterval;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}

    /** Aggiorna il modello */
    private void updateModel() {
        model.update();
    }
    
    /** Aggiorna la view */
    private void updateView() {
        view.update();
    }
    
    /** Ridisegna la view */
    private void render() {
        view.repaint();
    }

    /** Ferma il gioco */
    public void stopGame() {
        running = false;
    }

    /** Reset completo del gioco */
    public void resetGame() {
        model.getLevelManager().resetGame();
        model.getItemsManager().reset();
        model.getBubblesManager().reset();
        model.getProjectilesManager().reset();
        model.getEnemiesManager().reset();
        model.getPlayersManager().reset();
    }

}