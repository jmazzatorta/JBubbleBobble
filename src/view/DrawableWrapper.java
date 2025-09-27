package view;

import view.interfaces.Drawable;

/**
 * Wrapper generico per un oggetto Drawable.
 * Tiene traccia del tick per animazioni e dell'indice dell'immagine corrente.
 */
public class DrawableWrapper {

    private final Drawable drawable;   // L'oggetto view originale
    private int tickCounter;           // Conta i tick per cambiare frame
    private int currentFrame;          // Indice dell'immagine corrente
    private int animationSpeed;        // Numero di tick per avanzare di un frame
    
    private String previousAction;

    public DrawableWrapper(Drawable drawable) {
        this.drawable = drawable;
        this.tickCounter = 0;
        this.currentFrame = 0;
        this.animationSpeed = 10; // Default, può essere modificato
    }
    
    public void update() {
    	String currentAction = drawable.getActionData();
    	if (currentAction != previousAction) reset();
    	previousAction = currentAction;
    	
    	tick();
    	
    }

    /** Incrementa il tick e aggiorna l'immagine corrente */
    private void tick() {
        tickCounter++;
        if (tickCounter >= animationSpeed) {
            currentFrame++;
            tickCounter = 0;
        }
    }

    /**
     * Restituisce l'indice dell'immagine da usare.
     * Il numero massimo di frame viene passato dal chiamante.
     */
    public int getCurrentFrame(int totalFrames) {
        if (totalFrames <= 1) return 0;
        if (currentFrame >= totalFrames) currentFrame = 0;
        return currentFrame;
    }

    /** Resetta il wrapper (frame e tick) */
    public void reset() {
        tickCounter = 0;
        
        // Casi irregolari
        switch (drawable.getActionData()) {
        case "INSIDE BUBBLE" : currentFrame = 4; break;
        default : currentFrame = 0; break;
        }
        
    }

    /** Imposta la velocità di animazione (tick per frame) */
    public void setAnimationSpeed(int speed) {
        this.animationSpeed = speed;
    }

    // --- Getter ---
    public Drawable getDrawable() { return drawable; }
    public int getTickCounter() { return tickCounter; }
    public int getCurrentFrame() { return currentFrame; }
    public int getAnimationSpeed() { return animationSpeed; }

}