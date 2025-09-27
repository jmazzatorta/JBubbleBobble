package view.panels.stats;

import model.users.User;
import model.users.UserDatas;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import static constants.Constants.*;

public class StatsDisplayPanel extends JPanel {

    private final UserDatas userDatas;
    private final BufferedImage[] avatars;
    private final Font font;

    public StatsDisplayPanel(UserDatas userDatas, BufferedImage[] avatars, Font font) {
        this.userDatas = userDatas;
        this.avatars = avatars;
        this.font = font;
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
        g2.setFont(font);

        String title = "game stats";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (SCREENWIDTH / 2) - (titleWidth / 2), TILESIZE * 4);

        g2.setStroke(new BasicStroke(2));
        
        List<User> usersList = userDatas.getUsersList();
        int startY = TILESIZE * 6;
        int rowHeight = TILESIZE * 4;

        for (int i = 0; i < 5; i++) {
            int y = startY + rowHeight * i;
            g2.drawLine(0, y, SCREENWIDTH, y);
            if (i < usersList.size()) {
                drawUser(g2, y, usersList.get(i));
            }
        }
        // Disegna l'ultima linea in fondo
        g2.drawLine(0, startY + rowHeight * 5, SCREENWIDTH, startY + rowHeight * 5);
        
        g2.dispose();
    }

    private void drawUser(Graphics2D g2, int y, User user) {
        g2.drawImage(avatars[user.getAvatarIndex()], TILESIZE * 4, y + TILESIZE / 2, null);
        
        String username = user.getName();
        int nameWidth = g2.getFontMetrics().stringWidth(username);
        String score = "" + user.getScore();
        
        int textY = y + (TILESIZE * 2) + (g2.getFontMetrics().getAscent() / 2);
        
        g2.drawString(username, (SCREENWIDTH / 2) - (nameWidth / 2), textY);
        g2.drawString(score, 24 * TILESIZE, textY);
    }
}