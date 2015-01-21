package com.tomgames.pit.entities.items;

import com.tomgames.basic.resources.Assets;

public class MapItem extends Item{

	public MapItem(int tilePosX, int tilePosY) {
		super(tilePosX, tilePosY);

		setTexture(Assets.textures.map);
	}

	
}//end class
