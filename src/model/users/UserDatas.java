package model.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static constants.Constants.*;

/**
 * Gestisce la collezione di utenti in memoria (rifattorizzata).
 * Non è più un Singleton e opera su una lista di istanza.
 */
public class UserDatas implements Serializable { // Serializable per poter salvare l'intero oggetto
    
    private static final long serialVersionUID = 1L;
    
    // --- MODIFICATO ---
    // La lista ora è un campo di istanza, non più statico.
    private List<User> usersList;

    public UserDatas() {
        this.usersList = new ArrayList<>();
    }

    /**
     * Aggiunge un nuovo utente, riordina la lista e la tronca se supera la dimensione massima.
     * @param user L'utente da aggiungere.
     */
    private void addUserAndSort(User user) {
        usersList.add(user);
        
        // Usa una lambda per un codice più moderno e compatto
        usersList.sort(Comparator.comparingInt(User::getScore).reversed());
        
        if (usersList.size() > RANK_DIMENSION) {
            // Crea una nuova lista con solo i primi N elementi
            usersList = new ArrayList<>(usersList.subList(0, RANK_DIMENSION));
        }
    }

    // --- Getters ---
    public List<User> getUsersList() {

        return Collections.unmodifiableList(usersList);
    }
    
    // --- Setters ---
    public void setUsersList(List<User> users) {
        if (users != null) {
            this.usersList = users;
        }
    }
    
    // --- Builder ---
    // L'inner class non ha più bisogno di essere statica
    public class UserBuilder {
        private Integer avatarIndex;
        private String name;
        private Integer score;

        public UserBuilder() {
            // Il costruttore può essere vuoto
        }

        public UserBuilder setAvatarIndex(int avatarIndex) {
            this.avatarIndex = avatarIndex;
            return this; // Ritorna se stesso per il "chaining"
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setScore(int score) {
            this.score = score;
            return this;
        }

        public User createUser() {
            User user = new User(avatarIndex, name, score);
            addUserAndSort(user); // Usa il metodo privato della classe esterna
            return user;
        }
    }
}