package com.barconr.games.marblegame;

import java.util.Iterator;

import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
    
    Vector2 startPosition;
    Vector2 endPostion;
    
    OrthogonalTiledMapRenderer tilerender; 
    
    Matrix4 debugProj;

    private TiledMap map;
	@Override
	public void create() {
		Assets.load();
		
		
		debugRenderer = new Box2DDebugRenderer();  
		
		camera = new OrthographicCamera(Assets.VIRTUAL_SCREEN_WIDTH,Assets.VIRTUAL_SCREEN_HEIGHT);
		camera.position.set(Assets.VIRTUAL_SCREEN_WIDTH/2f,Assets.VIRTUAL_SCREEN_HEIGHT/2f, 0);
		//camera.
		camera.update();
		//camera.
		marbleSprite = new Sprite(Assets.marble);
		marbleSprite.setBounds(0, 0, 32, 32);
		
		tilerender = new OrthogonalTiledMapRenderer(Assets.mazemap);
		//createBoxShit();
		createFixturesFromMapII(Assets.mazemap);
		
		debugProj = new Matrix4(camera.combined);
		debugProj.scale(Assets.PIXELS_PER_METER, Assets.PIXELS_PER_METER, 1);
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(camera.combined);
		batcher.enableBlending();
		
		tilerender.setView(camera);
		
		debugRenderer = new Box2DDebugRenderer();
		
	}
	
	
	
	
	public void createBoxShit(){
		world = new World(new Vector2(0, -9), true); 
		
		//world.
        BodyDef groundBodyDef =new BodyDef();  
        groundBodyDef.position.set(new Vector2(5, 1f));  
        Body groundBody = world.createBody(groundBodyDef);  
        groundBodyDef.type = BodyType.StaticBody;
        PolygonShape groundBox = new PolygonShape();  
        //groundBox.setAsBox(20, 0.3f);  
        groundBox.setAsBox(5f, 0.3f);
        groundBody.createFixture(groundBox, 0.0f);  
        
        
        
        
        bodyDef = new BodyDef();  
        
        //bodyDef.
        bodyDef.type = BodyType.DynamicBody; 
        bodyDef.position.set(3f ,3f);  
        circleBody = world.createBody(bodyDef);  
        
        
        CircleShape dynamicCircle = new CircleShape();  
        dynamicCircle.setRadius(70 / Assets.PIXELS_PER_METER);  
        System.out.println("Radius" + 70 / Assets.PIXELS_PER_METER);
       //System.out.println(70 / Assets.PIXELS_PER_METER);
        
        FixtureDef fixtureDef = new FixtureDef();  
        fixtureDef.shape = dynamicCircle;  
        fixtureDef.density = 0.1f;  
        fixtureDef.friction = 1f;  
        
        fixtureDef.restitution = 0.5f; 
        
        //fixtureDef.
        circleBody.createFixture(fixtureDef);  
        
        
        PolygonShape squareShape = new PolygonShape();
        BodyDef squareBodyDef = new BodyDef();
        squareBodyDef.position.sub(1* Assets.METERS_PER_PIXEL, 1* Assets.METERS_PER_PIXEL);
        
        squareShape.setAsBox(50 * Assets.METERS_PER_PIXEL, 50 * Assets.METERS_PER_PIXEL);
        FixtureDef fixDefSquare = new FixtureDef();
        fixDefSquare.shape = squareShape;
        fixDefSquare.density = 0.1f;
        fixDefSquare.restitution= 0.3f;
        Body squareBody = world.createBody(squareBodyDef);
        squareBody.createFixture(fixDefSquare);
        
        
        
        
        
        
	}
	
	
	
	void createFixturesFromMapII(TiledMap tiledmap){
		int layerHeight =0 , layerWidth=0;
		float tileWidth=0 ,tileHeight=0 ;
		
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
		
		
		
		
		
		for(int i=0;i<layerHeight;i++){
			
			for(int j=0;j<layerWidth;j++){
				boolean impassibleBlock = tilelayer.getCell(j, layerHeight-i-1).getTile().getProperties().containsKey("block");
				if(tilelayer.getCell(j, layerHeight-i-1).getTile().getProperties().containsKey("start")){
					if(startPosition==null){
						
						startPosition = new Vector2(j*tileWidth*Assets.METERS_PER_PIXEL,i*tileHeight*Assets.METERS_PER_PIXEL);
						System.out.println(startPosition);
					}
					
					
				}
				if(tilelayer.getCell(j, layerHeight-i-1).getTile().getProperties().containsKey("end")){
					
				}
				
					if(impassibleBlock){
				
					//Draw box for fixture that is impassible
					PolygonShape squareShape = new PolygonShape();
			        BodyDef squareBodyDef = new BodyDef();
			        squareBodyDef.type = BodyType.StaticBody;	//Static body which won't move
			        //Box position
			        squareBodyDef.position.set( new Vector2((j* Assets.METERS_PER_PIXEL*tileWidth), (layerHeight-i-1)* Assets.METERS_PER_PIXEL*tileHeight));
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
					
					//System.out.print("@");
					
				}
				else{
					//System.out.print("#");
				}
				
			}
			//System.out.println();
		}
		
		
		
        bodyDef = new BodyDef();  
        //bodyDef.
        bodyDef.type = BodyType.DynamicBody; 
        if(startPosition==null){
        	bodyDef.position.set(3f ,3f);  
        }
        else{
        	bodyDef.position.set(startPosition.x ,startPosition.y);  
        }
        
        circleBody = world.createBody(bodyDef);  
        
		

        dynamicCircle = new CircleShape();  
        dynamicCircle.setRadius(16/Assets.PIXELS_PER_METER );  
        
       //System.out.println(70 / Assets.PIXELS_PER_METER);
        
        FixtureDef fixtureDef = new FixtureDef();  
        fixtureDef.shape = dynamicCircle;  
        fixtureDef.density = 0.1f;  
        fixtureDef.friction = 1f;  
        
        fixtureDef.restitution = 0.5f; 
        
        //fixtureDef.
        circleBody.createFixture(fixtureDef);  
		
		
		
	}
	
	

	@Override
	public void dispose() {
		Assets.dispose();
	}

	@Override
	public void render() {	
		float f = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0,0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

//		batcher.begin();
//		batcher.draw(Assets.marble,circleBody.getPosition().x,circleBody.getPosition().y * Assets.PIXELS_PER_METER);
//		batcher.end();
//		
		if(Gdx.input.justTouched()){
			float x = Gdx.input.getX();
			float y = Gdx.input.getY();
			System.out.println("**************");
			System.out.println("x:"+x + " y: "+ y +" before unproject");
			Vector3 v3 = new Vector3(x, y, 0);

			
			camera.unproject(v3);
			createTestBall(v3.x,v3.y);
			
		}
		
		
		
		tilerender.render();
		debugRenderer.render(world, debugProj);
		//System.out.println(bodyDef.position.x*Assets.PIXELS_PER_METER +""+bodyDef.position.y * Assets.PIXELS_PER_METER);
		//world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);  
		
		
		batcher.begin();
		Vector2 v2 = circleBody.getWorldCenter();
		
		marbleSprite.setPosition((v2.x*Assets.PIXELS_PER_METER)-16, (v2.y*Assets.PIXELS_PER_METER)-16);
		marbleSprite.draw(batcher);
		
		batcher.end();
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
		
		
	}

	private void createTestBall(float x, float y) {
			System.out.println("x: "+x+" y: "+ y+"Uprojected");
			System.out.println("x: "+x*Assets.METERS_PER_PIXEL+" y: "+ y*Assets.METERS_PER_PIXEL+" * pixels per meter");
		 	BodyDef bodyDef1 = new BodyDef();  
	        //bodyDef.
	        bodyDef1.type = BodyType.DynamicBody; 
	        
	        bodyDef1.position.set((x*Assets.METERS_PER_PIXEL),(y *Assets.METERS_PER_PIXEL));  
	        
	        Body circleBody2 = world.createBody(bodyDef1);  
	        
			
	        CircleShape dynamicCircle1;
	        dynamicCircle1 = new CircleShape();  
	        dynamicCircle1.setRadius(0.25f );  
	     
	       //System.out.println(70 / Assets.PIXELS_PER_METER);
	        
	        FixtureDef fixtureDef1 = new FixtureDef();  
	        fixtureDef1.shape = dynamicCircle1;  
	        fixtureDef1.density = 0.1f;  
	        fixtureDef1.friction = 1f;  
	        
	        fixtureDef1.restitution = 0.5f; 
	        
	        //fixtureDef.
	        circleBody2.createFixture(fixtureDef1);  
			
		
	}




	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
