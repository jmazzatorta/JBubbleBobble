package view.panels.stats;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import model.managers.StatisticsManager;
import model.managers.StatisticsManager.AvatarButton;
import static constants.Constants.*;

public class AvatarSelectionPanel extends JPanel {

    private final StatisticsManager statsManager;
    private final BufferedImage[] avatars;
    private final Font font;
    private int selectionTickCounter = 0;

    public AvatarSelectionPanel(StatisticsManager statsManager, BufferedImage[] avatars, Font font) {
        this.statsManager = statsManager;
        this.avatars = avatars;
        this.font = font;
        this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    // Metodo per le animazioni (es. il rettangolo che lampeggia)
    public void update() {
        selectionTickCounter = (selectionTickCounter + 1) % 20;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // --- Lettura dati dal View-Model (StatisticsManager) ---
        AvatarButton[] avatarButtons = statsManager.getAvatarButtons();
        AvatarButton selectedButton = statsManager.getSelectedButton();

        // Disegna il testo
        g2.setColor(Color.WHITE);
        g2.setFont(font);
        String text = "Choose your avatar!";
        int width = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (SCREENWIDTH / 2) - (width / 2), TILESIZE * 6);

        // Disegna gli avatar
        for (AvatarButton button : avatarButtons) {
            g2.drawImage(avatars[button.getIndex()], button.getX(), button.getY(), null);
        }
        
        // Disegna il rettangolo di selezione lampeggiante
        if (selectionTickCounter < 10) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.YELLOW);
            g2.drawRect(selectedButton.getX() - 5, selectedButton.getY() - 5, 
                        avatars[0].getWidth() + 10, avatars[0].getHeight() + 10);
        }
        
        g2.dispose();
    }
}
