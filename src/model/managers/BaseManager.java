package model.managers;

import model.GameModel;
import model.interfaces.EntityProvider;
import model.interfaces.Updatable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseManager<T extends Updatable> implements Updatable, EntityProvider<T> {

    protected List<T> entities = new ArrayList<>();
    private final List<T> toAdd = new ArrayList<>();
    private final List<T> toRemove = new ArrayList<>();

    protected GameModel gameModel;

    public BaseManager(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void add(T entity) {
        toAdd.add(entity);
    }

    public void remove(T entity) {
        toRemove.add(entity);
    }

    @Override
    public void update() {
        for (T entity : entities) {
            entity.update();
        }

        for (T entity : toAdd) {
            if (!entities.contains(entity)) {
                entities.add(entity);

                gameModel.notifyEntityAdded(entity);

            }
        }
        toAdd.clear(); 

        for (T entity : toRemove) {
            if (entities.remove(entity)) {
                if (gameModel != null) {
                    gameModel.notifyEntityRemoved(entity);
                }
            }
        }
        toRemove.clear(); 
    }


    public void reset() {

        toRemove.addAll(entities);

        toAdd.clear();

    }
    
    @Override
    public List<T> getEntities() {
        return new ArrayList<>(entities);
    }
    
    protected boolean hasPendingAdditions() {
        return !toAdd.isEmpty();
    }
}