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
    public Maze3Drenderer(){
        modelBatch = new ModelBatch();
        instances = new ArrayList<ModelInstance>();
        
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set((float) Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, 400f);
        cam.lookAt(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,0);
        
        
//        cam.position.set(400f, 260f, 400f);
//        cam.lookAt(400,260,0);
        cam.near = 0.1f;
        cam.far = 500f;
        
        cam.update();
        
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        createModels(Assets.mazemap);
        
    }
    
	 private void createModels(TiledMap tiledmap){
		int layerHeight =0 , layerWidth=0;
		float tileWidth=0 ,tileHeight=0 ;

		// Load the Tiled map layer and store some relevant variables
		TiledMapTileLayer tilelayer = (TiledMapTileLayer)(tiledmap.getLayers().get(0));
		layerHeight = tilelayer.getHeight();
		layerWidth = tilelayer.getWidth();
		tileHeight = tilelayer.getTileHeight();
		tileWidth = tilelayer.getTileWidth();
		
		
		for(int y_pos=0;y_pos<layerHeight;y_pos++){

			for(int x_pos=0;x_pos<layerWidth;x_pos++){

				//boolean impassibleBlock = tilelayer.getCell(x_pos, layerHeight-y_pos-1).getTile().getProperties().containsKey("block");
				boolean impassibleBlock = tilelayer.getCell(x_pos, y_pos).getTile().getProperties().containsKey("block");
				if(impassibleBlock){
					//Draw a cube here
					float xcord = x_pos * tileWidth;
					float ycord = y_pos * tileHeight;
					
					addSquare(new Vector3(xcord,ycord,0));
				}

				
			}
			
		}
		
		
	}
	
	private void addSquare(Vector3 position){
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model = modelBuilder.createBox(16f, 16f, 16f, 
		    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
		    Usage.Position | Usage.Normal);
		ModelInstance mis = new ModelInstance(model);
		mis.transform.setTranslation(position);
		instances.add(mis);
	}
	
	private void doTestRender(){
		addSquare(new Vector3(0,0,0));
		addSquare(new Vector3(0,16,0));
		addSquare(new Vector3(0,32,0));
		addSquare(new Vector3(0,48,0));
	}
	
    

    

    public  void render(){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        for(ModelInstance mi : instances){
        	modelBatch.render(mi,environment);
        }
        
        modelBatch.end();
    }
    
	public void moveCamera(){
		cam.rotate(new Vector3(0, 0, 0), 100);
		cam.update();
	}


}
