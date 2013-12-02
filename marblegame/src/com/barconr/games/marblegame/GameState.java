package com.barconr.games.marblegame;

public class GameState {
	public boolean gameWon;
	public float timeLeft;
	
	public GameState(){
		gameWon =false;
		timeLeft = 0;
	}
	
	public void setTimeLeft(float time){
		
	}
	
	public void timeUpdate(float delta){
		timeLeft -= delta;
	}
	

}
