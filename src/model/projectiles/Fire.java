package model.projectiles;

import model.utils.DirectionX;
import model.managers.ProjectilesManager;

import static constants.Constants.*;

public class Fire extends Projectile {

    protected Fire root;
    protected boolean falling = true;
    protected boolean collisionFloor = false;

    protected int charges = 2;
    private int propagateTC = 20;
    private int fireTC = 5 * FPS;

    private final ProjectilesManager projectilesManager;

    public Fire(int x, int y, int[][] tileMap, ProjectilesManager projectilesManager) {
        super(x, y, DirectionX.RIGHT, tileMap);
        this.root = this;
        this.projectilesManager = projectilesManager;
        this.speed = 4;
        this.hitbox = new java.awt.Rectangle(10, 0, TILESIZE * 2 - 20, TILESIZE * 2);
    }

    @Override
    public void update() {
        updateCoordinates();
        checkTilesCollisions();

        if (falling) {
            if (collisionFloor) falling = false;
            else y += speed;
        } else {
            if (propagateTC > 0) propagateTC--;
            else if (charges > 0) propagate();

            if (fireTC > 0) fireTC--;
            else readyToRemove = true;
        }
    }

    @Override
    protected void updateCoordinates() {
        projectileLeft = x + hitbox.x;
        projectileRight = projectileLeft + hitbox.width;
        projectileTop = y + hitbox.y;
        projectileBottom = projectileTop + hitbox.height;

        if (falling) projectileBottom += speed;

        projectileLeftCol = projectileLeft / (double) TILESIZE;
        projectileRightCol = projectileRight / (double) TILESIZE;
        projectileTopRow = projectileTop / (double) TILESIZE;
        projectileBottomRow = projectileBottom / (double) TILESIZE;
    }

    @Override
    protected void checkTilesCollisions() {
        int tile1 = tileMap[(int) projectileBottomRow][(int) projectileLeftCol];
        int tile2 = tileMap[(int) projectileBottomRow][(int) projectileRightCol];

        if (tile1 != 0 || tile2 != 0) collisionFloor = true;
    }

    protected void propagate() {
        int row = y / TILESIZE;
        int col = x / TILESIZE;

        if (charges <= 0) return;

        if (col > 0 && tileMap[row][col - 1] == 0 && tileMap[row + 2][col - 1] != 0)
            generateChild(DirectionX.LEFT, charges);
        if (col < MAXSCREENCOL - 1 && tileMap[row][col + 1] == 0 && tileMap[row + 2][col + 1] != 0)
            generateChild(DirectionX.RIGHT, charges);
    }

    protected void generateChild(DirectionX childDirection, int parentCharges) {
        int childX = (childDirection == DirectionX.LEFT) ? x - TILESIZE : x + TILESIZE;
        projectilesManager.spawnFireChild(childX, y, root, childDirection, parentCharges - 1);
    }

    public String getTypeData() { return "FIRE"; }
    public String getActionData() { return null; }
    public String getDirectionData() { return null; }
    public boolean isFlashing() { return false; }
}
