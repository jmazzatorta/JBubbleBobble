package model.projectiles;

import static constants.Constants.*;
import model.managers.ProjectilesManager;
import model.utils.DirectionX;

public class FireChild extends Fire {

    private final DirectionX childDirection;

    public FireChild(int x, int y, Fire root, DirectionX childDirection, int charges, int[][] tileMap, ProjectilesManager pm) {
        super(x, y, tileMap, pm);
        this.root = root;
        this.charges = charges;
        this.childDirection = childDirection;
        this.falling = false;
    }

    @Override
    protected void propagate() {
        int row = y / TILESIZE;
        int col = x / TILESIZE;

        if (charges <= 0) return;

        switch (childDirection) {
            case LEFT:
                if (col > 0 && tileMap[row][col - 1] == 0 && tileMap[row + 2][col - 1] != 0)
                    generateChild(DirectionX.LEFT, charges);
                break;
            case RIGHT:
                if (col < MAXSCREENCOL - 1 && tileMap[row][col + 1] == 0 && tileMap[row + 2][col + 1] != 0)
                    generateChild(DirectionX.RIGHT, charges);
                break;
        }
    }
}
