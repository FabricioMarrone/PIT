package com.tomgames.pit.entities.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.GameObject;
import com.tomgames.pit.entities.Player;

/**
 * An item is an object of the map that can be collected by the player.
 * @author F.Marrone
 *
 */
public class Item extends GameObject{

	private int value;
	public static enum States{HIDDEN, DISCOVERED, TAKEN}
	private States currentState;
	private TextureRegion texture;
	
	private float takedAnimTime= 0.5f;
	private float elapsedTakedAnimTime;
	private int takedAnimOffset;
	private boolean recentlyTaked= false;
	
	public Item(int tilePosX, int tilePosY) {
		super(tilePosX * 32, tilePosY * 32);
		
		setTexture(Assets.textures.coin);
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapeRender) {
		if(getCurrentState() == States.DISCOVERED || recentlyTaked){
			batch.draw(getTexture(), getPosition().x, getPosition().y + takedAnimOffset);
		}
		
	}

	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		if(recentlyTaked){
			elapsedTakedAnimTime+= delta;
			takedAnimOffset++;
			if(elapsedTakedAnimTime > takedAnimTime){
				recentlyTaked= false;
			}
		}
		
	}

	/**
	 * This method is called when the item is taken.
	 */
	public void itemTaked(Player player){
		//This method should be override to add behavior. Dont forget to call super.
		player.addScore(value);
		
		recentlyTaked= true;
		elapsedTakedAnimTime= 0;
		takedAnimOffset= 0;
	}

	/**
	 * Returns true if the player is able to take the item. Sub-clases could override this method.
	 * @param player
	 * @return
	 */
	public boolean conditionToTakeItem(Player player){
		return true;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public States getCurrentState() {
		return currentState;
	}

	public void setCurrentState(States currentState) {
		this.currentState = currentState;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public boolean isRecentlyTaked() {
		return recentlyTaked;
	}
	
	
}//end class
