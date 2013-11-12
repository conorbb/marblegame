package com.barconr.games.marblegame;

import java.util.Iterator;

import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class MarbleGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batcher;
	private World world;
	BodyDef bodyDef;
	CircleShape dynamicCircle;
	Body circleBody;
	Sprite marbleSprite;
	static final float BOX_STEP=1/60f;  
	static final int BOX_VELOCITY_ITERATIONS=6;  
	static final int BOX_POSITION_ITERATIONS=2;  
	Box2DDebugRenderer debugRenderer;
	Maze3Drenderer maze3Drenderer;

	Vector2 startPosition;
	Vector2 endPostion;

	boolean ballVisble=true;

	OrthogonalTiledMapRenderer tilerender; 

	Matrix4 debugProj;

	private TiledMap map;

	//3D shite
//	public PerspectiveCamera cam3D;
//	public Model model;
//	public ModelInstance instance;
//	public ModelBatch modelBatch;

	@Override
	public void create() {
		Assets.load();
//		create3Dcam();
//		create3DModel();
		maze3Drenderer = new Maze3Drenderer();
		debugRenderer = new Box2DDebugRenderer();  
		//Setup Ortho (2d) cam
		camera = new OrthographicCamera(Assets.VIRTUAL_SCREEN_WIDTH,Assets.VIRTUAL_SCREEN_HEIGHT);
		camera.position.set(Assets.VIRTUAL_SCREEN_WIDTH/2f,Assets.VIRTUAL_SCREEN_HEIGHT/2f, 0);
		camera.update();

		//Load Marble Sprite
		marbleSprite = new Sprite(Assets.marble);
		marbleSprite.setBounds(0, 0, 16, 16);

		tilerender = new OrthogonalTiledMapRenderer(Assets.mazemap);
		maze3Drenderer.createModels(Assets.mazemap);
		createFixtures(Assets.mazemap);
		
		debugProj = new Matrix4(camera.combined);
		debugProj.scale(Assets.PIXELS_PER_METER, Assets.PIXELS_PER_METER, 1);
		batcher = new SpriteBatch();

		batcher.setProjectionMatrix(camera.combined);
		batcher.enableBlending();

		tilerender.setView(camera);

		debugRenderer = new Box2DDebugRenderer();

		


	}

