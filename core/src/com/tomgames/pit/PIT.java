package com.tomgames.pit;

import java.util.Random;

import com.tomgames.basic.GameClass;
import com.tomgames.basic.ScreenClass;
import com.tomgames.basic.gui.UserInterface;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.Raft;
import com.tomgames.pit.screens.Gameplay;
import com.tomgames.pit.screens.SplashScreen;
import com.tomgames.pit.screens.TestScreen;

public class PIT extends GameClass {
	
	/** Screens */
	public TestScreen testScreen;
	public SplashScreen splashScreen;
	public Gameplay gameplay;
	
	
	public static PIT instance;
	public Random random;
	
	@Override
	public void create() {
		instance= this;
		gui= new UserInterface();
		
		random= new Random();
		
		//testScreen= new TestScreen();
		splashScreen= new SplashScreen();
		gameplay= new Gameplay();
		
		//this.setScreen(testScreen);
		this.setScreen(splashScreen);
		//this.setScreen(gameplay);
	}
	
	public Player getPlayer(){
		return gameplay.player;
	}
	
	public Raft getRaft(){
		return gameplay.raft;
	}
}//end class
