package com.barconr.games.marblegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Physics2D {
	
	private World world;
	private BodyDef playerBallBodyDef;
	private CircleShape dynamicCircle;
	private Body ballCircleBody;
	private Vector2 startPosition;	
	
	private BallContactListener ballsListener;
	static final float BOX_STEP=1/60f;  
	static final int BOX_VELOCITY_ITERATIONS=6;  
	static final int BOX_POSITION_ITERATIONS=2;  
	Box2DDebugRenderer debugRenderer;

	boolean gameWon = false;

	

	
	public void simulate(){
		
		if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			world.setGravity(getAccel());
		}
			
		
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
	}
	
	public void createFixtures(TiledMap tiledmap){
		int layerHeight =0 , layerWidth=0;
		float tileWidth=0 ,tileHeight=0 ;
		
		ballsListener = new BallContactListener();

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
		world.setContactListener(ballsListener);




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
					//world.
					//squareBody.setUserData("exit");
					//Size of box
					squareShape.setAsBox(tileWidth/2 * Assets.METERS_PER_PIXEL, tileHeight/2 * Assets.METERS_PER_PIXEL);
					FixtureDef fixDefSquare = new FixtureDef();
					
					fixDefSquare.shape = squareShape;
					fixDefSquare.isSensor = true;
					
					fixDefSquare.density = 0.1f;
					fixDefSquare.restitution= 0.3f;
					


					Fixture endFix = squareBody.createFixture(fixDefSquare);
					endFix.setSensor(true);
					endFix.setUserData("exit");
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
		playerBallBodyDef = new BodyDef();  
		playerBallBodyDef.type = BodyType.DynamicBody; 

		if(startPosition==null){
			playerBallBodyDef.position.set(3f ,3f);  

		}
		else{
			playerBallBodyDef.position.set(startPosition.x ,startPosition.y);  
		}
		playerBallBodyDef.allowSleep = false;
		
		ballCircleBody = world.createBody(playerBallBodyDef);  
		
		dynamicCircle = new CircleShape();  
		dynamicCircle.setRadius(8/Assets.PIXELS_PER_METER );  


		FixtureDef ballFixtureDef = new FixtureDef(); 
		ballFixtureDef.shape = dynamicCircle;  
		ballFixtureDef.density = 0.1f;  
		ballFixtureDef.friction = 1f;  
		//ballFixtureDef.

		ballFixtureDef.restitution = 0.5f; 
		//ballCircleBody.setUserData("ball");
		Fixture fx = ballCircleBody.createFixture(ballFixtureDef);  
		fx.setUserData("ball");

		

}
	public Vector2 getPlayerBallLoc(){
		
		return ballCircleBody.getWorldCenter();
	}
	
	private Vector2 getAccel(){
		return new Vector2(Gdx.input.getAccelerometerY()*7,Gdx.input.getAccelerometerX()*-7);
	}
	
	
	public World getWorld(){
		return world;
	}
	
	boolean getWon(){
		return gameWon;
	}
	
	
	class BallContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			
				if(contact.getFixtureA()!=null&&contact.getFixtureB()!=null){
					
					if(contact.getFixtureA().getUserData()!=null&&contact.getFixtureB().getUserData()!=null){
						
						if(((String)contact.getFixtureA().getUserData()).equals("ball")&&((String)contact.getFixtureB().getUserData()).equals("exit")){
							gameWon = true;
						}
						else if (((String)contact.getFixtureA().getUserData()).equals("exit")&&((String)contact.getFixtureB().getUserData()).equals("ball")){
							gameWon = true;
						}

					
					}
					
				}

			
		}

		@Override
		public void endContact(Contact contact) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
