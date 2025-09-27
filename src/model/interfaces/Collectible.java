package model.interfaces;

import model.entities.players.Player;

public interface Collectible {
    void collect(Player player);
    boolean isReadyToRemove();
}
