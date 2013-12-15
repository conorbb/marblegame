package com.barconr.games.marblegame;

import com.badlogic.gdx.Gdx;

/*
 * Copyright 2011 Conor Byrne (conor@barconr.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
public class GameState {
	public boolean gameWon;
	private long timeLeft;
	private long levelTime;
	private long endTime;
	private boolean gameStarted;
	
	public GameState(){
		levelTime = 20 *1000; // 80 seconds
		gameWon =false;
		gameStarted = false;

	}
	
	public void setTimeLeft(float time){
		
	}
	
	public void timeUpdate(float delta){
		timeLeft -= delta;
	}
	
	public void startGame(){
		gameStarted = true;
		endTime = System.currentTimeMillis() + levelTime;
		
	}
	
	public long getTimeRemaining(){
		if(endTime - System.currentTimeMillis()<=0){
			return 0;
		}
		else{
			return endTime - System.currentTimeMillis();
		}
		
	}
	
	public boolean gameStarted(){
		return gameStarted;
	}
	
	
	

}
