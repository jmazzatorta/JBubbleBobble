package view.visualFx;

import java.awt.image.BufferedImage;

public class ScoreFx extends VisualFx { 
    
    private final int FX_SPEED = 1;

    public ScoreFx(int arrayPosition, int x, int y, BufferedImage image) {
        super(arrayPosition, x, y);
        this.currentSprite = image; // Imposta lo sprite una sola volta
    }
    
    @Override
    public void update() {
        frameCounter++;
        if (frameCounter < 120) {
            y -= FX_SPEED; // L'effetto sale
        }
        if (frameCounter > 180) {
            alive = false; // L'effetto muore dopo un po'
        }
    }

    // --- METODO DRAW RIMOSSO ---
    // Ora userà il metodo draw() della classe base VisualFx, che è corretto.
}