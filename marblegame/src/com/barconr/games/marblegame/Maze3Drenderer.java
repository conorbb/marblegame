package com.barconr.games.marblegame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Maze3Drenderer {
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    
    public ModelInstance instance;
    public ArrayList<ModelInstance> instances;
	private Environment environment;
	private ModelBuilder modelBuilder;
//	private Mazebuilder mazeBuilder;
	
	
	int layerHeight =0 , layerWidth=0;
	float tileWidth=0 ,tileHeight=0 ;
	
	private static final float CUBE_SIZE = 8;
    public Maze3Drenderer(){
        modelBatch = new ModelBatch();
        instances = new ArrayList<ModelInstance>();
        modelBuilder = new ModelBuilder();
//        mazeBuilder = new Mazebuilder();
        createModels(Assets.mazemap);
        
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float mapPixelWidth = layerWidth * CUBE_SIZE;
        float mapPixelHeight = layerHeight * CUBE_SIZE;
        
        Vector3 midPoint = new Vector3(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,0);
        Vector3 leftPoint = new Vector3(0, Gdx.graphics.getHeight()/2,0);
        
        cam.position.set((float) mapPixelWidth/2, (float)mapPixelHeight/2, 200f);
        cam.lookAt((float) mapPixelWidth/2, (float)mapPixelHeight/2,0);
        
//        cam.position.set((float) Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, 250f);
//        cam.lookAt((float)Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2,0);
        

        
//    cam.position.set(0, 0, 150);
//       cam.lookAt(0,0,0);
        cam.near = 90.1f;
        cam.far = 220f;
        
        cam.update();
//        cam.project(midPoint);
//        cam.project(leftPoint);
//        
////        float xtrans = midPoint.x - ((float)mapPixelWidth/2) - leftPoint.x;
//        float xtrans = leftPoint.x - midPoint.x;
//        System.out.println("Mid Point " + midPoint);
//        System.out.println("left Point" + leftPoint);
//        System.out.println("xtrans " + xtrans);
//        System.out.println("mapPixelWidth" + mapPixelWidth);
//        cam.position.set(cam.position.x-180, cam.position.y, 450);
//        
//        //cam.position.set((float) mapPixelWidth - midPoint.x,(float)mapPixelHeight - midPoint.y, 250f);
//        cam.lookAt(cam.position.x,cam.position.y,0);
//        cam.update();
//        
//        
//        
        
        System.out.println("[][][][] "+midPoint);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        
        
    }
    
	 private void createModels(TiledMap tiledmap){

		 
		// Load the Tiled map layer and store some relevant variables
		TiledMapTileLayer tilelayer = (TiledMapTileLayer)(tiledmap.getLayers().get(0));
		layerHeight = tilelayer.getHeight();
		layerWidth = tilelayer.getWidth();
//		tileHeight = tilelayer.getTileHeight();
//		tileWidth = tilelayer.getTileWidth();
		
		tileWidth= tileHeight = CUBE_SIZE;
		
		
		for(int y_pos=0;y_pos<layerHeight;y_pos++){

			for(int x_pos=0;x_pos<layerWidth;x_pos++){

				//boolean impassibleBlock = tilelayer.getCell(x_pos, layerHeight-y_pos-1).getTile().getProperties().containsKey("block");
				boolean impassibleBlock = tilelayer.getCell(x_pos, y_pos).getTile().getProperties().containsKey("block");
				if(impassibleBlock){
					//Draw a cube here
					float xcord = x_pos * tileWidth;
					float ycord = y_pos * tileHeight;
					
					addBox(new Vector3(xcord,ycord,0));
//					mazeBuilder.createMazePart(xcord,ycord,0);
				}

				
			}
			
		}
		addBox(new Vector3((layerWidth*tileWidth)/2,(layerHeight*tileHeight)/2,-CUBE_SIZE), layerWidth*tileWidth, layerHeight*tileHeight, CUBE_SIZE,Color.GRAY);
		
		
		
		
	}
	
	private void addBox(Vector3 position){
		addBox(position, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE,Color.GREEN);

	}
	
	private void addBox(Vector3 position,float width,float height , float depth,Color col){
		Model model = modelBuilder.createBox(width, height, depth, 
			    new Material(ColorAttribute.createDiffuse(col)),
			    Usage.Position | Usage.Normal);
			ModelInstance mis = new ModelInstance(model);
			mis.transform.setTranslation(position);
			instances.add(mis);
	}
	
//	private void addBoxViaMeshBuilder(Vector3 position,float width,float height , float depth,Color col){
//		mazeBuilder.createMazePart(width, height, depth);
//		
//	}
//	
//	private void finishMazeBuild(){
//		instances.add(new ModelInstance(mazeBuilder.finishModel()));
//	}
//	
	private void doTestRender(){
		addBox(new Vector3(0,0,50));
		addBox(new Vector3(0,16,50));
		addBox(new Vector3(0,32,50));
		addBox(new Vector3(0,48,50));
	}
	
    

    

    public  void render(){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        Gdx.gl20.glEnable(Gdx.gl20.GL_POLYGON_OFFSET_FILL);
        Gdx.gl20.glPolygonOffset(0.1f,0.1f);
//        modelBatch.begin(cam);
//        for(ModelInstance mi : instances){
        	modelBatch.render(instances,environment);
//        }
        
//        modelBatch.end();
    }
    
	public void moveCamera(){
		cam.rotate(new Vector3(0, 0, 0), 100);
		cam.update();
	}


}
