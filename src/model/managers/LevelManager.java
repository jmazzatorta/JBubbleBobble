package model.managers;

import model.GameModel;
import model.bubbles.SpecialBubbleType;
import model.states.GameStates;
import model.states.StateManager;
import static constants.Constants.*;

public class LevelManager {
	
	private GameModel gameModel;

    private ItemsManager items;
    private EnemiesManager enemies;
    private PlayersManager players;
    private BubblesManager bubbles;

    private final TilesManager tiles;
    private final MinorEventsManager minorEvents;
    private final ProjectilesManager projectiles;
    private final StateManager state;

    private boolean initialisingLevel = false;
    private boolean angryEnemies;
    private int angryEnemiesTC = 30 * FPS;

    private int currentLevel = 1;
    private final int LEVELMAX = 8;

    private int endLevelTC;
    private final long TICKS_TO_END = 7 * FPS;

    private boolean clock, dynamite;
    private int clockTC, dynamiteTC;

    public LevelManager(GameModel gameModel, ItemsManager items, EnemiesManager enemies, TilesManager tiles,
                        PlayersManager players, BubblesManager bubbles, MinorEventsManager minorEvents,
                        ProjectilesManager projectiles, StateManager state) {
    	this.gameModel = gameModel;
        this.items = items;
        this.enemies = enemies;
        this.tiles = tiles;
        this.players = players;
        this.bubbles = bubbles;
        this.minorEvents = minorEvents;
        this.projectiles = projectiles;
        this.state = state;
    }

    // --- Dependency injection setters ---
    public void setEnemiesManager(EnemiesManager enemies) { this.enemies = enemies; }
    public void setItemsManager(ItemsManager items) { this.items = items; }
    public void setPlayersManager(PlayersManager players) { this.players = players; }
    public void setBubblesManager(BubblesManager bubbles) { this.bubbles = bubbles; }
    public void setGameModel(GameModel gameModel) { this.gameModel = gameModel; }
    
    // --- Inizializzazione primo livello ---
    public void initFirstLevel() {
    	
    	players.reset();
    	enemies.reset();

        loadLevelAndPrepareStack(1);

        initialisingLevel = true;
        
        players.takePosition();

        enemies.createEnemy("Zenchan", 13, 10);
        enemies.createEnemy("Zenchan", 15, 10);
        enemies.createEnemy("Zenchan", 17, 10);

        bubbles.setNextSpecialBubble(SpecialBubbleType.NONE);
    }

    private void loadLevelAndPrepareStack(int levelNumber) {
        tiles.loadLevel(levelNumber); // lo stack viene popolato solo qui
    }

    // --- Update ciclo di gioco ---
    public void update() {
        if (initialisingLevel) {
            initialisingUpdate();
        } else {
            playingUpdate();
        }
    }

    private void initialisingUpdate() {
        if (enemies.noMoreEnemies() && !enemies.hasPendingAdditions()) {
            return; 
        }
        
        if (enemies.isPositioningComplete() && players.isPositioningComplete() && tiles.isSlidingDone()) {
            initialisingLevel = false;
            
            items.getFoodRandomizer().updateMap();
            
            items.unlockFactory();
            bubbles.unlockFactory();
        }
    }

    private void playingUpdate() {
        if (!players.playersAlive()) {
            state.setState(GameStates.GAMEOVER);
            //sound
            
            endLevel();
            return;
        }

        if (enemies.noMoreEnemies()) {
            endLevelTC++;
            if (endLevelTC == (int) TICKS_TO_END/2) {
            	bubbles.popAll();
            }
            if (endLevelTC > TICKS_TO_END) {
                endLevelTC = 0;
                endLevel();
            }
        } else {
        	
            handleAngryEnemies();
            handleClockEffect();
            handleDynamiteEffect();
        }
    }

    private void handleAngryEnemies() {
        if (!angryEnemies) {
            angryEnemiesTC--;
            if (angryEnemiesTC <= 0) {
                enemies.getMad();
                angryEnemies = true;
                angryEnemiesTC = 30 * FPS;
            }
        }
    }

    private void handleClockEffect() {
        if (clock) {
            clockTC++;
            if (clockTC > FPS * 10) {
                enemies.canMove(true);
                clock = false;
                clockTC = 0;
            }
        }
    }

