package com.tomgames.pit;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public class DestructibleBlock {

	private GridPoint2 tilePosition;
	private Rectangle zone;
	private int life;
	
	public DestructibleBlock(int tilePosX, int tilePosY){
		tilePosition= new GridPoint2(tilePosX, tilePosY);
		zone= new Rectangle(tilePosX * 32, tilePosY * 32, 32, 32);
		life= 3;
	}
	
	public boolean isDestroyed(){
		if(life == 0) return true;
		else return false;
	}

	public GridPoint2 getTilePosition() {
		return tilePosition;
	}

	public Rectangle getZone() {
		return zone;
	}

	public int getLife() {
		return life;
	}
	
	public void applyDamage(){
		life-= 1;
		if(life < 0) life= 0;
	}
	
	public void applyMassiveDamage(){
		life= 0;
	}
}//end class
