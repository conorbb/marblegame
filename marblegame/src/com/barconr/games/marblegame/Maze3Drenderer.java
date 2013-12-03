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
	
	private static final float CUBE_SIZE = 8f;
    public Maze3Drenderer(){
        modelBatch = new ModelBatch();
        instances = new ArrayList<ModelInstance>();
        modelBuilder = new ModelBuilder();
        
        createModels(Assets.mazemap);
        
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float mapPixelWidth = layerWidth * CUBE_SIZE;
        float mapPixelHeight = layerHeight * CUBE_SIZE;

        cam.position.set((float) Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, 100f);
        cam.lookAt((float) Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2,0);
        

        cam.near = 0.1f;
        cam.far = 150f;
        
        cam.update();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        
        
    }
    
	 private void createModels(TiledMap tiledmap){

		 
		// Load the Tiled map layer and store some relevant variables
		TiledMapTileLayer tilelayer = (TiledMapTileLayer)(tiledmap.getLayers().get(0));
		layerHeight = tilelayer.getHeight();
		layerWidth = tilelayer.getWidth();

		
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
		
		//Background
		addBox(new Vector3((layerWidth*tileWidth)/2,(layerHeight*tileHeight)/2,-CUBE_SIZE), layerWidth*tileWidth, layerHeight*tileHeight, CUBE_SIZE,Color.GRAY);
		
		
		
		
	}
	
	private void addBox(Vector3 position){
		addBox(position, CUBE_SIZE+0.1f, CUBE_SIZE+0.1f, CUBE_SIZE+0.1f,Color.GREEN);

	}
	
	private void addBox(Vector3 position,float width,float height , float depth,Color col){
		Model model = modelBuilder.createBox(width, height, depth, 
			    new Material(ColorAttribute.createDiffuse(col)),
			    Usage.Position | Usage.Normal);
			ModelInstance mis = new ModelInstance(model);
			mis.transform.setTranslation(position);
			instances.add(mis);
	}
	

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
        modelBatch.begin(cam);
        for(ModelInstance mi : instances){
        	modelBatch.render(mi,environment);
        }
        
        
        	moveCamera();
        
        
        modelBatch.end();
    }
    
	public void moveCamera(){
		cam.position.add(-10*Gdx.graphics.getDeltaTime(), 0, 0);
		cam.update();
	}


}
