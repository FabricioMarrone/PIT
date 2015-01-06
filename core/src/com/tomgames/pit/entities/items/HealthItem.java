package com.tomgames.pit.entities.items;

import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.PIT;
import com.tomgames.pit.entities.Player;

public class HealthItem extends Item{

	private int cantHealthPoints;
	
	public HealthItem(int tilePosX, int tilePosY) {
		super(tilePosX, tilePosY);
		
		setTexture(Assets.textures.health);
	}

	@Override
	public void itemTaked(Player player) {
		super.itemTaked(player);
		player.addLifePoints(cantHealthPoints);
	}

	public int getCantHealthPoints() {
		return cantHealthPoints;
	}

	public void setCantHealthPoints(int cantHealthPoints) {
		this.cantHealthPoints = cantHealthPoints;
	}

	@Override
	public boolean conditionToTakeItem(Player player) {
		if(player.getLifePoints() == 100) return false;
		return true;
	}

	
}//end class
