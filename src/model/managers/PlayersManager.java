package model.managers;

import java.util.List;
import java.util.stream.Collectors;

import model.GameModel;
import model.entities.players.Player;

public class PlayersManager extends BaseManager<Player> {

    private final MinorEventsManager minorEventsManager;
    private final LevelManager levelManager;
    private final TilesManager tilesManager;
    private final AudioManager audioManager;
    private final ItemsManager itemsManager;
    private BubblesManager bubblesManager;

    public PlayersManager(GameModel gameModel, BubblesManager bubblesManager, MinorEventsManager minorEventsManager,
                          LevelManager levelManager, TilesManager tilesManager, AudioManager audioManager, ItemsManager itemsManager) {
        super(gameModel);
        this.bubblesManager = bubblesManager;
        this.minorEventsManager = minorEventsManager;
        this.levelManager = levelManager;
        this.tilesManager = tilesManager;
        this.audioManager = audioManager;
        this.itemsManager = itemsManager;
    }
    
    public void setBubblesManager(BubblesManager bubblesManager) {
    	this.bubblesManager = bubblesManager;
    }
    
    public void setItemsManager(ItemsManager itemsManager2) {
		
	}


    public void addPlayer(String name) {
        if (entities.size() < 2) {
            Player player = new Player(name, bubblesManager, minorEventsManager, levelManager, tilesManager, audioManager, itemsManager);
            super.add(player);
        }
    }

    public Player getP1() {
        return entities.isEmpty() ? null : entities.get(0);
    }

    public Player getP2() {
        return entities.size() < 2 ? null : entities.get(1);
    }

    @Override
    public void update() {
    	super.update();
    }

    public List<Player> getActivePlayers() {
        return entities.stream()
                .filter(Player::hasLives)
                .collect(Collectors.toList());
    }

    @Override
    public void reset() {
        super.reset(); // 
        addPlayer("bub");
    }

    // --- Metodi delegati ---
    public void takePosition() {
        entities.forEach(Player::takePosition);
    }

    public void resetPowerUps() {
        entities.forEach(Player::resetPowerUps);
    }

    public boolean hasTwoPlayers() {
        return entities.size() > 1;
    }

    public boolean playersAlive() {
        return entities.stream().anyMatch(Player::hasLives);
    }

    public boolean areDying() {
        return entities.stream().filter(Player::hasLives).allMatch(Player::isDying);
    }

    public boolean inSpawnPosition() {
        return entities.stream().filter(Player::hasLives).allMatch(Player::inSpawnPosition);
    }
    
    public boolean isPositioningComplete() {
        return entities.stream().noneMatch(e -> e.isPositioning());
    }

	
}
