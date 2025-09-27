package model.interfaces;

import java.util.List;

public interface EntityProvider<T extends Updatable> {
    
    List<T> getEntities();
    
}