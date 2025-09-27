package model.interfaces;

import java.util.List;

/**
 * Definisce il contratto per qualsiasi oggetto che possa fornire
 * una lista di entità di un certo tipo.
 * È usato per disaccoppiare la View dai Manager concreti del Model.
 * @param <T> il tipo di entità fornita, deve essere almeno Updatable.
 */
public interface EntityProvider<T extends Updatable> {
    
    /**
     * Restituisce una lista delle entità gestite.
     * Si raccomanda di restituire una copia della lista per evitare
     * modifiche concorrenti dall'esterno (incapsulamento).
     * @return una lista di entità.
     */
    List<T> getEntities();
}