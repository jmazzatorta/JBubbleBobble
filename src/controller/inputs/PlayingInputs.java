package controller.inputs;

import model.GameModel;
import model.entities.players.Player;
import model.utils.DirectionX;

public class PlayingInputs {
    
    private final GameModel model;
    private boolean moveSxPressed = false;
    private boolean moveDxPressed = false;
    private boolean p2_moveSxPressed = false;
    private boolean p2_moveDxPressed = false;

    public PlayingInputs(GameModel model) {
        this.model = model;
    }

    // --- METODI PER IL GIOCATORE 1 ---

    public void p1_moveLeft_press() {
        Player p1 = model.getPlayersManager().getP1();
        if (p1 != null) {
            p1.move(DirectionX.LEFT);
            moveSxPressed = true;
        }
    }

    public void p1_moveLeft_release() {
        Player p1 = model.getPlayersManager().getP1();
        if (p1 != null) {
            moveSxPressed = false;
            if (!moveDxPressed) {
                p1.stopMoving();
            }
        }
    }

    public void p1_moveRight_press() {
        Player p1 = model.getPlayersManager().getP1();
        if (p1 != null) {
            p1.move(DirectionX.RIGHT);
            moveDxPressed = true;
        }
    }

    public void p1_moveRight_release() {
        Player p1 = model.getPlayersManager().getP1();
        if (p1 != null) {
            moveDxPressed = false;
            if (!moveSxPressed) {
                p1.stopMoving();
            }
        }
    }

    public void p1_jump() {    	
        Player p1 = model.getPlayersManager().getP1();
        if (p1 != null) p1.jump();
    }

    public void p1_attack_press() {
        Player p1 = model.getPlayersManager().getP1();
        if (p1 != null) p1.attack();
    }

    public void p1_attack_release() {
        Player p1 = model.getPlayersManager().getP1();
        if (p1 != null) p1.stopAttacking();
    }
    
 // --- METODI PER IL GIOCATORE 2 ---

    public void p2_moveLeft_press() {
        Player p2 = model.getPlayersManager().getP2();
        if (p2 != null) {
            p2.move(DirectionX.LEFT);
            p2_moveSxPressed = true;
        }
    }

    public void p2_moveLeft_release() {
        Player p2 = model.getPlayersManager().getP2();
        if (p2 != null) {
            p2_moveSxPressed = false;
            if (!p2_moveDxPressed) {
                p2.stopMoving();
            }
        }
    }

    public void p2_moveRight_press() {
        Player p2 = model.getPlayersManager().getP2();
        if (p2 != null) {
            p2.move(DirectionX.RIGHT);
            p2_moveDxPressed = true;
        }
    }

    public void p2_moveRight_release() {
        Player p2 = model.getPlayersManager().getP2();
        if (p2 != null) {
            p2_moveDxPressed = false;
            if (!p2_moveSxPressed) {
                p2.stopMoving();
            }
        }
    }

    public void p2_jump() {
        Player p2 = model.getPlayersManager().getP2();
        if (p2 != null) {
            p2.jump();
        }
    }

    public void p2_attack_press() {
        Player p2 = model.getPlayersManager().getP2();
        if (p2 != null) {
            p2.attack(); // O p2.attack(), a seconda della tua implementazione
        }
    }

    public void p2_attack_release() {
        Player p2 = model.getPlayersManager().getP2();
        if (p2 != null) {
            p2.stopAttacking();
        }
    }

    // --- ALTRI METODI ---

    public void addPlayer() {
        if (!model.getLevelManager().isInitialising()) {
            model.getPlayersManager().addPlayer("bob");
        }
    }

}