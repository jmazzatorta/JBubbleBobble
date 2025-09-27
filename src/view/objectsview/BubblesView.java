package view.objectsview;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import view.DrawableWrapper;
import view.interfaces.Drawable;
import view.utils.ImageLoader;


public class BubblesView {

	private final ArrayList<BufferedImage> bubBubbleImages, bobBubbleImages;
    private final ArrayList<BufferedImage> bubProjectileImages, bobProjectileImages;
    private final ArrayList<BufferedImage> bubZenchanImages, bobZenchanImages;
    private final ArrayList<BufferedImage> bubMonstaImages, bobMonstaImages;
    private final ArrayList<BufferedImage> bubMaitaImages, bobMaitaImages;
    private final ArrayList<BufferedImage> fireBubImages, fireBobImages;
    private final ArrayList<BufferedImage> thunderBubImages, thunderBobImages;
    private final ArrayList<BufferedImage> popImages;
    private final Random r = new Random(); 


    public BubblesView() {
    	this.bubBubbleImages = new ArrayList<>();
        this.bobBubbleImages = new ArrayList<>();
        this.bubProjectileImages = new ArrayList<>();
        this.bobProjectileImages = new ArrayList<>();
        this.bubZenchanImages = new ArrayList<>();
        this.bobZenchanImages = new ArrayList<>();
        this.bubMonstaImages = new ArrayList<>();
        this.bobMonstaImages = new ArrayList<>();
        this.bubMaitaImages = new ArrayList<>();
        this.bobMaitaImages = new ArrayList<>();
        this.fireBubImages = new ArrayList<>();
        this.fireBobImages = new ArrayList<>();
        this.thunderBubImages = new ArrayList<>();
        this.thunderBobImages = new ArrayList<>();
        this.popImages = new ArrayList<>();

        loadImages();
    }

    public void draw(Graphics2D g2, DrawableWrapper wrapper) {
    	Drawable bubble = wrapper.getDrawable();
    	
        BufferedImage sprite = getSprite(wrapper);
        if (sprite != null) {
            g2.drawImage(sprite, bubble.getX(), bubble.getY(), null);
        }
    }

    private BufferedImage getSprite(DrawableWrapper wrapper) {
    	Drawable bubble = wrapper.getDrawable();
    	
        String type = bubble.getTypeData();
        String action = bubble.getActionData();

        ArrayList<BufferedImage> images = null;

        switch (type) {
            case "bub":
                images = switch (action) {
                	case "MOVING" -> bubBubbleImages;
                	case "JUMPED" -> bubBubbleImages;
                    case "PROJECTILE" -> bubProjectileImages;
                    case "TRAPPING ZENCHAN" -> bubZenchanImages;
                    case "TRAPPING MONSTA" -> bubMonstaImages;
                    case "TRAPPING MAITA" -> bubMaitaImages;
                    default -> null;
                };
                break;
            case "bob":
                images = switch (action) {
                	case "MOVING" -> bobBubbleImages;
                	case "JUMPED" -> bobBubbleImages;
                    case "PROJECTILE" -> bobProjectileImages;
                    case "TRAPPING ZENCHAN" -> bobZenchanImages;
                    case "TRAPPING MONSTA" -> bobMonstaImages;
                    case "TRAPPING MAITA" -> bobMaitaImages;
                    default -> null;
                };
                break;
            case "FIRE":
                images = fireBubImages;
                break;
            case "THUNDER":
                images = thunderBobImages;
                break;
            case "POPPING":
                images = popImages;
                break;
        }

        if (images == null || images.isEmpty()) {
            return null;
        }
               
        int frameIndex = 0;
        
        if (!"MOVING".equals(action)) {
        	frameIndex = wrapper.getCurrentFrame() % images.size();
        	if (frameIndex == images.size()) { wrapper.reset(); frameIndex = 0; }
        }
        
        return images.get(frameIndex);
    }

    // --- METODI PER IL CARICAMENTO DELLE IMMAGINI (INVARIATI) ---

    private void loadImages() {
        ArrayList<String> imageNames = new ArrayList<>();
        ImageLoader.setScaling(2);

     // Bolle proiettile
        Collections.addAll(imageNames, "bubble1", "bubble2", "bubble3");
        ImageLoader.setPath("/bubbles/bub");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bubBubbleImages::add);
        ImageLoader.setPath("/bubbles/bob");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bobBubbleImages::add);
        imageNames.clear();
        
        // Bolle proiettile
        Collections.addAll(imageNames, "projectile1", "projectile2", "projectile3", "projectile4", "projectile5", "projectile6");
        ImageLoader.setPath("/bubbles/bub");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bubProjectileImages::add);
        ImageLoader.setPath("/bubbles/bob");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bobProjectileImages::add);
        imageNames.clear();

        // Bolle con Zenchan
        Collections.addAll(imageNames, "zenchan1", "zenchan2", "zenchan3");
        ImageLoader.setPath("/bubbles/bub");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bubZenchanImages::add);
        ImageLoader.setPath("/bubbles/bob");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bobZenchanImages::add);
        imageNames.clear();

        // Bolle con Monsta
        Collections.addAll(imageNames, "monsta1", "monsta2", "monsta3");
        ImageLoader.setPath("/bubbles/bub");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bubMonstaImages::add);
        ImageLoader.setPath("/bubbles/bob");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bobMonstaImages::add);
        imageNames.clear();

        // Bolle con Maita
        Collections.addAll(imageNames, "maita1", "maita2", "maita3");
        ImageLoader.setPath("/bubbles/bub");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bubMaitaImages::add);
        ImageLoader.setPath("/bubbles/bob");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(bobMaitaImages::add);
        imageNames.clear();

        // Bolle di fuoco
        Collections.addAll(imageNames, "fire1", "fire2", "fire3");
        ImageLoader.setPath("/bubbles/special/bub");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(fireBubImages::add);
        ImageLoader.setPath("/bubbles/special/bob");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(fireBobImages::add);
        imageNames.clear();

        // Bolle di tuono
        Collections.addAll(imageNames, "thunder1", "thunder2", "thunder3");
        ImageLoader.setPath("/bubbles/special/bub");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(thunderBubImages::add);
        ImageLoader.setPath("/bubbles/special/bob");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(thunderBobImages::add);
        imageNames.clear();

        // Poppin
        ImageLoader.setPath("/bubbles/");
        Collections.addAll(imageNames, "bubblepoppin1", "bubblepoppin2", "bubblepoppin3");
        imageNames.stream().map(ImageLoader::load).forEachOrdered(popImages::add);
        imageNames.clear();
    }
}