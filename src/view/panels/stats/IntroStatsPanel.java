package view.panels.stats;

import model.managers.StatisticsManager;
import view.utils.FontTool;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static constants.Constants.*;

public class IntroStatsPanel extends JPanel {

    private final StatisticsManager statsManager;
    private final Font font70, font50;
    private final ArrayList<BufferedImage> charactersImages;

    // Campi per le animazioni interne
    private int textTickCounter = 0;
    private int animationTickCounter = 0;

    public IntroStatsPanel(StatisticsManager statsManager, ArrayList<BufferedImage> charactersImages, Font font70, Font font50) {
        this.statsManager = statsManager;
        this.charactersImages = charactersImages;
        this.font70 = font70;
        this.font50 = font50;
        
        setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    public void update() {
        textTickCounter = (textTickCounter + 1) % 40;
        animationTickCounter = (animationTickCounter + 1) % 20;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        // Disegna titolo
        g2.setFont(font70);
        String title = "create your user!";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (SCREENWIDTH / 2) - (titleWidth / 2), HALF_SCREENHEIGHT - TILESIZE * 2);

        // Disegna testo "press enter" lampeggiante
        if (textTickCounter < 20) {
            g2.setFont(font50);
            String subtitle = "press enter to continue";
            int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
            g2.drawString(subtitle, (SCREENWIDTH / 2) - (subtitleWidth / 2), HALF_SCREENHEIGHT + TILESIZE * 2);
        }
        
        // Disegna il giocatore corrente che si sta per creare
        StatisticsManager.CurrentPlayer playerEnum = statsManager.getCurrentPlayer();
        String playerText = "";
        int charImgIndex = 0;
        
        if (playerEnum == StatisticsManager.CurrentPlayer.P1) {
            g2.setColor(Color.CYAN);
            playerText = "P1";
            charImgIndex = 0;
        } else {
            g2.setColor(Color.ORANGE); // Colore diverso per P2
            playerText = "P2";
            charImgIndex = 2;
        }
        
        g2.setFont(font70);
        g2.drawString(playerText, HALF_SCREENWIDTH + TILESIZE, HALF_SCREENHEIGHT + TILESIZE * 8);
        
        // Animazione del personaggio
        int animationFrame = (animationTickCounter < 10) ? 0 : 1;
        g2.drawImage(charactersImages.get(charImgIndex + animationFrame), HALF_SCREENWIDTH - TILESIZE * 4, HALF_SCREENHEIGHT + TILESIZE * 6, null);

        g2.dispose();
    }
}