    private void handleDynamiteEffect() {
        if (!dynamite) return;

        dynamiteTC++;
        if (dynamiteTC == 0) {
            minorEvents.createEvent(-1, -1, "DYNAMITE");
        } else if (dynamiteTC == 40) {
            enemies.exterminate();
        } else if (dynamiteTC == 80) {
            dynamite = false;
            dynamiteTC = 0;
        }
    }

    // --- Fine livello ---
    public void endLevel() {
        items.reset();
        items.blockFactory();

        bubbles.reset();
        bubbles.blockFactory();

        projectiles.reset();

        players.resetPowerUps();

        if (state.getState() == GameStates.PLAYING) {
        	
        	enemies.reset();
        	
            if (currentLevel < LEVELMAX) {
                nextLevel();
            } else {
                state.setState(GameStates.VICTORY);
            }
        } else if (state.getState() == GameStates.GAMEOVER) {
            enemies.reset();
        }
    }

    // --- Passaggio al livello successivo ---
    public void nextLevel() {
        currentLevel++;
        loadLevelAndPrepareStack(currentLevel);
        tiles.slideToNextLevel();

        players.takePosition();
        
        initialisingLevel = true;

        angryEnemies = false;
        angryEnemiesTC = 30 * FPS;

        // Creazione nemici e impostazione special bubble per ogni livello
        switch (currentLevel) {
            case 2:
                enemies.createEnemy("Zenchan", 14, 5);
                enemies.createEnemy("Zenchan", 16, 5);
                enemies.createEnemy("Zenchan", 11, 10);
                enemies.createEnemy("Zenchan", 19, 10);
                bubbles.setNextSpecialBubble(SpecialBubbleType.FIRE);
                break;
            case 3:
                enemies.createEnemy("Zenchan", 10, 5);
                enemies.createEnemy("Zenchan", 20, 5);
                enemies.createEnemy("Zenchan", 6, 10);
                enemies.createEnemy("Zenchan", 24, 10);
                bubbles.setNextSpecialBubble(SpecialBubbleType.FIRE);
                break;
            case 4:
                enemies.createEnemy("Zenchan", 5, 6);
                enemies.createEnemy("Zenchan", 25, 6);
                enemies.createEnemy("Zenchan", 7, 10);
                enemies.createEnemy("Zenchan", 23, 10);
                enemies.createEnemy("Zenchan", 9, 20);
                enemies.createEnemy("Zenchan", 21, 20);
                bubbles.setNextSpecialBubble(SpecialBubbleType.THUNDER);
                break;
            case 5:
                enemies.createEnemy("Zenchan", 18, 5);
                enemies.createEnemy("Zenchan", 16, 10);
                enemies.createEnemy("Zenchan", 14, 15);
                enemies.createEnemy("Zenchan", 12, 20);
                bubbles.setNextSpecialBubble(SpecialBubbleType.THUNDER);
                break;
            case 6:
                enemies.createEnemy("Zenchan", 24, 5);
                enemies.createEnemy("Zenchan", 26, 15);
                enemies.createEnemy("Maita", 20, 10);
                enemies.createEnemy("Maita", 15, 25);
                bubbles.setNextSpecialBubble(SpecialBubbleType.FIRE);
                break;
            case 7:
                enemies.createEnemy("Maita", 2, 5);
                enemies.createEnemy("Maita", 28, 5);
                enemies.createEnemy("Maita", 4, 10);
                enemies.createEnemy("Maita", 26, 10);
                bubbles.setNextSpecialBubble(SpecialBubbleType.FIRE);
                break;
            case 8:
                enemies.createEnemy("Maita", 8, 8);
                enemies.createEnemy("Maita", 22, 8);
                enemies.createEnemy("Monsta", 8, 13);
                enemies.createEnemy("Monsta", 22, 13);
                bubbles.setNextSpecialBubble(SpecialBubbleType.THUNDER);
                break;
        }
    }

    public void setDynamite() { dynamite = true; }
    public void setClock() { clock = true; enemies.canMove(false); }
    public boolean isInitialising() { return initialisingLevel; }
    public int getLevel() { return currentLevel; }

    public void resetGame() {
        currentLevel = 1;
        players.reset();
    }
}
