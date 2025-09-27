package view.panels;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.GameModel;
import model.managers.StatisticsManager;
import model.managers.StatisticsManager.Phases;
import model.users.UserDatas;
import view.panels.stats.*; // Importa il nuovo package con i sotto-pannelli
import view.utils.FontTool;
import view.utils.ImageLoader;
import static constants.Constants.*;

/**
 * Pannello contenitore per le schermate di gestione statistiche (rifattorizzato).
 * Usa un CardLayout per mostrare il sotto-pannello corretto in base allo stato
 * del suo View-Model (StatisticsManager).
 */
@SuppressWarnings("serial")
public class StatsPanel extends JPanel {

    private final StatisticsManager statsManager;
    private final CardLayout cardLayout;

    // Riferimenti ai sotto-pannelli che questo contenitore gestisce
    private final IntroStatsPanel introStatsPanel;
    private final AvatarSelectionPanel avatarSelectionPanel;
    private final NameSelectionPanel nameSelectionPanel;
    private final FinalizationPanel finalizationPanel;
    private final StatsDisplayPanel statsDisplayPanel;

    public StatsPanel(GameModel model) {
        this.statsManager = model.getStatisticsManager();
        UserDatas userDatas = model.getUserDatas(); 

        // 2. Imposta questo pannello come un contenitore CardLayout
        this.cardLayout = new CardLayout();
        setLayout(this.cardLayout);
        setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));

        // 3. Carica le risorse condivise una sola volta
        ArrayList<BufferedImage> charactersImages = loadCharactersImages();
        BufferedImage[] avatars = loadAvatars();
        Font font40 = FontTool.loadFont("font1.ttf", 40f);
        Font font50 = FontTool.loadFont("font1.ttf", 50f);
        Font font70 = FontTool.loadFont("font1.ttf", 70f);

        // 4. Crea le istanze dei sotto-pannelli, iniettando le dipendenze
        introStatsPanel = new IntroStatsPanel(statsManager, charactersImages, font70, font50);
        avatarSelectionPanel = new AvatarSelectionPanel(statsManager, avatars, font70);
        nameSelectionPanel = new NameSelectionPanel(statsManager, font70, font50);
        finalizationPanel = new FinalizationPanel(statsManager, avatars, font70, font40);
        statsDisplayPanel = new StatsDisplayPanel(userDatas, avatars, font50);
        
        // 5. Aggiungi i sotto-pannelli come "carte" al layout
        add(introStatsPanel, Phases.INTRO.toString());
        add(avatarSelectionPanel, Phases.AVATAR_SELECTION.toString());
        add(nameSelectionPanel, Phases.NAME_SELECTION.toString());
        add(finalizationPanel, Phases.FINALIZATION.toString());
        add(statsDisplayPanel, Phases.STATS_DISPLAY.toString());
    }
    
    /**
     * Metodo chiamato dal GameFrame per aggiornare lo stato di questa schermata.
     */
    public void update() {
        // A. Leggi la fase corrente dal "cervello" (StatisticsManager)
        Phases currentPhase = statsManager.getCreationPhase();
        
        // B. Mostra il sotto-pannello corretto
        cardLayout.show(this, currentPhase.toString());

        // C. Delega l'update() al sotto-pannello attivo, se ha animazioni
        switch (currentPhase) {
            case INTRO:
                introStatsPanel.update();
                break;
            case AVATAR_SELECTION:
                avatarSelectionPanel.update();
                break;
            default:
                // Gli altri pannelli sono statici e non hanno bisogno di update
                break;
        }
    }

    // Metodi helper per caricare le risorse condivise
    private BufferedImage[] loadAvatars() {
        BufferedImage[] avatarsArray = new BufferedImage[AVATARS_NUM];
        ImageLoader.setPath("/avatars/");
        ImageLoader.setScaling(2);
        for (int i = 0; i < AVATARS_NUM; i++) {
            avatarsArray[i] = ImageLoader.load("" + (1 + i));
        }
        return avatarsArray;
    }

    private ArrayList<BufferedImage> loadCharactersImages() {
        ArrayList<BufferedImage> images = new ArrayList<>();
        ImageLoader.setScaling(2);
        ImageLoader.setPath("/player/");
        images.add(ImageLoader.load("bubwalking1"));
        images.add(ImageLoader.load("bubwalking2"));
        images.add(ImageLoader.load("bobwalking1"));
        images.add(ImageLoader.load("bobwalking2"));
        return images;
    }
}