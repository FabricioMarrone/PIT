package com.tomgames.basic.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	/** ------------------- SOUNDS --------------------- */
	public Sound testSound;
	public Sound coin, coin2, shoot, shoot2, shootHit, shootHit2, shootHit3, ammo, ammo2, health, health2, bigTreasure;
	
	/** ------------------- MUSIC --------------------- */
	public Music testMusic;
	
	public void loadSounds(){
		//testSound= Gdx.audio.newSound(Gdx.files.internal("sounds/select.mp3"));
		coin= Gdx.audio.newSound(Gdx.files.internal("sounds/coin.mp3"));
		coin2= Gdx.audio.newSound(Gdx.files.internal("sounds/coin2.mp3"));
		shoot= Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
		shoot2= Gdx.audio.newSound(Gdx.files.internal("sounds/shoot2.mp3"));
		shootHit= Gdx.audio.newSound(Gdx.files.internal("sounds/shoothit.mp3"));
		shootHit2= Gdx.audio.newSound(Gdx.files.internal("sounds/shoothit2.mp3"));
		shootHit3= Gdx.audio.newSound(Gdx.files.internal("sounds/shoothit3.mp3"));
		ammo= Gdx.audio.newSound(Gdx.files.internal("sounds/ammo.mp3"));
		ammo2= Gdx.audio.newSound(Gdx.files.internal("sounds/ammo2.mp3"));
		health= Gdx.audio.newSound(Gdx.files.internal("sounds/health.mp3"));
		health2= Gdx.audio.newSound(Gdx.files.internal("sounds/health2.mp3"));
		bigTreasure= Gdx.audio.newSound(Gdx.files.internal("sounds/bigTreasure.mp3"));
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
