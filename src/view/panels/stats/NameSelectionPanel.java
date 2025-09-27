package view.panels.stats;

import model.managers.StatisticsManager;
import javax.swing.*;
import java.awt.*;
import static constants.Constants.*;

public class NameSelectionPanel extends JPanel {

    private final StatisticsManager statsManager;
    private final Font font70, font50;

    public NameSelectionPanel(StatisticsManager statsManager, Font font70, Font font50) {
        this.statsManager = statsManager;
        this.font70 = font70;
        this.font50 = font50;
        setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    // Pannello statico, l'update è vuoto
    public void update() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        // --- Lettura dati dal View-Model ---
        String typedString = statsManager.getTypedString();
        
        g2.setFont(font70);
        String title = "Enter your name!";
        int width = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (SCREENWIDTH / 2) - (width / 2), TILESIZE * 6);
        
        g2.setFont(font50);
        int typedWidth = g2.getFontMetrics().stringWidth(typedString);
        g2.drawString(typedString, (SCREENWIDTH / 2) - (typedWidth / 2), TILESIZE * 12);
        
        int boxWidth = Math.max(TILESIZE * 8, typedWidth);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect((SCREENWIDTH / 2) - (boxWidth / 2) - TILESIZE, TILESIZE * 10, boxWidth + TILESIZE * 2, TILESIZE * 3);
        
        g2.dispose();
    }
}