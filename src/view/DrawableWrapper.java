package view;

import view.interfaces.Drawable;

public class DrawableWrapper {

    private final Drawable drawable;   
    private int tickCounter;           
    private int currentFrame;         
    private int animationSpeed;       
    
    private String previousAction;

    public DrawableWrapper(Drawable drawable) {
        this.drawable = drawable;
        this.tickCounter = 0;
        this.currentFrame = 0;
        this.animationSpeed = 10; 
    }
    
    public void update() {
    	String currentAction = drawable.getActionData();
    	if (currentAction != previousAction) reset();
    	previousAction = currentAction;
    	
    	tick();	
    }

    private void tick() {
        tickCounter++;
        if (tickCounter >= animationSpeed) {
            currentFrame++;
            tickCounter = 0;
        }
    }

    public int getCurrentFrame(int totalFrames) {
        if (totalFrames <= 1) return 0;
        if (currentFrame >= totalFrames) currentFrame = 0;
        return currentFrame;
    }

    public void reset() {
        tickCounter = 0;
        
        switch (drawable.getActionData()) {
        case "INSIDE BUBBLE" : currentFrame = 2; break;
        default : currentFrame = 0; break;
        }
        
    }

    public void setAnimationSpeed(int speed) {
        this.animationSpeed = speed;
    }

    public Drawable getDrawable() { return drawable; }
    public int getTickCounter() { return tickCounter; }
    public int getCurrentFrame() { return currentFrame; }
    public int getAnimationSpeed() { return animationSpeed; }

}