package model.managers;

import model.GameModel;
import model.ai.EnemyAIService;
import model.entities.enemies.*;

import java.util.ArrayList;
import java.util.List;

public class EnemiesManager extends BaseManager<Enemy> {

    private final PlayersManager playersManager;
    private final LevelManager levelManager;
    private final ItemsManager itemsManager;
    private final TilesManager tilesManager;
    private final ProjectilesManager projectilesManager;
    private final EnemyAIService aiService;
    private final AudioManager audioManager;

    private final List<Enemy> trappedEnemies;
    private boolean angry;

    public EnemiesManager(GameModel gameModel,
                          PlayersManager playersManager,
                          LevelManager levelManager,
                          ItemsManager itemsManager,
                          TilesManager tilesManager,
                          ProjectilesManager projectilesManager,
                          EnemyAIService aiService, 
                          AudioManager audioManager) {
        super(gameModel);
        this.playersManager = playersManager;
        this.levelManager = levelManager;
        this.itemsManager = itemsManager;
        this.tilesManager = tilesManager;
        this.projectilesManager = projectilesManager;
        this.aiService = aiService;
        this.audioManager = audioManager;
        this.trappedEnemies = new ArrayList<>();
    }

    @Override
    public void update() {
        super.update(); 

        boolean canEnemiesMove = !playersManager.areDying() 
        		&& !levelManager.isInitialising();
        
        
        for (Enemy enemy : entities) { 
            enemy.canMove(canEnemiesMove);
            enemy.update();
        }

        for (Enemy enemy : entities) {
            if (enemy.isReadyToRemove()) {
                remove(enemy);
            }
        }
    }

    public void createEnemy(String enemyName, int x, int y) {
        Enemy enemy = switch (enemyName) {
            case "Zenchan" -> new ZenChan(x, y, tilesManager, playersManager, aiService, audioManager, itemsManager);
            case "Monsta" -> new Monsta(x, y, tilesManager, audioManager, itemsManager);
            case "Maita" -> new Maita(x, y, tilesManager, playersManager, projectilesManager, aiService, audioManager, itemsManager);
            default -> null;
        };

        if (enemy != null) {
            add(enemy); 
        }

    }

    public void exterminate() {
        new ArrayList<>(entities).forEach(Enemy::die);
    }

    public void getMad() {
        if (!angry) {
            entities.forEach(Enemy::getMad);
            angry = true;
        }
    }

    public void trapEnemy(Enemy enemy) {
        enemy.trapped();
        remove(enemy); 
        trappedEnemies.add(enemy);
    }

    public List<Enemy> getActiveEnemies() {
        return new ArrayList<>(entities);
    }

    @Override
    public void reset() {
        super.reset(); 
        trappedEnemies.clear();
        angry = false;
    }

    public boolean noMoreEnemies() {
        return entities.isEmpty() && trappedEnemies.isEmpty() && !hasPendingAdditions();
    }

    public void canMove(boolean b) {
        entities.forEach(enemy -> enemy.canMove(b));
    }

    public boolean inSpawnPosition() {
        return entities.stream().allMatch(Enemy::inSpawnPosition);
    }

    public void takePosition() {
        entities.forEach(Enemy::takePosition);
    }
    
    public boolean isPositioningComplete() {
        return entities.stream().noneMatch(e -> e.isPositioning());
    }

    
    public void releaseTrappedEnemy(Enemy enemy, int x, int y, boolean wasPopped) {
        trappedEnemies.remove(enemy);

        if (wasPopped) {
            enemy.setX(x);
            enemy.setY(y);
            enemy.die();
            add(enemy);
        } else {
            enemy.getMad();
            add(enemy); 
        }
    }
}
