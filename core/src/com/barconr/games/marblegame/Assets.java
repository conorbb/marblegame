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
//		mazemap = new TmxMapLoader().load("tiles/test pattern.tmx");
		//new TmxMapLoader().lo
		
	}
	
	public static void dispose(){
		gameAtlas.dispose();
		mazemap.dispose();
		
	}

}
