package view.panels.stats;

import model.managers.StatisticsManager;
import model.users.User;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static constants.Constants.*;

public class FinalizationPanel extends JPanel {

    private final StatisticsManager statsManager;
    private final BufferedImage[] avatars;
    private final Font font70, font40;

    public FinalizationPanel(StatisticsManager statsManager, BufferedImage[] avatars, Font font70, Font font40) {
        this.statsManager = statsManager;
        this.avatars = avatars;
        this.font70 = font70;
        this.font40 = font40;
        setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    public void update() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        
        User createdUser = statsManager.getCreatedUser();
        if (createdUser == null) return; 

        g2.setFont(font70);
        String title = "User created!";
        int boxWidth = g2.getFontMetrics().stringWidth(title);
        int leftBorder = (SCREENWIDTH / 2) - (boxWidth / 2);
        g2.drawString(title, leftBorder, TILESIZE * 6);
        
        g2.drawImage(avatars[createdUser.getAvatarIndex()], leftBorder + TILESIZE * 2, HALF_SCREENHEIGHT - TILESIZE, null);
        
        g2.setFont(font40);
        String username = createdUser.getName();
        int nameWidth = g2.getFontMetrics().stringWidth(username);
        String score = "" + createdUser.getScore();
        int scoreWidth = g2.getFontMetrics().stringWidth(score);
        
        g2.drawString(username, (SCREENWIDTH / 2) - (nameWidth / 2), HALF_SCREENHEIGHT + TILESIZE / 2);
        g2.drawString(score, leftBorder + boxWidth - scoreWidth - TILESIZE, HALF_SCREENHEIGHT + TILESIZE / 2);
        
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(leftBorder, HALF_SCREENHEIGHT - TILESIZE * 2, boxWidth, TILESIZE * 4);
        
        g2.dispose();
    }
}