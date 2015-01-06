package com.tomgames.pit.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tomgames.basic.resources.Assets;

public class Raft extends GameObject{

	private boolean tripulated;
	
	public Raft(float posX, float posY) {
		super(posX, posY);
		
		setTripulated(false);
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapeRender) {
		if(!isTripulated()) batch.draw(Assets.textures.raftalone, getPosition().x, getPosition().y);
		
	}

	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		Assets.fonts.defaultFont.draw(batchGUI, "Raft tile pos: ("+getTilePosition().x+" / "+getTilePosition().y+")", 350, 60);
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	public boolean isTripulated() {
		return tripulated;
	}

	public void setTripulated(boolean tripulated) {
		this.tripulated = tripulated;
	}

}//end class
