package model.managers;

import model.GameModel;
import model.interfaces.EntityProvider;
import model.interfaces.Updatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe base astratta per tutti i manager (MODIFICATA PER PERFORMANCE).
 * Ora usa il pattern "Buffered Modification" per evitare di creare nuove
 * collezioni ad ogni frame, risolvendo un problema critico di performance.
 */
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

    /**
     * Aggiunge l'entità a una lista d'attesa.
     * L'aggiunta effettiva avverrà alla fine del ciclo di update.
     * @param entity L'entità da aggiungere.
     */
    public void add(T entity) {
        toAdd.add(entity);
    }

    /**
     * Aggiunge l'entità a una lista d'attesa per la rimozione.
     * @param entity L'entità da rimuovere.
     */
    public void remove(T entity) {
        // Aggiungiamo alla lista temporanea per la rimozione.
        toRemove.add(entity);
    }

    /**
     * 1. Aggiorna tutte le entità esistenti.
     * 2. Applica le modifiche (rimozioni e aggiunte) in modo sicuro.
     */
    @Override
    public void update() {
        // FASE 1: Aggiorna lo stato di tutte le entità nella lista principale.
        for (T entity : entities) {
            entity.update();
        }

        // FASE 2: Applica le aggiunte bufferizzate.
        for (T entity : toAdd) {
            if (!entities.contains(entity)) {
                entities.add(entity);

                gameModel.notifyEntityAdded(entity);

            }
        }
        toAdd.clear(); // Svuota la lista d'attesa per il prossimo frame.

        // FASE 3: Applica le rimozioni bufferizzate.
        for (T entity : toRemove) {
            if (entities.remove(entity)) {
                if (gameModel != null) {
                    gameModel.notifyEntityRemoved(entity);
                }
            }
        }
        toRemove.clear(); // Svuota la lista d'attesa per il prossimo frame.
    }

    /**
     * Svuota il manager, assicurandosi di processare anche le entità
     * in attesa di essere rimosse e di pulire quelle in attesa di essere aggiunte.
     */
    public void reset() {
        // Aggiunge tutte le entità correnti alla lista di rimozione.
        toRemove.addAll(entities);
        // Pulisce eventuali aggiunte in sospeso che non sono mai state processate.
        toAdd.clear();
        
        // Nota: Le entità verranno effettivamente rimosse e notificate
        // al prossimo ciclo di update(). Questo garantisce coerenza.
    }
    
    /**
     * Restituisce una copia difensiva della lista di entità.
     * Questo metodo è usato principalmente dalla View, che non viene chiamata
     * 60 volte al secondo, quindi il costo di creare una copia qui è accettabile
     * e garantisce sicurezza.
     * @return una nuova lista contenente le entità attuali.
     */
    @Override
    public List<T> getEntities() {
        return new ArrayList<>(entities);
    }
    
    protected boolean hasPendingAdditions() {
        return !toAdd.isEmpty();
    }
}