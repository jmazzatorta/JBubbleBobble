package model;

import model.ai.EnemyAIService;
import model.interfaces.Updatable;
import model.managers.*;
import model.states.StateManager;
import model.users.SavingsManager;
import model.users.UserDatas;
import model.utils.MinorEvent;
import view.interfaces.EntityListener;
import view.interfaces.GameStateListener;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private final StateManager stateManager;
    private final PlayersManager playersManager;
    private final BubblesManager bubblesManager;
    private final TilesManager tilesManager;
    private final ItemsManager itemsManager;
    private final EnemiesManager enemiesManager;
    private final MinorEventsManager minorEventsManager;
    private final LevelManager levelManager;
    private final ProjectilesManager projectilesManager;
    private final StatisticsManager statisticsManager;
    private final AudioManager audioManager;
    private final EnemyAIService aiService;
    private final UserDatas userDatas;
    private final SavingsManager savingsManager;

    private EntityListener entityListener;
    private final List<GameStateListener> gameStateListeners;
    
    private GameModel(GameModelBuilder builder) {
        this.stateManager = builder.stateManager;
        this.playersManager = builder.playersManager;
        this.bubblesManager = builder.bubblesManager;
        this.tilesManager = builder.tilesManager;
        this.itemsManager = builder.itemsManager;
        this.enemiesManager = builder.enemiesManager;
        this.minorEventsManager = builder.minorEventsManager;
        this.levelManager = builder.levelManager;
        this.projectilesManager = builder.projectilesManager;
        this.statisticsManager = builder.statisticsManager;
        this.audioManager = builder.audioManager;
        this.aiService = builder.aiService;
        this.userDatas = builder.userDatas;
        this.savingsManager = builder.savingsManager;
        this.gameStateListeners = new ArrayList<>();
    }

    public static GameModelBuilder newBuilder() {
        return new GameModelBuilder();
    }
    
    public void update() {

        switch (stateManager.getState()) {
            case PLAYING -> {
                levelManager.update();
                itemsManager.update();
                playersManager.update();
                bubblesManager.update();
                enemiesManager.update();
                projectilesManager.update();
                tilesManager.update();
            }
            case STATS_MANAGEMENT -> statisticsManager.update();
            default -> { /* Stati statici */ }
        }
    }
    
    public void setEntityListener(EntityListener listener) { this.entityListener = listener; }
    public void notifyEntityAdded(Updatable entity) { if (entityListener != null) entityListener.onEntityAdded(entity); }
    public void notifyEntityRemoved(Updatable entity) { if (entityListener != null) entityListener.onEntityRemoved(entity); }
    public void notifyMinorEvent(MinorEvent event) { if (entityListener != null) entityListener.onMinorEvent(event); }
    
    public void addGameStateListener(GameStateListener listener) {
        this.gameStateListeners.add(listener);
    }
    
    public void notifyLevelChangeStart(java.util.Stack<int[]> nextLevelRows) {
        for (GameStateListener listener : gameStateListeners) {
            listener.onLevelChangeStart(nextLevelRows);
        }
    }
    
    public void notifyGameStateChanged(model.states.GameStates newState) {
        for (GameStateListener listener : gameStateListeners) {
            listener.onGameStateChanged(newState);
        }
    }
    
    public StateManager getStateManager() { return stateManager; }
    public PlayersManager getPlayersManager() { return playersManager; }
    public StatisticsManager getStatisticsManager() { return statisticsManager; }
    public MinorEventsManager getMinorEventsManager() { return minorEventsManager; }
    public TilesManager getTilesManager() { return tilesManager; }
    public ItemsManager getItemsManager() { return itemsManager; }
    public LevelManager getLevelManager() { return levelManager; }
    public BubblesManager getBubblesManager() { return bubblesManager; }
    public EnemiesManager getEnemiesManager() { return enemiesManager; }
    public ProjectilesManager getProjectilesManager() { return projectilesManager; }
    public AudioManager getAudioManager() { return audioManager; }
    public EnemyAIService getAiService() { return aiService; }
    public UserDatas getUserDatas() { return userDatas; }
    public SavingsManager getSavingsManager() { return savingsManager; }
    

    // --- CLASSE BUILDER AGGIORNATA ---
    public static class GameModelBuilder {
    
        private StateManager stateManager;
        private PlayersManager playersManager;
        private BubblesManager bubblesManager;
        private TilesManager tilesManager;
        private ItemsManager itemsManager;
        private EnemiesManager enemiesManager;
        private MinorEventsManager minorEventsManager;
        private LevelManager levelManager;
        private ProjectilesManager projectilesManager;
        private StatisticsManager statisticsManager;
        private AudioManager audioManager;
        private EnemyAIService aiService;
        private UserDatas userDatas;
        private SavingsManager savingsManager;

        private GameModelBuilder() {}

        public GameModel build() {
            this.audioManager = new AudioManager();
            this.tilesManager = new TilesManager();
            this.stateManager = new StateManager();
            this.userDatas = new UserDatas();
            this.savingsManager = new SavingsManager(userDatas);
            this.projectilesManager = new ProjectilesManager(null, tilesManager);
           
            this.minorEventsManager = new MinorEventsManager(null);
            this.itemsManager = new ItemsManager(null, tilesManager);
            this.levelManager = new LevelManager(null, null, null, tilesManager, null, null, minorEventsManager, projectilesManager, stateManager);
            this.playersManager = new PlayersManager(null, null, minorEventsManager, levelManager, tilesManager, audioManager, itemsManager);
            this.aiService = new EnemyAIService(playersManager);
            this.enemiesManager = new EnemiesManager(null, playersManager, levelManager, itemsManager, tilesManager, projectilesManager, aiService, audioManager);
            this.bubblesManager = new BubblesManager(null, tilesManager, minorEventsManager, enemiesManager, projectilesManager);
            this.statisticsManager = new StatisticsManager(playersManager, userDatas, levelManager, stateManager, savingsManager);

            GameModel gameModel = new GameModel(this);
            
            minorEventsManager.setGameModel(gameModel);
            ((BaseManager<?>) projectilesManager).setGameModel(gameModel);
            ((BaseManager<?>) playersManager).setGameModel(gameModel);
            ((BaseManager<?>) itemsManager).setGameModel(gameModel);
            ((BaseManager<?>) enemiesManager).setGameModel(gameModel);
            ((BaseManager<?>) bubblesManager).setGameModel(gameModel);
            
            levelManager.setGameModel(gameModel);
            
            levelManager.setPlayersManager(playersManager);
            levelManager.setItemsManager(itemsManager);
            levelManager.setEnemiesManager(enemiesManager);
            levelManager.setBubblesManager(bubblesManager);
            playersManager.setBubblesManager(bubblesManager);
            itemsManager.setPlayersManager(playersManager);
            stateManager.setGameModel(gameModel);
            
            return gameModel;
        }
    }
}