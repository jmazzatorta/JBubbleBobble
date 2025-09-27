package model.items;

import static constants.Constants.*;

import model.entities.PowerUpType;
import model.entities.players.Player;
import model.managers.TilesManager;


public class SpecialItem extends Item {
    private final PowerUpType itemType; 

    public SpecialItem(PowerUpType itemType, int x, int y, TilesManager tilesManager) { 
        super(x, y, 2 * TILESIZE, tilesManager);
        this.itemType = itemType;
    }

    @Override
    public void collect(Player collector) {
        if (!readyToRemove) {
            collector.activatePowerUp(itemType); 
            readyToRemove = true;
        }
    }

    public PowerUpType getItemType() { return itemType; }
    
    @Override
    public String getTypeData() {
		return itemType.toString();
    	
    }

}
