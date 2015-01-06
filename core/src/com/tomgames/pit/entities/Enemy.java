package com.tomgames.pit.entities;

public abstract class Enemy extends Entity{

	public Enemy(float posX, float posY) {
		super(posX, posY);
		
	}

	public abstract void shoot(Directions dir);
	public abstract void moveTo(int tileX, int tileY);
	
}//end class
