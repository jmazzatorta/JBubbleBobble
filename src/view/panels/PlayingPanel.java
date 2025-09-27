package view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import model.GameModel;
import model.interfaces.Updatable;
import model.utils.MinorEvent;
import view.DrawableWrapper;
import view.GUI;
import view.interfaces.Drawable;
import view.objectsview.*;
import view.visualFx.VFXManager;

import static constants.Constants.*;

@SuppressWarnings("serial")
public class PlayingPanel extends JPanel {

    private final TilesView tilesView;
    private final ItemsView itemsView;
    private final EnemiesView enemiesView;
    private final PlayersView playersView;
    private final BubblesView bubblesView;
    private final ProjectilesView projectilesView;
    private final VFXManager vfxManager;
    private final GUI gui;
    private final Map<Updatable, DrawableWrapper> drawableEntities;

    public PlayingPanel(GameModel model) {
        this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.drawableEntities = new ConcurrentHashMap<>();
        
        this.playersView = new PlayersView();
        this.enemiesView = new EnemiesView();
        this.itemsView = new ItemsView();
        this.bubblesView = new BubblesView();
        this.projectilesView = new ProjectilesView();
        this.tilesView = new TilesView(model.getTilesManager());
        this.gui = new GUI(model);
        this.vfxManager = new VFXManager();
    }

    public void update() {
        vfxManager.update();
        gui.update();
        
        for (DrawableWrapper wrapper : drawableEntities.values()) {
            wrapper.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        // 1. Disegna la mappa
        tilesView.draw(g2);
        // 2. Disegna tutte le entità dinamiche
        for (DrawableWrapper wrapper : drawableEntities.values()) {
            drawEntity(g2, wrapper);
        }
        
        // 3. Disegna gli effetti speciali
        vfxManager.draw(g2);
        
        // 4. Disegna l'interfaccia utente
        gui.draw(g2);
        
        g2.dispose();
    }
    
    private void drawEntity(Graphics2D g2, DrawableWrapper wrapper) {
        Drawable entity = wrapper.getDrawable();
        
        if (entity instanceof model.entities.players.Player) playersView.draw(g2, wrapper);
        else if (entity instanceof model.entities.enemies.Enemy) enemiesView.draw(g2, wrapper);
        else if (entity instanceof model.items.Item) itemsView.draw(g2, entity);
        else if (entity instanceof model.bubbles.Bubble) bubblesView.draw(g2, wrapper);
        else if (entity instanceof model.projectiles.Projectile) projectilesView.draw(g2, wrapper);
    }
    
    // --- Metodi dei Listener (inoltrati da GameFrame) ---

    public void onEntityAdded(Updatable entity) {
    	if (entity instanceof Drawable) {
            drawableEntities.put(entity, new DrawableWrapper((Drawable) entity));
        }
    }

    public void onEntityRemoved(Updatable entity) {
        drawableEntities.remove(entity);
    }
    
    public void onMinorEvent(MinorEvent event) {
        vfxManager.addEvent(event);
    }
    
    //public void onLevelChangeStart(Stack<int[]> nextLevelRows) {
    //    tilesView.startLevelTransition(nextLevelRows);
    //}
}