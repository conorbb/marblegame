package com.barconr.games.marblegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Maze2Drenderer {
	private OrthographicCamera camera;
	private SpriteBatch batcher;

	Box2DDebugRenderer renderer;
	
	Vector2 ballLoc;

	Sprite marbleSprite;
	Vector2 startPosition;
	Vector2 endPostion;

	boolean ballVisble=true;

	OrthogonalTiledMapRenderer tilerender; 

	Matrix4 debugProj;
	Physics2D physics;
	private TiledMap map;


	public Maze2Drenderer(TiledMap tm, Physics2D physics) {
		this.physics = physics;
		renderer = new Box2DDebugRenderer();
		ballLoc = new Vector2();
		map = tm;

		camera = new OrthographicCamera(Assets.VIRTUAL_SCREEN_WIDTH,Assets.VIRTUAL_SCREEN_HEIGHT);
		camera.position.set(Assets.VIRTUAL_SCREEN_WIDTH/2f,Assets.VIRTUAL_SCREEN_HEIGHT/2f, 0);
		camera.update();

		//Load Marble Sprite
		marbleSprite = new Sprite(Assets.marble);
		marbleSprite.setBounds(0, 0, 16, 16);

		tilerender = new OrthogonalTiledMapRenderer(map);

		//createFixtures(Assets.mazemap);

		debugProj = new Matrix4(camera.combined);
		debugProj.scale(Assets.PIXELS_PER_METER, Assets.PIXELS_PER_METER, 1);
		batcher = new SpriteBatch();

		batcher.setProjectionMatrix(camera.combined);
		batcher.enableBlending();

		tilerender.setView(camera);




	}





	public void render() {	
		
		Gdx.gl.glClearColor(0,0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);


		tilerender.render();
		

		batcher.begin();
		ballLoc.set(physics.getPlayerBallLoc());

		marbleSprite.setPosition((ballLoc.x*Assets.PIXELS_PER_METER)-8, (ballLoc.y*Assets.PIXELS_PER_METER)-8);
		marbleSprite.draw(batcher);

		batcher.end();

		renderer.render(physics.getWorld(), debugProj);



	}
}



