package model.users;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = -7207853372977136677L;
	
	private int avatarIndex;
	private String name;
	private int score;
	
	
	public User(int avatarIndex, String name, int score) {
		
		this.avatarIndex= avatarIndex;		
		this.name= name;
		this.score= score;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAvatarIndex() {
		return avatarIndex;
	}
	
	public int getScore() {
		return score;
	}
	
	
}
