package model.projectiles;

import static constants.Constants.*;
import model.utils.DirectionX;

public class MaitaProjectile extends Projectile {

    private int explodingTC;

    public MaitaProjectile(int x, int y, DirectionX direction, int[][] tileMap) {
        super(x, y, direction, tileMap);
        this.speed = 3;
        this.hitbox = new java.awt.Rectangle(10, 0, TILESIZE * 2, TILESIZE);
    }

    @Override
    public void update() {
        if (exploding) {
            explodingTC++;
            if (explodingTC >= 30) readyToRemove = true;
            return;
        }

        collisionX = false;
        updateCoordinates();
        checkTilesCollisions();

        if (collisionX) explode();
        else {
            switch (direction) {
                case LEFT: x -= speed; break;
                case RIGHT: x += speed; break;
            }
        }
    }

    @Override
    protected void checkTilesCollisions() {
        int tile1 = tileMap[(int) projectileTopRow][(int) projectileLeftCol];
        int tile2 = tileMap[(int) projectileTopRow][(int) projectileRightCol];
        if (tile1 != 0 || tile2 != 0) collisionX = true;
    }

    public String getTypeData() { return "MAITA"; }
    public String getActionData() { return exploding ? "EXPLODING" : "MOVING"; }
    public String getDirectionData() { return null; }
    public boolean isFlashing() { return false; }
}
