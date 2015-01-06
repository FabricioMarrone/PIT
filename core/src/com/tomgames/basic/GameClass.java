package com.tomgames.basic;

import com.badlogic.gdx.Game;
import com.tomgames.basic.gui.UserInterface;

public abstract class GameClass extends Game{

	public static String gameTitle;
	public static String gameVersion;
	public static String gameReleaseVersionDate;
	protected static float maxDelta= 0.025f;
	public UserInterface gui;
	
	public ScreenClass getActualScreen(){
		return (ScreenClass)this.getScreen();
	}
	
	public static float confirmDelta(float delta){
		if(delta > maxDelta) return maxDelta;
		
		return delta;
	}
	
	public void setMaxDelta(float max){
		maxDelta= max;
	}
}//end class
