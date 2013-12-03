package com.barconr.games.marblegame;
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
