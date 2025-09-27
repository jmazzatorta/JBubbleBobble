package view.objectsview;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import view.DrawableWrapper;
import view.interfaces.Drawable;
import view.utils.ImageLoader;

public class ItemsView {

    private final ArrayList<BufferedImage> foodImages;
    private final ArrayList<BufferedImage> specialItemsImages;


    public ItemsView() {
        this.foodImages = new ArrayList<>();
        this.specialItemsImages = new ArrayList<>();
        loadImages();
    }

    public void draw(Graphics2D g2, Drawable item) {
        BufferedImage sprite = getSprite(item);
        if (sprite != null) {
            g2.drawImage(sprite, item.getX(), item.getY(), null);
        }
    }

    private BufferedImage getSprite(Drawable item) {
        
        return switch (item.getTypeData()) {
        case "GREENPEPPER" -> foodImages.get(0);
        case "REDTURNIP"   -> foodImages.get(1);
        case "CUCUMBER"    -> foodImages.get(2);
        case "CORN"        -> foodImages.get(3);
        case "PERSIMMON"   -> foodImages.get(4);
        case "PEACH"       -> foodImages.get(5);
        case "BANANA"      -> foodImages.get(6);
        case "FRENCHFRIES" -> foodImages.get(7);
        case "CRYSTAL"     -> foodImages.get(8);
        case "BIGWATERMELON" -> foodImages.get(9);
        case "PINKCANDY"    -> specialItemsImages.get(0);
        case "BLUECANDY"    -> specialItemsImages.get(1);
        case "YELLOWCANDY"  -> specialItemsImages.get(2);
        case "SHOES"        -> specialItemsImages.get(3);
        case "CLOCK"        -> specialItemsImages.get(4);
        case "DYNAMITE"     -> specialItemsImages.get(5);
        case "CRYSTALRING"  -> specialItemsImages.get(6);
        case "AMETHYSTRING" -> specialItemsImages.get(7);
        case "RUBYRING"     -> specialItemsImages.get(8);
        default -> null;
        };
    }

    private void loadImages() {
        ArrayList<String> imageNames = new ArrayList<>();
        
        // Food images
        ImageLoader.setPath("/items/");
        ImageLoader.setScaling(2);
        
        Collections.addAll(imageNames, "greenpepper", "redturnip", "cucumber", "corn", "persimmon", "peach", "banana", "frenchfries", "crystal");
        imageNames.stream()
                .map(ImageLoader::load)
                .forEachOrdered(foodImages::add);
        imageNames.clear();
        
        ImageLoader.setScaling(4);
        foodImages.add(ImageLoader.load("bigwatermelon"));
        
        // Special Items images
        ImageLoader.setScaling(2);
        
        Collections.addAll(imageNames, "pinkcandy", "bluecandy", "yellowcandy", "shoes", "clock", "dynamite", "crystalring", "amethystring", "rubyring");
        imageNames.stream()
                .map(ImageLoader::load)
                .forEachOrdered(specialItemsImages::add);
        imageNames.clear();
    }
}