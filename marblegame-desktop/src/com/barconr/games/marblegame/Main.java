package com.barconr.games.marblegame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "marblegame";
		cfg.useGL20 = true;
		cfg.width = 400;
		cfg.height = 240;
		
		new LwjglApplication(new MarbleGame(), cfg);
	}
}
