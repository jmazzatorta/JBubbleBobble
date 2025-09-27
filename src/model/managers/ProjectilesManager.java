package model.managers;

import model.GameModel;
import model.bubbles.SpecialBubble;
import model.entities.Entity;
import model.projectiles.*;
import model.utils.DirectionX;

import java.util.ArrayList;
import java.util.List;

public class ProjectilesManager extends BaseManager<Projectile> {


    private final int[][] map;

    public ProjectilesManager(GameModel gameModel, TilesManager tilesManager) {
    	super(gameModel);
        this.entities = new ArrayList<>();
        map = tilesManager.getMap();
    }

    @Override
    public void update() {
        super.update(); // Aggiorna tutti i proiettili

        for (Projectile projectile : entities) {
            if (projectile.isReadyToRemove()) {
                remove(projectile); 
            }
        }
    }

    // --- SPAWN PROIETTILI DA NEMICI ---
    public void spawnProjectileFromEnemy(Entity enemy) {
        if (enemy == null) return;

        DirectionX direction = enemy.getDirection();
        int xShift = (direction == DirectionX.LEFT) ? -5 : 5;

        Projectile projectile = null;

        switch (enemy.getName().toLowerCase()) {
            case "maita":
                projectile = new MaitaProjectile(enemy.getX() + xShift, enemy.getY() - 5, direction, map);
                break;
        }

        if (projectile != null) add(projectile); // 🔥 notifica automatica
    }

    // --- SPAWN PROIETTILI DA BUBBLES ---
    public void spawnProjectileFromBubble(SpecialBubble bubble) {
        if (bubble == null) return;

        Projectile projectile = null;
        String type = bubble.getType().toString();

        switch (type) {
            case "FIRE" -> projectile = new Fire(bubble.getX(), bubble.getY(), map, this);
            case "THUNDER" -> projectile = new Thunder(bubble.getX(), bubble.getY(), map);
        }

        if (projectile != null) add(projectile); // 🔥 notifica automatica
    }

    // --- SPAWN FIRE CHILD ---
    public void spawnFireChild(int x, int y, Fire root, DirectionX direction, int charges) {
        if (root == null || direction == null) return;
        FireChild fireChild = new FireChild(x, y, root, direction, charges, map, this);
        add(fireChild); // 🔥 notifica automatica
    }

    // --- RESET ---
    @Override
    public void reset() {
        super.reset(); // 🔥 notifica automatica clearing
    }

    // --- GETTERS ---
    public List<Projectile> getProjectiles() {
        return getEntities();
    }
}
