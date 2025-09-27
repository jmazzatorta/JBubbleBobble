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
		
		// Questo blocco aggiorna il punteggio massimo in modo thread-safe.
		int currentHighest;
		do {
			currentHighest = highestScore.get();
			if (this.score <= currentHighest) {
				break; // Il nostro punteggio non è più alto, usciamo.
			}
			// Altrimenti, tentiamo di impostare il nostro punteggio come nuovo record.
			// compareAndSet ha successo solo se il valore non è cambiato nel frattempo.
		} while (!highestScore.compareAndSet(currentHighest, this.score));
	}
	
	public void subtractScore(int delta) {
		score -= delta;
	}
	
	public int getScore() {
		return score;
	}
	
	// --- Metodi per l'highest score  ---
	
	public static int getHighestScore() {
		return highestScore.get();
	}
	
	public static void setHighestScore(int score) {
		highestScore.set(score);
	}
}
