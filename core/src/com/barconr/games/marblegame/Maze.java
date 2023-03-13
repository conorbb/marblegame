package com.barconr.games.marblegame;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Maze{
	
	public final int width;
	public final int height;
	public MazeUnit [][] mazeUnits; 
	public final String gameType;
	public final boolean hasEnemy;
	public final int timeLimit;
	public Maze(int widthUnits, int heightUnits){
		width = widthUnits;
		height = heightUnits;
		
		mazeUnits = new MazeUnit[width][height];
		gameType =new String("timed") ;
		hasEnemy = false;
		timeLimit = 60; // Time in seconds
		
		}
	
	public void setMazeUnit(MazeUnit unit,int x,int y){
		mazeUnits [x][y] = unit;
	}
	
	public MazeUnit getMazeUnit(int x, int y){
		return mazeUnits [x][y];
	}
	
	//Removes boxes that are not touching passage boxes.
	public void removeExtraBoxes(){
		
		ArrayList<Vector2> unitsToRemove = new ArrayList<Vector2>();
		
		for(int i=0;i<width;i++){
			
			for(int j=0;j<height;j++){
				if(mazeUnits[j][i].getType().equals("block")){
					System.out.print("#");
					
					//Is there a passable block around the current block
					boolean touchingPassable = false;
					
					int x=j;
					int y=i;
					
					if(x-1<=-1){	//test left
						//Block to the left is outside the grid
					}
					else{
						if(mazeUnits[x-1][y].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					
					if(x-1<=-1 || y-1<=-1){	//test upperleft
						//Block to the upperleft is outside the grid
					}
					else{
						if(mazeUnits[x-1][y-1].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					
					if(y+1>=height||x-1<=-1 ){	//test lower left
						//Block to the lower left is outside the grid
					}
					else{
						if(mazeUnits[x-1][y+1].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					
					
					
					
					if(x+1>=width){	//test right
						//Block to the right is outside the grid
					}
					else{
						if(mazeUnits[x+1][y].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					
					if(x+1>=width||y-1<=-1){	//test upperright
						//Block to the upperright is outside the grid
					}
					else{
						if(mazeUnits[x+1][y-1].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					if(x+1>=width||y+1>=-height){	//test lowerright
						//Block to the upperright is outside the grid
					}
					else{
						if(mazeUnits[x+1][y+1].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					
					if(y-1<=-1){	//test above
						//Block to the above is outside the grid
					}
					else{
						if(mazeUnits[x][y-1].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					
					
					if(y+1>=-height){	//test below
						//Block to the below is outside the grid
					}
					else{
						if(mazeUnits[x][y+1].getType().equals("passage")){
							touchingPassable =true;
						}
					}
					
					//Block isn't touching a passable block so it doesn't need
					// to collide with anything hence don't put it on the fixtures
					if(!touchingPassable){
						unitsToRemove.add(new Vector2(x,y));
					}
					
							
					
					
			
				}
				else{
					System.out.print(" ");
				}
			}
			System.out.println();
			
		}
		
		for(Vector2 item: unitsToRemove){
			mazeUnits[(int) item.x][(int) item.y].setType("passage");
		}
		
		
		
	for(int i=0;i<width;i++){
				
				for(int j=0;j<height;j++){
					if(mazeUnits[j][i].getType().equals("block")){
						System.out.print("#");
					}
					else{
						System.out.print(".");
					}
					
					}
				System.out.println();
	
					
					}
			
			
		}
	

}
