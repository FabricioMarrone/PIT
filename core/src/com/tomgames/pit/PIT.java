package com.tomgames.pit;

import com.tomgames.basic.GameClass;
import com.tomgames.basic.ScreenClass;
import com.tomgames.basic.gui.UserInterface;
import com.tomgames.pit.entities.Player;

public class PIT extends GameClass {
	
	public Gameplay gameplay;
	public static PIT instance;
	
	@Override
	public void create() {
		instance= this;
		gui= new UserInterface();
		
		gameplay= new Gameplay();
		this.setScreen(gameplay);
	}
	
	public Player getPlayer(){
		return gameplay.player;
	}
}//end class
