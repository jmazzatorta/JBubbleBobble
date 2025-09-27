package model.managers;

import java.util.concurrent.atomic.AtomicInteger; // NUOVO IMPORT

public class ScoreManager {
	
	private static final AtomicInteger highestScore = new AtomicInteger(0);
	
	private int score;
	
	public void resetScore() {
		score = 0;
	}
	
	public void addScore(int delta) {
		this.score += delta;
		
		int currentHighest;
		do {
			currentHighest = highestScore.get();
			if (this.score <= currentHighest) {
				break; 
			}
		} while (!highestScore.compareAndSet(currentHighest, this.score));
	}
	
	public void subtractScore(int delta) {
		score -= delta;
	}
	
	public int getScore() {
		return score;
	}
	
	
	public static int getHighestScore() {
		return highestScore.get();
	}
	
	public static void setHighestScore(int score) {
		highestScore.set(score);
	}
}
