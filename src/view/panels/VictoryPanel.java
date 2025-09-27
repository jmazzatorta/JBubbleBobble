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
public class VictoryPanel extends JPanel {

    private final Font font1;
    private final Font font2;

    public VictoryPanel(GameModel model) {
        this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        

        this.font1 = FontTool.loadFont("font1.ttf", 70f);
        this.font2 = FontTool.loadFont("font1.ttf", 40f);
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        

        g2.setFont(font1);
        String text1 = "you win";
        int width1 = g2.getFontMetrics().stringWidth(text1);
        g2.drawString(text1, (SCREENWIDTH / 2) - (width1 / 2), (SCREENHEIGHT / 2) - TILESIZE * 3);
        
        g2.setFont(font2);
        String text2 = "press enter to play again";
        int width2 = g2.getFontMetrics().stringWidth(text2);
        g2.drawString(text2, (SCREENWIDTH / 2) - (width2 / 2), (SCREENHEIGHT / 2) + TILESIZE * 3);
        
        String text3 = "press esc for the stats";
        int width3 = g2.getFontMetrics().stringWidth(text3);
        g2.drawString(text3, (SCREENWIDTH / 2) - (width3 / 2), (SCREENHEIGHT / 2) + TILESIZE * 6);
        
        g2.dispose();
    }
}