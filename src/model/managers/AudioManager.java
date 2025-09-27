package model.managers;

import model.GameModel;
import model.SoundEffects;
import model.states.GameStates;
import view.interfaces.GameStateListener;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;

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

    public void playSound(SoundEffects effect) {
        Clip clip = clips.get(effect);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playMusic(SoundEffects music) {

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

}