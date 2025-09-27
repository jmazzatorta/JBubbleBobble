package view.panels;

import static constants.Constants.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.GameModel;
import view.GUI;
import view.utils.FontTool;
import view.utils.ImageLoader;


@SuppressWarnings("serial")
public class IntroPanel extends JPanel { // Estende CustomPanel per coerenza, se necessario

    private final GUI gui;
    private final Font font;
    private final ArrayList<BufferedImage> logoFrames;
    private final BufferedImage taito;
    private final int xLogo, yLogo, xTaito, yTaito;
    
    private int logoFrameIndex = 0;
    private int tickCounter = 0;

    public IntroPanel(GameModel model) {
        this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.gui = new GUI(model);
        
        this.logoFrames = new ArrayList<>();
        this.taito = loadTaitoImage();
        loadLogoFrames();
        
        this.font = FontTool.loadFont("font1.ttf", 50f);
        
        BufferedImage firstLogo = logoFrames.get(0);
        this.xLogo = (SCREENWIDTH / 2) - (firstLogo.getWidth() / 2);
        this.yLogo = 3 * TILESIZE;
        this.xTaito = (SCREENWIDTH / 2) - (this.taito.getWidth() / 2);
        this.yTaito = yLogo + (firstLogo.getHeight()) + TILESIZE;
    }

    public void update() {
        tickCounter++;
        if (tickCounter > 20) {
            logoFrameIndex = (logoFrameIndex + 1) % logoFrames.size();
            tickCounter = 0;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(logoFrames.get(logoFrameIndex), xLogo, yLogo, null);
        g2.drawImage(taito, xTaito, yTaito, null);

        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String text = "jacopo mazzatorta"; 
        int width = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (SCREENWIDTH / 2) - width / 2, yTaito + TILESIZE * 4);

        gui.draw(g2);

        g2.dispose();
    }

    private void loadLogoFrames() {
        ImageLoader.setPath("/screenres/");
        ImageLoader.setScalingX(22);
        ImageLoader.setScalingY(18);
        for (int i = 1; i < 8; i++) {
            logoFrames.add(ImageLoader.load("logo" + i));
        }
    }

    private BufferedImage loadTaitoImage() {
        ImageLoader.setPath("/screenres/");
        ImageLoader.setScalingX(9);
        ImageLoader.setScalingY(2);
        return ImageLoader.load("taito");
    }
}