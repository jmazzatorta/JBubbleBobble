package model.users; // <-- NUOVA POSIZIONE CONSIGLIATA

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.managers.ScoreManager;


public class SavingsManager {
    
    private static final String HIGHSCORE_FILE = "savingdatas/highestscore.txt";
    private static final String USERS_FILE = "savingdatas/users.dat"; // Un unico file per tutti gli utenti

    private final UserDatas userDatas;

    public SavingsManager(UserDatas userDatas) {
        this.userDatas = userDatas;
    }

    public void save() {
        saveHighScore();
        saveUsersList();
    }

    public void load() {
        loadHighScore();
        loadUsersList();
    }

    private void saveHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HIGHSCORE_FILE))) {
            bw.write(String.valueOf(ScoreManager.getHighestScore()));
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dell'high score.");
            e.printStackTrace();
        }
    }
    
    private void saveUsersList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(new ArrayList<>(userDatas.getUsersList()));
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio della lista utenti.");
            e.printStackTrace();
        }
    }

    private void loadHighScore() {
        File file = new File(HIGHSCORE_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String scoreStr = br.readLine();
            if (scoreStr != null && !scoreStr.isBlank()) {
                ScoreManager.setHighestScore(Integer.parseInt(scoreStr));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Errore durante il caricamento dell'high score.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsersList() {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                userDatas.setUsersList((List<User>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento della lista utenti.");
            e.printStackTrace();
        }
    }
}