//	public void create3Dcam(){
//
//		cam3D = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		cam3D.position.set(10f, 10f, 10f);
//		cam3D.lookAt(0,0,0);
//		cam3D.near = 0.1f;
//		cam3D.far = 300f;
//		cam3D.update();
//
//	}
//
//	public void create3DModel(){
//		ModelBuilder modelBuilder = new ModelBuilder();
//		model = modelBuilder.createBox(5f, 5f, 5f, 
//				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
//				Usage.Position | Usage.Normal);
//		instance = new ModelInstance(model);
//		modelBatch = new ModelBatch();
//	}
//
//	public void renderBox(){
//		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//
//		modelBatch.begin(cam3D);
//		modelBatch.render(instance);
//		modelBatch.end();
//	}
//
//


	private void createFixtures(TiledMap tiledmap){
		int layerHeight =0 , layerWidth=0;
		float tileWidth=0 ,tileHeight=0 ;

		// Load the Tiled map layer and store some relevant variables
		TiledMapTileLayer tilelayer = (TiledMapTileLayer)(tiledmap.getLayers().get(0));
		layerHeight = tilelayer.getHeight();
		layerWidth = tilelayer.getWidth();
		tileHeight = tilelayer.getTileHeight();
		tileWidth = tilelayer.getTileWidth();


		System.out.println("Layer height (tiles) = " + layerHeight+" Layer width (tiles) = " + layerWidth+ " Tile height = " + tileHeight+"tile width = " + tileWidth);
		System.out.println("Tile count = "+ tilelayer.getObjects().getCount());
		System.out.println("layer height pixels = " + (layerHeight*tileHeight) + "LAYER WIDTH PIXELS : " + (layerWidth*tileWidth) );

		//Loop through tilemap

		//Create the box2d world
		world = new World(new Vector2(0, -9), true); 




		// Loop through the grid reference
		for(int y_pos=0;y_pos<layerHeight;y_pos++){

			for(int x_pos=0;x_pos<layerWidth;x_pos++){

				boolean impassibleBlock = tilelayer.getCell(x_pos, layerHeight-y_pos-1).getTile().getProperties().containsKey("block");

				// If the tile square contains the reference "start"
				// Store this as the start position
				if(tilelayer.getCell(x_pos, layerHeight-y_pos-1).getTile().getProperties().containsKey("start")){
					if(startPosition==null){
						System.out.println("x:"+ x_pos*tileWidth+ " y:"+y_pos*tileHeight);
						startPosition = new Vector2(x_pos*tileWidth*Assets.METERS_PER_PIXEL,(50-y_pos)*tileHeight*Assets.METERS_PER_PIXEL);


					}


				}

				//Create a fixture for the end position
				if(tilelayer.getCell(x_pos, layerHeight-y_pos-1).getTile().getProperties().containsKey("end")){
					//Draw box for fixture that is impassible
					PolygonShape squareShape = new PolygonShape();


					BodyDef squareBodyDef = new BodyDef();
					squareBodyDef.type = BodyType.StaticBody;	//Static body which won't move
					//Box position
					squareBodyDef.position.set( new Vector2((x_pos* Assets.METERS_PER_PIXEL*tileWidth), (layerHeight-y_pos-1)* Assets.METERS_PER_PIXEL*tileHeight));
					//Correction for fact Box2Ds squares are half width/height from center point
					squareBodyDef.position.add(tileWidth/2*Assets.METERS_PER_PIXEL, tileHeight/2*Assets.METERS_PER_PIXEL);	

					Body squareBody = world.createBody(squareBodyDef);
					squareBody.setUserData("exit");
					//Size of box
					squareShape.setAsBox(tileWidth/2 * Assets.METERS_PER_PIXEL, tileHeight/2 * Assets.METERS_PER_PIXEL);
					FixtureDef fixDefSquare = new FixtureDef();

					fixDefSquare.shape = squareShape;
					fixDefSquare.isSensor = true;
					fixDefSquare.density = 0.1f;
					fixDefSquare.restitution= 0.3f;


					squareBody.createFixture(fixDefSquare);

				}

				if(impassibleBlock){

					//Draw box for fixture that blocks
					PolygonShape squareShape = new PolygonShape();
					BodyDef squareBodyDef = new BodyDef();
					squareBodyDef.type = BodyType.StaticBody;	//Static body which won't move
					//Box position
					squareBodyDef.position.set( new Vector2((x_pos* Assets.METERS_PER_PIXEL*tileWidth), (layerHeight-y_pos-1)* Assets.METERS_PER_PIXEL*tileHeight));
					//Correction for fact Box2Ds squares are half width/height from center point
					squareBodyDef.position.add(tileWidth/2*Assets.METERS_PER_PIXEL, tileHeight/2*Assets.METERS_PER_PIXEL);	

					Body squareBody = world.createBody(squareBodyDef);
					//Size of box
					squareShape.setAsBox(tileWidth/2 * Assets.METERS_PER_PIXEL, tileHeight/2 * Assets.METERS_PER_PIXEL);
					FixtureDef fixDefSquare = new FixtureDef();
					fixDefSquare.shape = squareShape;
					fixDefSquare.density = 0.1f;
					fixDefSquare.restitution= 0.3f;
					squareBody.createFixture(fixDefSquare);

					//System.out.print("@");		// Draw ascii map in stdout

				}
				else{
					//System.out.print("#");		// Draw ascii map in stdout
				}

			}
			//System.out.println();
		}


		// The players ball
		bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody; 

		if(startPosition==null){
			bodyDef.position.set(3f ,3f);  

		}
		else{
			bodyDef.position.set(startPosition.x ,startPosition.y);  
		}
		bodyDef.allowSleep = false;

		circleBody = world.createBody(bodyDef);  
		circleBody.setUserData("ball");
		dynamicCircle = new CircleShape();  
		dynamicCircle.setRadius(8/Assets.PIXELS_PER_METER );  


		FixtureDef fixtureDef = new FixtureDef(); 
		fixtureDef.shape = dynamicCircle;  
		fixtureDef.density = 0.1f;  
		fixtureDef.friction = 1f;  

		fixtureDef.restitution = 0.5f; 
		circleBody.createFixture(fixtureDef);  



	}




	@Override
	public void render() {	
		float f = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0,0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);


		// Get the phone accelerometer data. Use it to set the gravity
		world.setGravity(getAccel());


		//tilerender.render();
		//debugRenderer.render(world, debugProj);

		maze3Drenderer.render();


		batcher.begin();
		Vector2 v2 = circleBody.getWorldCenter();

		marbleSprite.setPosition((v2.x*Assets.PIXELS_PER_METER)-8, (v2.y*Assets.PIXELS_PER_METER)-8);
		marbleSprite.draw(batcher);

		batcher.end();
		world.step(f, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
		//world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
		
		if(Gdx.input.justTouched()){
			maze3Drenderer.moveCamera();
		}


	}

	Vector2 getAccel(){
		return new Vector2(Gdx.input.getAccelerometerY()*7,Gdx.input.getAccelerometerX()*-7);
	}




	@Override
	public void dispose() {
		//		world.dispose();
		//		tilerender.dispose();
		//		batcher.dispose();
		//		Assets.dispose();

	}



	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}


	@Override
	public void pause() {
		dispose();
	}

	@Override
	public void resume() {
	}
}
