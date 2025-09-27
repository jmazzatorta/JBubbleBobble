package model.managers;

import static constants.Constants.*;

import model.states.GameStates;
import model.states.StateManager;
import model.users.SavingsManager;
import model.users.User;
import model.users.UserDatas;
import model.users.UserDatas.UserBuilder;
import model.utils.DirectionX;
import model.utils.DirectionY;

public class StatisticsManager {
    
    private final PlayersManager playersManager;
    private final UserDatas userDatas;
    private final LevelManager levelManager;
    private final StateManager stateManager;
    private final SavingsManager savingsManager;

    private CurrentPlayer currentPlayer = CurrentPlayer.P1;
    private Phases currentPhase = Phases.INTRO;
    private UserBuilder userBuilder;
    private User createdUser;
    
    private boolean endPhase;
    
    private final StringBuilder nameBuilder = new StringBuilder();
    private Character typedChar;
    private int lastCharIndex = -1;
    
    private final AvatarButton[] buttonsArray = new AvatarButton[AVATARS_NUM];
    private int avatarIndex;
    private boolean moveLeft, moveRight, moveUp, moveDown;

    public StatisticsManager(PlayersManager playersManager, UserDatas userDatas, 
                             LevelManager levelManager, StateManager stateManager, SavingsManager savingsManager) {
        this.playersManager = playersManager;
        this.userDatas = userDatas;
        this.levelManager = levelManager;
        this.stateManager = stateManager;
        this.savingsManager = savingsManager;
        
        setButtons();
        this.savingsManager.load();
    }
    
    private void setButtons() {
        int x = SCREENWIDTH / 5;
        int y = SCREENHEIGHT / 2;
        for (int i = 0; i < AVATARS_NUM; i++) {
            if (i == 4) y += TILESIZE * 4;
            buttonsArray[i] = new AvatarButton(i, x - TILESIZE + x * (i % 4), y);
        }
    }
    
    public void update() {
        if (endPhase) {
            finalizePhase();
            endPhase = false;
            return;
        }
        
        switch (currentPhase) {
            case NAME_SELECTION:
                if (typedChar != null) {
                    if (nameBuilder.length() < 9) {
                        nameBuilder.append(typedChar);
                        lastCharIndex++;
                    }
                    typedChar = null;
                }
                break; 

            case AVATAR_SELECTION:
                if (moveLeft && avatarIndex > 0) { avatarIndex--; moveLeft = false; } 
                else if (moveRight && avatarIndex < 7) { avatarIndex++; moveRight = false; } 
                else if (moveUp && avatarIndex > 3) { avatarIndex -= 4; moveUp = false; } 
                else if (moveDown && avatarIndex < 4) { avatarIndex += 4; moveDown = false; }
                break;
                
            default:
                break;
        }
    }

    public void finalizePhase() {
        switch (currentPhase) {
            case INTRO:
                userBuilder = userDatas.new UserBuilder();
                userBuilder.setScore(setUserScore());
                nextPhase();
                break;
            case NAME_SELECTION:
                userBuilder.setName(nameBuilder.toString());
                nextPhase();
                break;
            case AVATAR_SELECTION:
                userBuilder.setAvatarIndex(avatarIndex);
                createdUser = userBuilder.createUser();
                savingsManager.save();
                nextPhase();
                break;
            case FINALIZATION:
            case STATS_DISPLAY:
                nextPhase();
                break;
        }
    }
    
    private void nextPhase() {
        switch(currentPhase) {
            case INTRO:
                currentPhase = Phases.NAME_SELECTION;
                break;
            case NAME_SELECTION:
                currentPhase = Phases.AVATAR_SELECTION;
                break;
            case AVATAR_SELECTION:
                currentPhase = Phases.FINALIZATION;
                break;
            case FINALIZATION:
                if (playersManager.hasTwoPlayers() && currentPlayer == CurrentPlayer.P1) {
                    currentPlayer = CurrentPlayer.P2;
                    resetUserStats();
                } else {
                    currentPhase = Phases.STATS_DISPLAY;
                }
                break;
            case STATS_DISPLAY:
                levelManager.resetGame();
                stateManager.setState(GameStates.INTRO);
                break;
        }
        
    }
    
    private void resetUserStats() {
        currentPhase = Phases.INTRO;
        nameBuilder.setLength(0);
        avatarIndex = 0;
        lastCharIndex = -1;
        createdUser = null;
    }

    private int setUserScore() {
        if (currentPlayer == CurrentPlayer.P1 && playersManager.getP1() != null) {
            return playersManager.getP1().getScore();
        } else if (currentPlayer == CurrentPlayer.P2 && playersManager.getP2() != null) {
            return playersManager.getP2().getScore();
        }
        return 0;
    }
    
    public void moveSelection(DirectionX direction) { if(direction == DirectionX.LEFT) moveLeft=true; else moveRight=true; }
    public void moveSelection(DirectionY direction) { if(direction == DirectionY.UP) moveUp=true; else moveDown=true; }
    public void setTypedChar(char c) { typedChar = c; }
    public void deleteChar() {
        if (lastCharIndex >= 0) {
            nameBuilder.deleteCharAt(lastCharIndex);
            lastCharIndex--;
        }
    }
    public void endPhase() { endPhase = true; }
    
    public AvatarButton[] getAvatarButtons() { return buttonsArray; }
    public AvatarButton getSelectedButton() { return buttonsArray[avatarIndex]; }
    public CurrentPlayer getCurrentPlayer() { return currentPlayer; }
    public Phases getCreationPhase() { return currentPhase; }
    public String getTypedString() { return nameBuilder.toString(); }
    public User getCreatedUser() { return createdUser; }
    
    public enum Phases { INTRO, AVATAR_SELECTION, NAME_SELECTION, FINALIZATION, STATS_DISPLAY; }
    public enum CurrentPlayer { P1, P2; }
    
    public class AvatarButton {
        private final int avatarIndex;
        private final int x, y;
        
        private AvatarButton(int avatarIndex, int x, int y) {
            this.avatarIndex = avatarIndex;
            this.x = x;
            this.y = y;
        }
        
        public int getIndex() { return avatarIndex; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
}