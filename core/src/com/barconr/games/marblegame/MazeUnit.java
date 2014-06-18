package com.barconr.games.marblegame;

import com.badlogic.gdx.math.Vector2;

public class MazeUnit {
	
	public Vector2 abs_coordinates;
	public Vector2 gridPos;
	
	String type;

	public MazeUnit(String type){
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
