package view.interfaces;

import model.utils.MinorEvent;

public interface EntityListener {

    <T> void onEntityAdded(T entity);

    <T> void onEntityRemoved(T entity);

	void onMinorEvent(MinorEvent event);

}