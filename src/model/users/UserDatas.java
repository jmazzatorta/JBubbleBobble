package model.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static constants.Constants.*;

public class UserDatas implements Serializable { 
    
    private static final long serialVersionUID = 1L;
    
    private List<User> usersList;

    public UserDatas() {
        this.usersList = new ArrayList<>();
    }

    private void addUserAndSort(User user) {
        usersList.add(user);
        
        usersList.sort(Comparator.comparingInt(User::getScore).reversed());
        
        if (usersList.size() > RANK_DIMENSION) {
            usersList = new ArrayList<>(usersList.subList(0, RANK_DIMENSION));
        }
    }

    public List<User> getUsersList() {

        return Collections.unmodifiableList(usersList);
    }
    
    public void setUsersList(List<User> users) {
        if (users != null) {
            this.usersList = users;
        }
    }
    
    public class UserBuilder {
        private Integer avatarIndex;
        private String name;
        private Integer score;

        public UserBuilder() {
        }

        public UserBuilder setAvatarIndex(int avatarIndex) {
            this.avatarIndex = avatarIndex;
            return this;
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
            addUserAndSort(user); 
            return user;
        }
    }
}