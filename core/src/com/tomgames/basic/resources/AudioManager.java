package com.tomgames.basic.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	/** ------------------- SOUNDS --------------------- */
	public Sound testSound;
	public Sound coin, coin2;
	
	/** ------------------- MUSIC --------------------- */
	public Music testMusic;
	
	public void loadSounds(){
		//testSound= Gdx.audio.newSound(Gdx.files.internal("sounds/select.mp3"));
		coin= Gdx.audio.newSound(Gdx.files.internal("sounds/coin.mp3"));
		coin2= Gdx.audio.newSound(Gdx.files.internal("sounds/coin2.mp3"));
	}
	
	public void loadMusic(){
		//testMusic= Gdx.audio.newMusic(Gdx.files.internal("sounds/loginScreenMusic_lostVillage.mp3"));
		//testMusic.setLooping(true);
	}
	
	public void disposeAll(){
		//Dispose music...
		//testMusic.dispose();
		
		//Dispose sounds...
		//testSound.dispose();
	}
	
}//end class
