package view.visualFx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static constants.Constants.*;

import model.utils.MinorEvent;
import view.utils.ImageLoader;

/**
 * Gestisce la creazione, l'aggiornamento e il disegno degli effetti visivi (VFX).
 * Non è più un Singleton. Ora ha metodi separati per 'update' e 'draw'
 * e riceve gli eventi da creare tramite un metodo pubblico 'addEvent'.
 */
public class VFXManager {

    // I campi per gli sprite, ora 'final' per una maggiore robustezza.
    private final ArrayList<BufferedImage> bubImages;
    private final ArrayList<BufferedImage> bobImages;
    private final ArrayList<BufferedImage> powImages;
    private final ArrayList<BufferedImage> dynamiteImages;
    
    // La coda per gli eventi in arrivo e l'array (pool) per gli effetti attivi.
    private final List<MinorEvent> eventQueue;
    private final VisualFx[] activeFx;

    /**
     * Il costruttore pubblico carica le immagini e inizializza le strutture dati.
     */
    public VFXManager() {
        // Inizializza le liste di immagini
        this.bubImages = new ArrayList<>();
        this.bobImages = new ArrayList<>();
        this.powImages = new ArrayList<>();
        this.dynamiteImages = new ArrayList<>();
        
        // Inizializza le strutture dati per gli effetti
        this.eventQueue = new ArrayList<>();
        this.activeFx = new VisualFx[MAX_VFX];

        loadImages();
    }

    /**
     * Metodo pubblico chiamato da un gestore esterno (es. PlayingPanel)
     * per richiedere la creazione di un nuovo effetto visivo.
     * @param event L'evento del modello che descrive l'effetto da creare.
     */
    public void addEvent(MinorEvent event) {
        eventQueue.add(event);
    }
    
    /**
     * Aggiorna lo stato di tutti gli effetti.
     * 1. Crea nuovi effetti basandosi sugli eventi in coda.
     * 2. Aggiorna il ciclo di vita di tutti gli effetti già attivi.
     */
    public void update() {
        // Processa gli eventi in coda per creare nuovi effetti
        for (MinorEvent event : eventQueue) {
            generateFx(event);
        }
        eventQueue.clear(); // Svuota la coda dopo averla processata

        // Aggiorna tutti gli effetti visivi attivi e rimuove quelli "morti"
        for (int i = 0; i < activeFx.length; i++) {
            if (activeFx[i] != null) {
                activeFx[i].update();
                if (!activeFx[i].isAlive()) {
                    activeFx[i] = null;
                }
            }
        }
    }

    /**
     * Disegna tutti gli effetti visivi attivi.
     * @param g2 Il contesto grafico su cui disegnare.
     */
    public void draw(Graphics2D g2) {
        for (VisualFx vfx : activeFx) {
            if (vfx != null) {
                vfx.draw(g2);
            }
        }
    }

    /**
     * Metodo privato che smista un evento al corretto costruttore di effetti.
     */
    private void generateFx(MinorEvent event) {
        int freeSlot = findEmptySlot();
        if (freeSlot == -1) return; // Pool di effetti pieno, non ne crea di nuovi

        switch (event.getType()) {
            case "SCORE"    -> activeFx[freeSlot] = generateScoreFx(event, freeSlot);
            case "POW"      -> activeFx[freeSlot] = new PowFx(freeSlot, event.getX(), event.getY(), powImages);
            case "DYNAMITE" -> activeFx[freeSlot] = new DynamiteFx(freeSlot, dynamiteImages);
            // case "POPPIN BUBBLE" -> ... da implementare
        }
    }

    private int findEmptySlot() {
        for (int i = 0; i < activeFx.length; i++) {
            if (activeFx[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private ScoreFx generateScoreFx(MinorEvent event, int index) {
        int imageListIdx = switch (event.getScoredPoints()) {
            case 10 -> 0; case 50 -> 1; case 100 -> 2; case 200 -> 3;
            case 300 -> 4; case 400 -> 5; case 500 -> 6; case 1000 -> 7;
            case 8000 -> 8; case 16000 -> 9; default -> -1;
        };

        if (imageListIdx == -1) return null;

        // Determina se usare gli sprite di Bub o Bob in base al nome utente
        List<BufferedImage> imageList = "bub".equalsIgnoreCase(event.getUser()) ? bubImages : bobImages;
        BufferedImage image = imageList.get(imageListIdx);
        
        return new ScoreFx(index, event.getX(), event.getY(), image);
    }
    
    private void loadImages() {
        ImageLoader.setPath("/fx/pow");
        ImageLoader.setScaling(2);
        powImages.add(ImageLoader.load("1"));
        powImages.add(ImageLoader.load("2"));

        ImageLoader.setPath("/fx/dynamite");
        ImageLoader.setScaling(8);
        dynamiteImages.add(ImageLoader.load("1"));
        dynamiteImages.add(ImageLoader.load("2"));
        dynamiteImages.add(ImageLoader.load("3"));
        dynamiteImages.add(ImageLoader.load("4"));

        ImageLoader.setPath("/score/");
        ImageLoader.setScaling(2);
        bubImages.add(ImageLoader.load("10bub"));
        bubImages.add(ImageLoader.load("50bub"));
        bubImages.add(ImageLoader.load("100bub"));
        bubImages.add(ImageLoader.load("200bub"));
        bubImages.add(ImageLoader.load("300bub"));
        bubImages.add(ImageLoader.load("400bub"));
        bubImages.add(ImageLoader.load("500bub"));
        bobImages.add(ImageLoader.load("10bob"));
        bobImages.add(ImageLoader.load("50bob"));
        bobImages.add(ImageLoader.load("100bob"));
        bobImages.add(ImageLoader.load("200bob"));
        bobImages.add(ImageLoader.load("300bob"));
        bobImages.add(ImageLoader.load("400bob"));
        bobImages.add(ImageLoader.load("500bob"));
        
        ImageLoader.setScalingX(4);
        bubImages.add(ImageLoader.load("1000bub"));
        bubImages.add(ImageLoader.load("8000bub"));
        bobImages.add(ImageLoader.load("1000bob"));
        bobImages.add(ImageLoader.load("8000bob"));
        
        ImageLoader.setScalingX(6);
        bubImages.add(ImageLoader.load("16000bub"));
        bobImages.add(ImageLoader.load("16000bob"));
    }
}