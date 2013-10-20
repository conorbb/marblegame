package com.barconr.games.marblegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets{
	
	public static final int VIRTUAL_SCREEN_HEIGHT = 480;
	public static final int VIRTUAL_SCREEN_WIDTH = 800;
	
//	public static final float VIRTUAL_SCREEN_HEIGHT = 160;
//	public static final float VIRTUAL_SCREEN_WIDTH = 266.666666667f;
	
	
	public static TiledMap mazemap;
	
	public static final float HEIGHT_METERS = 6;
	public static final float WIDTH_METERS = 10;
//	public static final float HEIGHT_METERS = 18;
//	public static final float WIDTH_METERS = 30;
	
	
	public static final float ASPECT_RATIO= (float) VIRTUAL_SCREEN_WIDTH / VIRTUAL_SCREEN_HEIGHT;
	public static final float PIXELS_PER_METER = VIRTUAL_SCREEN_HEIGHT / HEIGHT_METERS;
	public static final float METERS_PER_PIXEL = HEIGHT_METERS / VIRTUAL_SCREEN_HEIGHT;
	
	
	
	public static TextureAtlas gameAtlas;
	public static TextureRegion marble;
	
	
	public static void load(){
		gameAtlas = new TextureAtlas(Gdx.files.internal("gfx/marble.pack"));
		marble = new TextureRegion(gameAtlas.findRegion("marb"));
		mazemap = new TmxMapLoader().load("tiles/mg.tmx");
		//new TmxMapLoader().lo
		
	}
	
	public static void dispose(){
		gameAtlas.dispose();
		mazemap.dispose();
	}

}
