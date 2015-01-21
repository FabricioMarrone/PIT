package com.tomgames.pit.entities.items;

import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.PIT;
import com.tomgames.pit.Settings;
import com.tomgames.pit.entities.Player;

/**
 * This class its kinda a "bug fix".
 * @author F.Marrone
 *
 */
public class ValueItem extends Item{

	public ValueItem(int tilePosX, int tilePosY) {
		super(tilePosX, tilePosY);
		
	}

	@Override
	public void itemTaken(Player player) {
		super.itemTaken(player);
		
		if(Settings.sounds){
			if(PIT.instance.random.nextBoolean()) Assets.audio.coin.play();
			else Assets.audio.coin2.play();
		}
	}

	
}
