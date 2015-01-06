package com.tomgames.pit.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

	private Vector2 position;
	private Vector2 velocity;
	
	/** Tile position depends off position. */
	private GridPoint2 tilePosition;
	private GridPoint2 lastTilePosition;
	private Rectangle zone;
	protected GridPoint2[] contactPoints;
	
	public GameObject(float posX, float posY){
		position= new Vector2();
		velocity= new Vector2();
		tilePosition= new GridPoint2();
		lastTilePosition= new GridPoint2();
		contactPoints= new GridPoint2[4];
		this.setZone(new Rectangle(posX, posY, 32, 32));
		this.setPosition(posX, posY);
	}
	
	public abstract void render(SpriteBatch batch, ShapeRenderer shapeRender);
	public abstract void renderGUI(SpriteBatch batchGUI);
	public abstract void update(float delta);

	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Setting the position of a game object automatically updates his tile position (using tiles 32x32)
	 * @param posX
	 * @param posY
	 */
	public void setPosition(float posX, float posY) {
		this.position.set(posX, posY);
		//the tile position is based on the center of the object
		int newTilePosX= (int)((posX+zone.width/2)/32);
		int newTilePosY= (int)((posY+zone.height/2)/32);
		keepLastTilePosition(newTilePosX, newTilePosY);
		this.tilePosition.set(newTilePosX, newTilePosY);
		this.setZonePosition(posX, posY);
		contactPoints[0].set((int)(zone.x + zone.width/2), (int)(zone.y + 1));
		contactPoints[1].set((int)(zone.x + zone.width - 1), (int)(zone.y + zone.height/2));
		contactPoints[2].set((int)(zone.x + 1), (int)(zone.y + zone.height/2));
		contactPoints[3].set((int)(zone.x + zone.width/2), (int)(zone.y + zone.height - 1));
	}

	private void keepLastTilePosition(int newTilePosX, int newTilePosY){
		//check current tile and compare with new. If differs, then we just moved to a new tile
		if(getTilePosition().x != newTilePosX) lastTilePosition.x= getTilePosition().x;
		else lastTilePosition.x= newTilePosX;
		if(getTilePosition().y != newTilePosY) lastTilePosition.y= getTilePosition().y;
		else lastTilePosition.y= newTilePosY;
	}
	
	public void moveToLastTilePosition(){
		setPosition(getLastTilePosition().x * 32, getLastTilePosition().y * 32);
	}
	
	public GridPoint2 getTilePosition() {
		return tilePosition;
	}
	
	public GridPoint2 getLastTilePosition() {
		return lastTilePosition;
	}

	public void setTilePosition(int tilePosX, int tilePosY) {
		this.setPosition(tilePosX * 32, tilePosY * 32);
		//this.tilePosition.set(tilePosX, tilePosY);
	}

	public Rectangle getZone() {
		return zone;
	}

	public void setZone(Rectangle zone) {
		this.zone = zone;
		contactPoints[0]= new GridPoint2((int)(zone.x + zone.width/2), (int)(zone.y + 1));
		contactPoints[1]= new GridPoint2((int)(zone.x + zone.width - 1), (int)(zone.y + zone.height/2));
		contactPoints[2]= new GridPoint2((int)(zone.x + 1), (int)(zone.y + zone.height/2));
		contactPoints[3]= new GridPoint2((int)(zone.x + zone.width/2), (int)(zone.y + zone.height - 1));
	}

	public void setZonePosition(float x, float y){
		this.zone.setPosition(x, y);
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(float velX, float velY) {
		this.velocity.set(velX, velY);
	}
	
	public void setVelX(float vel){
		this.velocity.x= vel;
	}
	
	public void setVelY(float vel){
		this.velocity.y= vel;
	}

	/**
	 * [0] bot
	 * [1] right
	 * [2] left
	 * [3] top
	 */
	public GridPoint2[] getContactPoints() {
		return contactPoints;
	}
	
	
}//end class
