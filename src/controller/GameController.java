package controller;

import model.GameModel;
import view.GameFrame;
import view.interfaces.EntityListener; 
import static constants.Constants.*;

public class GameController implements Runnable{

    private final GameModel model;
    private final GameFrame view;
    private volatile boolean running;
    
    Thread gameThread;

    public GameController(GameModel model, GameFrame view) {
        this.model = model;
        this.view = view;
        this.running = false;
    }

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

    private void updateModel() {
        model.update();
    }

    private void updateView() {
        view.update();
    }
    
    private void render() {
        view.repaint();
    }

    public void stopGame() {
        running = false;
    }

    public void resetGame() {
        model.getLevelManager().resetGame();
        model.getItemsManager().reset();
        model.getBubblesManager().reset();
        model.getProjectilesManager().reset();
        model.getEnemiesManager().reset();
        model.getPlayersManager().reset();
    }

}