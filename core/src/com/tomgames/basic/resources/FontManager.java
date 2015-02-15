package com.tomgames.basic.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontManager {

	public BitmapFont defaultFont, uiFont, uiFontWhite, uiFontBig, clueFont;
	
	public void loadFonts(){
		defaultFont= new BitmapFont(false);
		defaultFont.setColor(Color.WHITE);
		
		uiFont= new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), Gdx.files.internal("fonts/ui_font_0.png"), false);
		uiFont.setColor(0.498f, 0.415f, 0f, 1f);
		
		uiFontWhite= new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"), Gdx.files.internal("fonts/ui_font_0.png"), false);
		uiFontWhite.setColor(0.86f, 0.86f, 0.86f, 1);
		
		uiFontBig= new BitmapFont(Gdx.files.internal("fonts/ui_font26.fnt"), Gdx.files.internal("fonts/ui_font26_0.png"), false);
		uiFontBig.setColor(0.498f, 0.415f, 0f, 1f);
		
		clueFont= new BitmapFont(Gdx.files.internal("fonts/clue_font.fnt"), Gdx.files.internal("fonts/clue_font_0.png"), false);
		clueFont.setColor(0.25f, 0.25f, 0.25f, 1f);
		
	}
	
	public void disposeAll(){
		defaultFont.dispose();
	}
}//end class
