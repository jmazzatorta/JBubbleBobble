package view.visualFx;

import static constants.Constants.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DynamiteFx extends VisualFx {
    
    private final ArrayList<BufferedImage> images;
    private int spriteID = 0; // Inizializza a 0

    public DynamiteFx(int arrayPosition, ArrayList<BufferedImage> images) {
        super(arrayPosition, SCREENWIDTH / 2 - (images.get(0).getWidth() / 2), 
                             SCREENHEIGHT / 2 - (images.get(0).getHeight() / 2));
        this.images = images;
    }

    @Override
    public void update() {
        frameCounter++;
        
        if (frameCounter > 80) {
            alive = false;
        } else {
            // Calcola l'indice dello sprite in base al tempo, senza superare i limiti
            spriteID = frameCounter / 20;
            if (spriteID >= images.size()) {
                spriteID = images.size() - 1; // Blocca all'ultimo frame
            }
        }
        
        // Imposta sempre lo sprite corrente dopo aver calcolato l'indice
        currentSprite = images.get(spriteID);
    }
}