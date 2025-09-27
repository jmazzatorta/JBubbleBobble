package model.managers;

import model.GameModel;
import model.SoundEffects;
import model.states.GameStates;
import view.interfaces.GameStateListener;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Stack;

/**
 * Gestore audio rifattorizzato.
 * Pre-carica tutti i suoni all'avvio per efficienza.
 * Implementa GameStateListener per cambiare la musica in modo reattivo.
 */
public class AudioManager implements GameStateListener {

    private final EnumMap<SoundEffects, Clip> clips;
    private Clip currentMusic;

    public AudioManager() {
        clips = new EnumMap<>(SoundEffects.class);
        loadAllSounds();
    }

    private void loadAllSounds() {
        for (SoundEffects sound : SoundEffects.values()) {
            try {
                URL url = getClass().getResource(sound.getFilePath());
                if (url == null) {
                    System.err.println("File audio non trovato: " + sound.getFilePath());
                    continue;
                }
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clips.put(sound, clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Riproduce un effetto sonoro breve.
     * @param effect L'effetto da riprodurre.
     */
    public void playSound(SoundEffects effect) {
        Clip clip = clips.get(effect);
        if (clip != null) {
            // Se sta già suonando, lo ferma e lo fa ripartire dall'inizio
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * Riproduce una traccia musicale in loop, fermando quella precedente.
     * @param music La musica da riprodurre.
     */
    public void playMusic(SoundEffects music) {
        // Ferma la musica corrente se ce n'è una
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
        
        currentMusic = clips.get(music);
        if (currentMusic != null) {
            currentMusic.setFramePosition(0);
            currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stopMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
    }

    @Override
    public void onGameStateChanged(GameStates newState) {
        stopMusic();
        
        switch (newState) {
            case INTRO -> playMusic(SoundEffects.INTRO);
            case PLAYING -> playMusic(SoundEffects.MAIN_THEME);
            case STATS_MANAGEMENT -> playMusic(SoundEffects.STATS_THEME);
            case GAMEOVER -> playSound(SoundEffects.GAMEOVER_THEME);
            default -> {}
        }
    }

	@Override
	public void onLevelChangeStart(Stack<int[]> nextLevelRows) {
		// TODO Auto-generated method stub
		
	}
}