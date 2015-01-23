package com.tomgames.pit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.Raft;

public class MapaMundi {

	private TextureRegion[][] mapaMundi;
	
	public MapaMundi(){
		mapaMundi= new TextureRegion[3][3];
		
	}
	
	public void render(SpriteBatch batchGUI){
		int posX= 170;	//testing
		int posY= 40;
		
		batchGUI.draw(Assets.textures.pergamino, posX-30, posY-25, 660, 660);
		
		for(int i=0; i < mapaMundi.length; i++){
			for(int ii=0; ii < mapaMundi[i].length; ii++){
				if(mapaMundi[i][ii] != null) batchGUI.draw(mapaMundi[i][ii], posX + i*200, posY + ii*200);
				else batchGUI.draw(Assets.textures.mapclouds, posX + i*200, posY + ii*200);
			}
		}

		//draw player and raft position
		Player p= PIT.instance.getPlayer();
		Raft r= PIT.instance.getRaft();
		GridPoint2 v= getCurrentIslandOffsets();
		batchGUI.draw(Assets.textures.yellowPoint, posX + (r.getTilePosition().x * 2) + v.x - 8, posY + (r.getTilePosition().y * 2) + v.y - 8);
		batchGUI.draw(Assets.textures.greenPoint, posX + (p.getTilePosition().x * 2) + v.x - 8, posY + (p.getTilePosition().y * 2) + v.y - 8);
	}
	
	public void update(float delta){
		/*
		elapsedTime+= delta;
		if(elapsedTime > interval){
			elapsedTime= 0;
			showPoints= !showPoints;
		}
		*/
	}
	
	
	public void setMapView(TextureRegion tex, String islandName){
		if(islandName.endsWith("1")) mapaMundi[0][2]= tex;
		if(islandName.endsWith("2")) mapaMundi[1][2]= tex;
		if(islandName.endsWith("3")) mapaMundi[2][2]= tex;
		if(islandName.endsWith("4")) mapaMundi[0][1]= tex;
		if(islandName.endsWith("5")) mapaMundi[1][1]= tex;
		if(islandName.endsWith("6")) mapaMundi[2][1]= tex;
		if(islandName.endsWith("7")) mapaMundi[0][0]= tex;
		if(islandName.endsWith("8")) mapaMundi[1][0]= tex;
		if(islandName.endsWith("9")) mapaMundi[2][0]= tex;
	}
	
	public GridPoint2 getCurrentIslandOffsets(){
		GridPoint2 v= new GridPoint2();
		
		Island i= PIT.instance.gameplay.getCurrentIsland();
		if(i.getIslandName().endsWith("1")) v.set(0, 400);
		if(i.getIslandName().endsWith("2")) v.set(200, 400);
		if(i.getIslandName().endsWith("3")) v.set(400, 400);
		if(i.getIslandName().endsWith("4")) v.set(0, 200);
		if(i.getIslandName().endsWith("5")) v.set(200, 200);
		if(i.getIslandName().endsWith("6")) v.set(400, 200);
		if(i.getIslandName().endsWith("7")) v.set(0, 0);
		if(i.getIslandName().endsWith("8")) v.set(200, 0);
		if(i.getIslandName().endsWith("9")) v.set(400, 0);
		
		return v;
	}
}//end class
