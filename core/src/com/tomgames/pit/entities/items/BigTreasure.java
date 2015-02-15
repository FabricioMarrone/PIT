package com.tomgames.pit.entities.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.Settings;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.items.Item.States;

public class BigTreasure extends Item{

	public BigTreasure(int tilePosX, int tilePosY) {
		super(tilePosX, tilePosY);
		
		setTexture(Assets.textures.bigTreasure);
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapeRender) {
		if(getCurrentState() == States.DISCOVERED || isRecentlyTaked()){
			batch.draw(getTexture(), getPosition().x - takedAnimOffset/2, getPosition().y + takedAnimOffset, 
					getTexture().getRegionWidth()+takedAnimOffset, 
					getTexture().getRegionHeight()+takedAnimOffset);
		}
		
	}
	
	@Override
	public void itemTaken(Player player) {
		super.itemTaken(player);
		
		if(Settings.sounds) Assets.audio.bigTreasure.play();
	}

}//end class
