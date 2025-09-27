package view.panels;

import static constants.Constants.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import model.GameModel; // Importa il GameModel per coerenza
import view.utils.FontTool;

@SuppressWarnings("serial")
public class GameOverPanel extends JPanel {

    private final Font font;

    public GameOverPanel(GameModel model) {
        this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        
        this.font = FontTool.loadFont("font1.ttf", 70f);
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        String text = "game over";
        int width = g2.getFontMetrics().stringWidth(text);
        
        g2.drawString(text, (SCREENWIDTH / 2) - (width / 2), SCREENHEIGHT / 2);
        
        g2.dispose();
    }
}