package com.tomgames.basic.resources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontManager {

	public BitmapFont defaultFont;
	
	public void loadFonts(){
		defaultFont= new BitmapFont(false);
		defaultFont.setColor(Color.WHITE);
		
		//gameText= new BitmapFont(Gdx.files.internal("fonts/GameText.fnt"), Gdx.files.internal("fonts/GameText_0.png"), false);
		//gameText.setColor(Color.WHITE);
	}
	
	public void disposeAll(){
		defaultFont.dispose();
	}
}//end class
