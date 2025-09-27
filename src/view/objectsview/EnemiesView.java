package view.objectsview;

import view.DrawableWrapper;
import view.interfaces.Drawable;
import view.utils.ImageFlipper;
import view.utils.ImageLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Gestisce il caricamento e il disegno degli sprite dei nemici (rifattorizzata).
 * Carica le immagini nel costruttore e offre un metodo 'draw'.
 */
public class EnemiesView {


    // Zenchan images
    private final ArrayList<BufferedImage> sxZenchan, dxZenchan;
    private final ArrayList<BufferedImage> sxZenchanAngry, dxZenchanAngry;
    private final ArrayList<BufferedImage> sxZenchanDying, dxZenchanDying;
    // Monsta images
    private final ArrayList<BufferedImage> sxMonsta, dxMonsta;
    private final ArrayList<BufferedImage> sxMonstaAngry, dxMonstaAngry;
    private final ArrayList<BufferedImage> sxMonstaDying, dxMonstaDying;
    // Maita images
    private final ArrayList<BufferedImage> sxMaita, dxMaita;
    private final ArrayList<BufferedImage> sxMaitaAngry, dxMaitaAngry;
    private final ArrayList<BufferedImage> sxMaitaFalling, dxMaitaFalling;
    private final ArrayList<BufferedImage> sxMaitaAngryFalling, dxMaitaAngryFalling;
    private final ArrayList<BufferedImage> sxMaitaDying, dxMaitaDying;

    /**
     * Il costruttore pubblico carica e inizializza tutte le immagini.
     */
    public EnemiesView() {
        // La logica di caricamento è stata spostata qui dal vecchio metodo privato.
        // Inizializziamo e assegniamo le liste una sola volta.
        ArrayList<String> imageNames = new ArrayList<>();
        ImageLoader.setPath("/enemies/");
        ImageLoader.setScaling(2);

        // Zenchan
        sxZenchan = new ArrayList<>();
        Collections.addAll(imageNames, "zenchanwalking1", "zenchanwalking2", "zenchanwalking3", "zenchanwalking4");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxZenchan::add);
        imageNames.clear();
        dxZenchan = flipImages(sxZenchan);

