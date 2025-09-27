package view.objectsview;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import view.DrawableWrapper;
import view.interfaces.Drawable;
import view.utils.ImageLoader;


public class ProjectilesView {

    private final ArrayList<BufferedImage> maitaImages;
    private final ArrayList<BufferedImage> maitaCrashImages;
    private final ArrayList<BufferedImage> thunderImages;
    private final ArrayList<BufferedImage> fireImages;

    public ProjectilesView() {

        this.maitaImages = new ArrayList<>();
        this.maitaCrashImages = new ArrayList<>();
        this.thunderImages = new ArrayList<>();
        this.fireImages = new ArrayList<>();
        loadImages();
    }

    public void draw(Graphics2D g2, DrawableWrapper wrapper) {
    	Drawable projectile = wrapper.getDrawable();
    	
        BufferedImage sprite = getSprite(wrapper);
        if (sprite != null) {
            g2.drawImage(sprite, projectile.getX(), projectile.getY(), null);
        }
    }

    private BufferedImage getSprite(DrawableWrapper wrapper) {
    	Drawable projectile = wrapper.getDrawable();
    	
        String type = projectile.getTypeData();
        String action = projectile.getActionData();

        ArrayList<BufferedImage> images = switch (type) {
            case "MAITA" -> "EXPLODING".equals(action) ? maitaCrashImages : maitaImages;
            case "FIRE" -> fireImages;
            case "THUNDER" -> thunderImages;
            default -> null;
        };

        if (images == null || images.isEmpty()) {
            return null;
        }
        
        return images.get(wrapper.getCurrentFrame() % images.size());
    }


    private void loadImages() {
        ArrayList<String> imageNames = new ArrayList<>();
        ImageLoader.setScaling(2);

        ImageLoader.setPath("/projectiles/");
        Collections.addAll(imageNames, "maita1", "maita2", "maita3", "maita3", "maita4");
        imageNames.stream()
                .map(ImageLoader::load)
                .forEachOrdered(maitaImages::add);
        imageNames.clear();

        ImageLoader.setPath("/projectiles/");
        Collections.addAll(imageNames, "maitacrash1", "maitacrash2", "maitacrash3");
        imageNames.stream()
                .map(ImageLoader::load)
                .forEachOrdered(maitaCrashImages::add);
        imageNames.clear();

        ImageLoader.setPath("/projectiles/");
        Collections.addAll(imageNames, "fire1", "fire2", "fire3");
        imageNames.stream()
                .map(ImageLoader::load)
                .forEachOrdered(fireImages::add);
        imageNames.clear();

        ImageLoader.setPath("/projectiles/");
        Collections.addAll(imageNames, "thunder1", "thunder2", "thunder3");
        imageNames.stream()
                .map(ImageLoader::load)
                .forEachOrdered(thunderImages::add);
        imageNames.clear();
    }
}