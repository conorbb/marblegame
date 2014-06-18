package com.barconr.games.marblegame;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class Level {

	private TiledMap levelmap;		//Tiled map of level
	private int seconds;			//Seconds allowed to complete level
	
	public Level(TiledMap levelmap, int seconds){
		this.levelmap = levelmap;
		this.seconds = seconds;
	}
	
	
}