        sxZenchanAngry = new ArrayList<>();
        Collections.addAll(imageNames, "zenchanangry1", "zenchanangry2", "zenchanangry3", "zenchanangry4");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxZenchanAngry::add);
        imageNames.clear();
        dxZenchanAngry = flipImages(sxZenchanAngry);

        sxZenchanDying = new ArrayList<>();
        Collections.addAll(imageNames, "zenchandying1", "zenchandying4", "zenchandying2", "zenchandying3");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxZenchanDying::add);
        imageNames.clear();
        dxZenchanDying = flipImages(sxZenchanDying);

        // Monsta
        sxMonsta = new ArrayList<>();
        Collections.addAll(imageNames, "monsta1", "monsta2");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMonsta::add);
        imageNames.clear();
        dxMonsta = flipImages(sxMonsta);

        sxMonstaAngry = new ArrayList<>();
        Collections.addAll(imageNames, "monstaangry1", "monstaangry2");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMonstaAngry::add);
        imageNames.clear();
        dxMonstaAngry = flipImages(sxMonstaAngry);

        sxMonstaDying = new ArrayList<>();
        Collections.addAll(imageNames, "monstadying1", "monstadying2", "monstadying3", "monstadying4");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMonstaDying::add);
        imageNames.clear();
        dxMonstaDying = flipImages(sxMonstaDying);

        // Maita
        sxMaita = new ArrayList<>();
        Collections.addAll(imageNames, "maitawalking1", "maitawalking2");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMaita::add);
        imageNames.clear();
        dxMaita = flipImages(sxMaita);
        
        sxMaitaAngry = new ArrayList<>();
        Collections.addAll(imageNames, "maitaangrywalking1", "maitaangrywalking2");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMaitaAngry::add);
        imageNames.clear();
        dxMaitaAngry = flipImages(sxMaitaAngry);

        sxMaitaFalling = new ArrayList<>();
        Collections.addAll(imageNames, "maitafalling1", "maitafalling2");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMaitaFalling::add);
        imageNames.clear();
        dxMaitaFalling = flipImages(sxMaitaFalling);

        sxMaitaAngryFalling = new ArrayList<>();
        Collections.addAll(imageNames, "maitaangryfalling1", "maitaangryfalling2");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMaitaAngryFalling::add);
        imageNames.clear();
        dxMaitaAngryFalling = flipImages(sxMaitaAngryFalling);
        
        sxMaitaDying = new ArrayList<>();
        Collections.addAll(imageNames, "maitadying1", "maitadying2", "maitadying3", "maitadying4");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(sxMaitaDying::add);
        imageNames.clear();
        dxMaitaDying = flipImages(sxMaitaDying);
    }

    private ArrayList<BufferedImage> flipImages(ArrayList<BufferedImage> input) {
        return input.stream()
                .map(ImageFlipper::createFlipped)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public void draw(Graphics2D g2, DrawableWrapper wrapper) {
    	Drawable enemy = wrapper.getDrawable();
    	
        BufferedImage sprite = getSprite(wrapper);
        if (sprite != null) {
            g2.drawImage(sprite, enemy.getX(), enemy.getY(), null);
        }
    }

    /**
     * Seleziona lo sprite corretto da disegnare in base al tipo, azione e direzione del nemico.
     */
    private BufferedImage getSprite(DrawableWrapper wrapper) {
    	Drawable enemy = wrapper.getDrawable();
    	
        String type = enemy.getTypeData();
        ArrayList<BufferedImage> images = switch (type) {
            case "ZENCHAN" -> getZenchanImages(enemy);
            case "MONSTA" -> getMonstaImages(enemy);
            case "MAITA" -> getMaitaImages(enemy);
            default -> null;
        };

        if (images == null || images.isEmpty()) {
            return null;
        }        
        
        return images.get(wrapper.getCurrentFrame() % images.size());
    }

    // --- METODI HELPER PER SELEZIONARE LA LISTA DI IMMAGINI CORRETTA ---

    private ArrayList<BufferedImage> getZenchanImages(Drawable enemy) {
        String direction = enemy.getDirectionData();
        String action = enemy.getActionData();
        return switch (action) {
        	case "DYING" -> "RIGHT".equals(direction) ? dxZenchanDying : sxZenchanDying;
            case "MOVING" -> "RIGHT".equals(direction) ? dxZenchan : sxZenchan;
            case "ANGRY MOVING" -> "RIGHT".equals(direction) ? dxZenchanAngry : sxZenchanAngry;
            default -> "RIGHT".equals(direction) ? dxZenchan : sxZenchan; // Default a walking
        };
    }

    private ArrayList<BufferedImage> getMonstaImages(Drawable enemy) {
        String direction = enemy.getDirectionData();
        String action = enemy.getActionData();
        return switch (action) {
        	case "DYING" -> "RIGHT".equals(direction) ? dxMonstaDying : sxMonstaDying;
            case "MOVING" -> "RIGHT".equals(direction) ? dxMonsta : sxMonsta;
            case "ANGRY MOVING" -> "RIGHT".equals(direction) ? dxMonstaAngry : sxMonstaAngry;
            default -> "RIGHT".equals(direction) ? dxMonsta : sxMonsta;
        };
    }

    private ArrayList<BufferedImage> getMaitaImages(Drawable enemy) {
        String direction = enemy.getDirectionData();
        String action = enemy.getActionData();
        return switch (action) {
        	case "DYING" -> "RIGHT".equals(direction) ? dxMaitaDying : sxMaitaDying;
            case "MOVING" -> "RIGHT".equals(direction) ? dxMaita : sxMaita;
            case "ANGRY MOVING" -> "RIGHT".equals(direction) ? dxMaitaAngry : sxMaitaAngry;
            case "JUMPING" -> "RIGHT".equals(direction) ? dxMaitaFalling : sxMaitaFalling;
            case "ANGRY JUMPING" -> "RIGHT".equals(direction) ? dxMaitaAngryFalling : sxMaitaAngryFalling;
            default -> "RIGHT".equals(direction) ? dxMaita : sxMaita;
        };
    }
}