package view.objectsview;

import view.DrawableWrapper;
import view.interfaces.Drawable;
import view.utils.ImageFlipper;
import view.utils.ImageLoader;

import static constants.Constants.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class PlayersView {

    private ArrayList<BufferedImage> sxBubWalking, dxBubWalking, sxBubJumping, dxBubJumping;
    private ArrayList<BufferedImage> sxBubFalling, dxBubFalling, sxBubAttacking, dxBubAttacking;
    private ArrayList<BufferedImage> bubInsideBubble, bubPoppinBubble, bubDying;
    private ArrayList<BufferedImage> sxBobWalking, dxBobWalking, sxBobJumping, dxBobJumping;
    private ArrayList<BufferedImage> sxBobFalling, dxBobFalling, sxBobAttacking, dxBobAttacking;
    private ArrayList<BufferedImage> bobInsideBubble, bobPoppinBubble, bobDying;


    public PlayersView() {
        sxBubWalking = new ArrayList<>(); dxBubWalking = new ArrayList<>(); sxBubJumping = new ArrayList<>(); 
        dxBubJumping = new ArrayList<>(); sxBubFalling = new ArrayList<>(); dxBubFalling = new ArrayList<>();
        sxBubAttacking = new ArrayList<>(); dxBubAttacking = new ArrayList<>(); bubInsideBubble = new ArrayList<>();
        bubPoppinBubble = new ArrayList<>(); bubDying = new ArrayList<>();
        sxBobWalking = new ArrayList<>(); dxBobWalking = new ArrayList<>(); sxBobJumping = new ArrayList<>();
        dxBobJumping = new ArrayList<>(); sxBobFalling = new ArrayList<>(); dxBobFalling = new ArrayList<>();
        sxBobAttacking = new ArrayList<>(); dxBobAttacking = new ArrayList<>(); bobInsideBubble = new ArrayList<>();
        bobPoppinBubble = new ArrayList<>(); bobDying = new ArrayList<>();

        loadImages();
    }

    public void draw(Graphics2D g2, DrawableWrapper wrapper) {
    	Drawable player = wrapper.getDrawable();
    	
        if (player.isFlashing()) { return; }

        SpriteInfo info = getSpriteInfo(wrapper);
        if (info != null && info.image != null) {
            g2.drawImage(info.image,
                         player.getX() + info.xOffset,
                         player.getY() + info.yOffset,
                         null);
        }
    }
    
    private record SpriteInfo(BufferedImage image, int xOffset, int yOffset) {}


    private SpriteInfo getSpriteInfo(DrawableWrapper wrapper) {
    	Drawable player = wrapper.getDrawable();
    	
        String direction = player.getDirectionData();
        String action = player.getActionData();
        if ("DEAD".equals(action)) return null;
        boolean isBub = player.getTypeData().equalsIgnoreCase("bub"); 

        ArrayList<BufferedImage> images = null;
        int xOffset = 0;
        int yOffset = 0;
        
        switch (action) {
    		case "INSIDE BUBBLE" -> {
	            images = isBub ? bubInsideBubble : bobInsideBubble;
	            xOffset = -TILESIZE;
	            yOffset = -TILESIZE * 2;
	        }
	        case "POPPING BUBBLE" -> {
	            images = isBub ? bubPoppinBubble : bobPoppinBubble;
	            xOffset = -TILESIZE;
	            yOffset = -TILESIZE * 2;
	        }
	        case "DYING" -> {
                images = isBub ? bubDying : bobDying;
                yOffset = -TILESIZE * 2;
            }
            case "WALKING" -> images = isBub
                    ? ("RIGHT".equals(direction) ? dxBubWalking : sxBubWalking)
                    : ("RIGHT".equals(direction) ? dxBobWalking : sxBobWalking);
            case "ATTACKING" -> images = isBub
                    ? ("RIGHT".equals(direction) ? dxBubAttacking : sxBubAttacking)
                    : ("RIGHT".equals(direction) ? dxBobAttacking : sxBobAttacking);
            case "JUMPING" -> images = isBub
                    ? ("RIGHT".equals(direction) ? dxBubJumping : sxBubJumping)
                    : ("RIGHT".equals(direction) ? dxBobJumping : sxBobJumping);
            case "FALLING" -> images = isBub
                    ? ("RIGHT".equals(direction) ? dxBubFalling : sxBubFalling)
                    : ("RIGHT".equals(direction) ? dxBobFalling : sxBobFalling);
            case "IDLING" -> images = isBub
                    ? ("RIGHT".equals(direction) ? dxBubWalking : sxBubWalking)
                    : ("RIGHT".equals(direction) ? dxBobWalking : sxBobWalking);
        }
        
        if (images == null || images.isEmpty()) return null;

        int frameIndex = 0;
        
        if (!"IDLING".equals(action)) {
        	frameIndex = wrapper.getCurrentFrame() % images.size();
        	if (frameIndex == images.size()) { wrapper.reset(); frameIndex = 0; }
        }
        
        return new SpriteInfo(images.get(frameIndex), xOffset, yOffset);
    }

    private void loadImages() {
        ArrayList<String> imageNames = new ArrayList<>();
        ImageLoader.setScaling(2);

        Collections.addAll(imageNames, "walking1", "walking2", "walking3", "walking4");
        loadPlayerImages(imageNames, sxBubWalking, sxBobWalking);
        dxBubWalking = flipList(sxBubWalking);
        dxBobWalking = flipList(sxBobWalking);

        Collections.addAll(imageNames, "attacking2");
        loadPlayerImages(imageNames, sxBubAttacking, sxBobAttacking);
        dxBubAttacking = flipList(sxBubAttacking);
        dxBobAttacking = flipList(sxBobAttacking);

        Collections.addAll(imageNames, "jumping1", "jumping2");
        loadPlayerImages(imageNames, sxBubJumping, sxBobJumping);
        dxBubJumping = flipList(sxBubJumping);
        dxBobJumping = flipList(sxBobJumping);

        Collections.addAll(imageNames, "falling1", "falling2");
        loadPlayerImages(imageNames, sxBubFalling, sxBobFalling);
        dxBubFalling = flipList(sxBubFalling);
        dxBobFalling = flipList(sxBobFalling);

        ImageLoader.setScaling(4);
        Collections.addAll(imageNames, "bubble1", "bubble2", "bubble3", "bubble4");
        loadPlayerImages(imageNames, bubInsideBubble, bobInsideBubble);

        Collections.addAll(imageNames, "bubble5", "bubble6", "bubble7", "bubble8", "bubble9", "bubble10");
        loadPlayerImages(imageNames, bubPoppinBubble, bobPoppinBubble);

        ImageLoader.setScalingX(2);
        ImageLoader.setScalingY(4);
        Collections.addAll(imageNames, "dying1", "dying2", "dying3", "dying4", "dying5",
                "dying6", "dying7", "dying8", "dying9", "dying10");
        loadPlayerImages(imageNames, bubDying, bobDying);
    }

    private void loadPlayerImages(ArrayList<String> names, ArrayList<BufferedImage> bubList, ArrayList<BufferedImage> bobList) {
        ImageLoader.setPath("/player/bub");
        names.stream().map(ImageLoader::load).forEachOrdered(bubList::add);

        ImageLoader.setPath("/player/bob");
        names.stream().map(ImageLoader::load).forEachOrdered(bobList::add);

        names.clear();
    }

    private ArrayList<BufferedImage> flipList(ArrayList<BufferedImage> input) {
        return input.stream()
                .map(ImageFlipper::createFlipped)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}