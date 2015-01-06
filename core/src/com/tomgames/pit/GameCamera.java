package com.tomgames.pit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.tomgames.pit.entities.GameObject;

public class GameCamera {

	private OrthographicCamera cam;
	private GameObject following;
	private int mapPixelWidth;
    private int mapPixelHeight;
    private Rumble rumble;
    
	/**
	 * Camera with the screen size.
	 */
	public GameCamera(){
		rumble= new Rumble();
		following= null;
		cam= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
	}
	
	/**
	 * To stop following something, use null.
	 * @param object
	 */
	public void follow(GameObject object){
		following= object;
	}
	
	public void setLimits(TiledMap tiledMap){
		MapProperties prop = tiledMap.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;
	}
	
	public void update(float delta){
		if(following != null){
			//following the game object...
			int newX= (int)following.getPosition().x;
			int newY= (int)following.getPosition().y;
			//camera limits...
			if(newX < Gdx.graphics.getWidth()/2) newX= Gdx.graphics.getWidth()/2;
			if(newY < Gdx.graphics.getHeight()/2) newY= Gdx.graphics.getHeight()/2;
			if(newX > mapPixelWidth - Gdx.graphics.getWidth()/2) newX= mapPixelWidth - Gdx.graphics.getWidth()/2;
			if(newY > mapPixelHeight - Gdx.graphics.getHeight()/2) newY= mapPixelHeight - Gdx.graphics.getHeight()/2;
			//new pos
			setPosition(newX, newY, 0);
			
			//we update rumble in case of screen shake
			rumble.update(delta, getOrthographicCamera(), newX, newY);
		}
	}
	
	public void updateMatrixs(){
		cam.update();
	}
	
	public Matrix4 getCombined(){
		return cam.combined;
	}
	
	public OrthographicCamera getOrthographicCamera(){
		return cam;
	}
	
	public void setPosition(float x, float y, float z){
		cam.position.set(x, y, z);
	}
	
	public void rumble(float power, float time){
		this.rumble.rumble(power, time);
	}
}//end class
