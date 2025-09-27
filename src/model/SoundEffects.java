package model;

public enum SoundEffects {
    MAIN_THEME("/audio/maintheme.wav"),
    GAMEOVER_THEME("/audio/gameover.wav"),
    STATS_THEME("/audio/nameregister.wav"),
    INTRO("/audio/intro.wav"),
    JUMP("/audio/jump.wav"),
    ITEM("/audio/itemcollision.wav"),
    BUBBLE("/audio/bubble.wav"),
    DEATH("/audio/dying.wav"),
    ENEMY_DEATH("/audio/enemydying1.wav"); 

    private final String filePath;

    SoundEffects(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}