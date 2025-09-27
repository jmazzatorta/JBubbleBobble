package model;

/**
 * Un catalogo di tutti i file audio del gioco.
 * Questa enum ora contiene solo il percorso della risorsa, senza logica di riproduzione.
 */
public enum SoundEffects {
    MAIN_THEME("/audio/maintheme.wav"),
    GAMEOVER_THEME("/audio/gameover.wav"),
    STATS_THEME("/audio/nameregister.wav"),
    INTRO("/audio/intro.wav"),
    JUMP("/audio/jump.wav"),
    ITEM("/audio/itemcollision.wav"),
    BUBBLE("/audio/bubble.wav"),
    DEATH("/audio/dying.wav"),
    ENEMY_DEATH("/audio/enemydying1.wav"); // Semplificato per l'esempio

    private final String filePath;

    SoundEffects(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}