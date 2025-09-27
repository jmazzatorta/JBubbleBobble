package model.managers;

import static constants.Constants.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.GameModel;
import model.entities.PowerUpType;
import model.entities.players.Player;
import model.items.Food;
import model.items.FoodRandomizer;
import model.items.Item;
import model.items.SpecialItem;

public class ItemsManager extends BaseManager<Item> {

    private PlayersManager playersManager;
    private final TilesManager tilesManager;
    private final FoodRandomizer randomizer;
    private final Random random;

    private boolean factoryBlocked = false;
    private int itemsSpawnTC = 10 * FPS;
    private int conditionCheckTC = 0;

    public ItemsManager(GameModel gameModel, TilesManager tilesManager) {
    	super(gameModel);
        this.tilesManager = tilesManager;
        this.entities = new ArrayList<>();
        this.randomizer = new FoodRandomizer(tilesManager.getMap());
        this.random = new Random();
    }
    
    public void setPlayersManager(PlayersManager playersManager) {
    	this.playersManager = playersManager;	
	}

    // --- CREAZIONE OGGETTI ---
    public void foodFactory(int x, int y) {
        String itemName = randomizer.randomizeItem();
        Item item = switch(itemName) {
            case "GreenPepper"   -> new Food(itemName, x * TILESIZE, y * TILESIZE, 10, tilesManager);
            case "RedTurnip"     -> new Food(itemName, x * TILESIZE, y * TILESIZE, 50, tilesManager);
            case "Cucumber"      -> new Food(itemName, x * TILESIZE, y * TILESIZE, 100, tilesManager);
            case "Corn"          -> new Food(itemName, x * TILESIZE, y * TILESIZE, 200, tilesManager);
            case "Persimmon"     -> new Food(itemName, x * TILESIZE, y * TILESIZE, 300, tilesManager);
            case "Banana"        -> new Food(itemName, x * TILESIZE, y * TILESIZE, 500, tilesManager);
            case "Peach"         -> new Food(itemName, x * TILESIZE, y * TILESIZE, 400, tilesManager);
            case "FrenchFries"   -> new Food(itemName, x * TILESIZE, y * TILESIZE, 1000, tilesManager);
            case "Crystal"       -> new Food(itemName, x * TILESIZE, y * TILESIZE, 8000, tilesManager);
            case "BigWatermelon" -> new Food(itemName, x * TILESIZE, y * TILESIZE - TILESIZE, 4 * TILESIZE, 16000, tilesManager);
            default -> null;
        };
        if (item != null) add(item); 
    }

    public void specialItemsFactory(PowerUpType itemType) { 
        Point coords = randomizer.randomizeCoordinates();
        SpecialItem item = new SpecialItem(itemType, coords.x * TILESIZE, coords.y * TILESIZE, tilesManager);
        add(item);
    }

    @Override
    public void update() {
        super.update();

        if (!factoryBlocked) {
            itemsSpawnTC--;
            if (itemsSpawnTC <= 0) {
                Point coords = randomizer.randomizeCoordinates();
                foodFactory(coords.x, coords.y);
                itemsSpawnTC = 15 * FPS;
            }
        }

        conditionCheckTC++;
        if (conditionCheckTC > 10) {
            Player p1 = playersManager.getP1();
            Player p2 = playersManager.hasTwoPlayers() ? playersManager.getP2() : null;
            if (p1 != null) checkItemsConditions(p1);
            if (p2 != null) checkItemsConditions(p2);
            conditionCheckTC = 0;
        }

        for (Item item : entities) {
            if (item.isReadyToRemove()) {
                remove(item); 
            }
        }
    }

    private void checkItemsConditions(Player p) {
        if (p == null) return;

        // --- DROPPARE CANDIES ---
        if (p.getBubblesCounter() > 35) {
            int rand = random.nextInt(3);
            switch(rand) {
                case 0 -> specialItemsFactory(PowerUpType.PINKCANDY);
                case 1 -> specialItemsFactory(PowerUpType.BLUECANDY);
                case 2 -> specialItemsFactory(PowerUpType.YELLOWCANDY);
            }
            p.resetBubblesCounter();
        }

        // --- DROPPARE SCARPE ---
        if (p.getStepCounter() > SCREENWIDTH * 10) {
            specialItemsFactory(PowerUpType.SHOES);
            p.resetStepCounter();
        }

        // --- DROPPARE CLOCK / DYNAMITE ---
        if (p.getSpecialBubblesCounter() > 12) {
            if (random.nextInt(2) == 0) {
                specialItemsFactory(PowerUpType.CLOCK);
            } else {
                specialItemsFactory(PowerUpType.DYNAMITE);
            }
            p.resetSpecialBubblesCounter();
        }

        // --- DROPPARE RINGS ---
        if (p.getCandyCounter(PowerUpType.BLUECANDY) > 2) {
            specialItemsFactory(PowerUpType.CRYSTALRING);
            p.resetCandyCounter(PowerUpType.BLUECANDY);
        } else if (p.getCandyCounter(PowerUpType.YELLOWCANDY) > 2) {
            specialItemsFactory(PowerUpType.AMETHYSTRING);
            p.resetCandyCounter(PowerUpType.YELLOWCANDY);
        } else if (p.getCandyCounter(PowerUpType.PINKCANDY) > 2) {
            specialItemsFactory(PowerUpType.RUBYRING);
            p.resetCandyCounter(PowerUpType.PINKCANDY);
        }

    }

    // --- BLOCK / UNBLOCK FACTORY ---
    public void blockFactory() { factoryBlocked = true; }
    public void unlockFactory() { factoryBlocked = false; }

    @Override
    public void reset() {
        super.reset(); 
    }
    
    public FoodRandomizer getFoodRandomizer() {
    	return randomizer;
    }

    public List<Item> getItems() { return getEntities(); }
}
