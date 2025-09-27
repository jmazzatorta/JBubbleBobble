package view;

import static constants.Constants.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import model.GameModel;
import model.entities.players.Player;
import model.managers.ScoreManager;
import view.utils.FontTool;
import view.utils.ImageLoader;

public class GUI {

    private final GameModel model;
    private final Font font;
    private final BufferedImage bubLife;
    private final BufferedImage bobLife;

    private long insertCoinTimer = 0;
    private boolean showInsertCoin = true;

    public GUI(GameModel model) {
        this.model = model;
        
        this.font = FontTool.loadFont("font1.ttf", 80f);
        this.bubLife = loadLifeImage("bublife");
        this.bobLife = loadLifeImage("boblife");
        
        this.insertCoinTimer = System.currentTimeMillis();
    }
    
    private BufferedImage loadLifeImage(String imageName) {
        ImageLoader.setScaling(1);
        ImageLoader.setPath("/player/");
        return ImageLoader.load(imageName);
    }

    public void update() {
        if (System.currentTimeMillis() - insertCoinTimer > 1000) {
            showInsertCoin = !showInsertCoin;
            insertCoinTimer = System.currentTimeMillis();
        }
    }

    public void draw(Graphics2D g2) {
        Player p1 = model.getPlayersManager().getP1();
        Player p2 = model.getPlayersManager().getP2();
        
        int scoreP1 = (p1 != null) ? p1.getScore() : 0;
        int livesP1 = (p1 != null) ? p1.getLives() : 0;
        
        int scoreP2 = (p2 != null) ? p2.getScore() : 0;
        int livesP2 = (p2 != null) ? p2.getLives() : 0;
        
        int maxScore = ScoreManager.getHighestScore();

        drawTexts(g2, scoreP1, scoreP2, maxScore);
        drawLives(g2, livesP1, livesP2);
    }

    private void drawTexts(Graphics2D g2, int scoreP1, int scoreP2, int maxScore) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCREENWIDTH, 2 * TILESIZE);
        
        g2.setFont(font);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        drawText(g2, String.valueOf(scoreP1), 5 * TILESIZE, 2 * TILESIZE, Color.WHITE);
        drawText(g2, "1up", 5 * TILESIZE, TILESIZE, Color.CYAN);
        
        if (model.getPlayersManager().hasTwoPlayers()) {
            drawText(g2, String.valueOf(scoreP2), 26 * TILESIZE, 2 * TILESIZE, Color.WHITE);
            drawText(g2, "2up", 26 * TILESIZE, TILESIZE, Color.CYAN);
        } else if (showInsertCoin) {
            drawText(g2, "insert", 26 * TILESIZE, TILESIZE, Color.CYAN);
            drawText(g2, "coin", 26 * TILESIZE, 2 * TILESIZE, Color.CYAN);
        }
        
        drawText(g2, String.valueOf(maxScore), 16 * TILESIZE, 2 * TILESIZE, Color.WHITE);
        drawText(g2, "high score", 16 * TILESIZE, TILESIZE, Color.RED);
    }

    private void drawText(Graphics2D g2, String text, int x, int y, Color color) {
        g2.setColor(color);
        int width = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, x - (width / 2), y);
    }

    private void drawLives(Graphics2D g2, int livesP1, int livesP2) {
        for (int i = 0; i < livesP1; i++) {
            g2.drawImage(bubLife, i * TILESIZE, 27 * TILESIZE, null);
        }
        
        for (int i = 0; i < livesP2; i++) {
            g2.drawImage(bobLife, (31 - i) * TILESIZE, 27 * TILESIZE, null);
        }
    }
}