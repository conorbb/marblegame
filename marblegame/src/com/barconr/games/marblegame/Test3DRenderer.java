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

import com.badlogic.gdx.math.Vector3;

public class Test3DRenderer {
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    
    public ModelInstance instance;
    public ArrayList<ModelInstance> instances;
	private Environment environment;
    public Test3DRenderer(){
        modelBatch = new ModelBatch();
        instances = new ArrayList<ModelInstance>();
        
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0, 0, 400f);
        cam.lookAt(0, 0,0);
        
        

        cam.near = 0.1f;
        cam.far = 500f;
        
        cam.update();
        
        //Lighting
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
 
        doTestRender();
        
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
		addSquare(new Vector3(-50,0,0));
		addSquare(new Vector3(-50,16,0));
		addSquare(new Vector3(-50,32,0));
		addSquare(new Vector3(-50,48,0));
	}
	
    

    

    public  void render(){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        
        for(ModelInstance mi : instances)
        	modelBatch.render(mi,environment);
        
        modelBatch.end();
    }
    



}
