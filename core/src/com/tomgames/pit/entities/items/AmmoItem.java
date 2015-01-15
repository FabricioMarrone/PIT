package com.tomgames.pit.entities.items;

import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.PIT;
import com.tomgames.pit.entities.Player;

public class AmmoItem extends Item{

	private int cantAmmo;
	
	public AmmoItem(int tilePosX, int tilePosY) {
		super(tilePosX, tilePosY);
		
		setTexture(Assets.textures.ammo);
	}

	
	
	@Override
	public void itemTaken(Player player) {
		super.itemTaken(player);
		player.addAmmo(cantAmmo);
	}

	public int getCantAmmo() {
		return cantAmmo;
	}

	public void setCantAmmo(int cantAmmo) {
		this.cantAmmo = cantAmmo;
	}

}//end class
