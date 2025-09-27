package view.interfaces;

import model.utils.MinorEvent;

/**
 * L'interfaccia che la View deve implementare per ascoltare
 * e reagire ai cambiamenti che avvengono nel GameModel.
 */
public interface EntityListener {

    /**
     * Chiamato quando una singola entità (nemico, bolla, item, etc.)
     * viene aggiunta al modello di gioco.
     * @param <T>
     * @param entity L'oggetto di gioco aggiunto.
     */
    <T> void onEntityAdded(T entity);

    /**
     * Chiamato quando una singola entità viene rimossa dal modello.
     * @param <T>
     * @param entity L'oggetto di gioco rimosso.
     */
    <T> void onEntityRemoved(T entity);


	void onMinorEvent(MinorEvent event);

}