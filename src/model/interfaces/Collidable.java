package model.interfaces;

import java.awt.Rectangle;
import static constants.Constants.*; // Aggiungi questo import se non c'è

public interface Collidable {
    
    // Metodi base richiesti da ogni oggetto "collidibile"
    Rectangle getHitbox();
    int getX();
    int getY();

    // NUOVO: Metodi di utilità ereditati da Hitboxable.
    // Forniscono calcoli comuni senza bisogno di riscrivere codice.
    default int getLeft() { return getX() + getHitbox().x; }
    default int getRight() { return getX() + getHitbox().x + getHitbox().width; }
    default int getTop() { return getY() + getHitbox().y; }
    default int getBottom() { return getY() + getHitbox().y + getHitbox().height; }

    default int getLeftCol() { return getLeft() / TILESIZE; }
    default int getRightCol() { return getRight() / TILESIZE; }
    default int getTopRow() { return getTop() / TILESIZE; }
    default int getBottomRow() { return getBottom() / TILESIZE; }
}