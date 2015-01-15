package com.tomgames.basic.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {

	public TextureRegion testTex, pirate, coin, coin2, ammo, health, health2, raft, raftalone, arrow, shoot, sword, fireball, totem, 
	badPirate, deadbadpirate, bigTreasure, clue;
	
	private Texture pirateSprites;
	public TextureRegion pirateW, pirateE, pirateN, pirateS, pirateSW, pirateSE, pirateNW, pirateNE, dead;
	
	protected void loadTextures(){
		testTex= new TextureRegion(new Texture("images/testTex.png"));
		pirate= new TextureRegion(new Texture("images/player/pirate.png"));
		raft= new TextureRegion(new Texture("images/player/raft.png"));
		raftalone= new TextureRegion(new Texture("images/player/raftalone.png"));
		coin= new TextureRegion(new Texture("images/coin.png"));
		coin2= new TextureRegion(new Texture("images/coin2.png"));
		bigTreasure= new TextureRegion(new Texture("images/bigtreasure.png"));
		ammo= new TextureRegion(new Texture("images/ammo.png"));
		health= new TextureRegion(new Texture("images/health.png"));
		health2= new TextureRegion(new Texture("images/health2.png"));
		arrow= new TextureRegion(new Texture("images/arrow.png"));
		shoot= new TextureRegion(new Texture("images/shoot.png"));
		sword= new TextureRegion(new Texture("images/sword.png"));
		fireball= new TextureRegion(new Texture("images/fireball.png"));
		totem= new TextureRegion(new Texture("images/totem.png"));
		badPirate= new TextureRegion(new Texture("images/badpirate.png"));
		deadbadpirate= new TextureRegion(new Texture("images/deadbadpirate.png"));
		clue= new TextureRegion(new Texture("images/clue.png"));
		
		pirateSprites= new Texture("images/player/piratesprites.png");
		TextureRegion[][] sprites= TextureRegion.split(pirateSprites, 32, 32);
		pirateW= sprites[0][0];
		pirateS= sprites[0][1];
		pirateN= sprites[0][2];
		pirateE= sprites[0][3];
		pirateSW= sprites[1][0];
		pirateSE= sprites[1][1];
		pirateNW= sprites[1][2];
		pirateNE= sprites[1][3];
		dead= new TextureRegion(new Texture("images/player/dead.png"));
		
		/*
		//Cargamos el atlas del player...
		playerAtlas= new TextureAtlas(Gdx.files.internal("images/playerAtlas.txt"));
		
		//Creamos animacion de RUNNING
		TextureRegion running1= playerAtlas.findRegion("playerRunning1");
		TextureRegion running2= playerAtlas.findRegion("playerRunning2");
		TextureRegion running3= playerAtlas.findRegion("playerRunning3");
		Array<TextureRegion> playerRunningTextures= new Array<TextureRegion>();
		playerRunningTextures.add(running1);
		playerRunningTextures.add(running2);
		playerRunningTextures.add(running3);
		playerRunning= new Animation(0.15f, playerRunningTextures);
		playerRunning.setPlayMode(Animation.LOOP);
		
		//Creamos animacion de JUMP FORWARD
		TextureRegion jumpForward1= playerAtlas.findRegion("playerJumpForward1");
		TextureRegion jumpForward2= playerAtlas.findRegion("playerJumpForward2");
		TextureRegion jumpForward3= playerAtlas.findRegion("playerJumpForward3");
		Array<TextureRegion> playerJumpForwardTextures= new Array<TextureRegion>();
		playerJumpForwardTextures.add(jumpForward1);
		playerJumpForwardTextures.add(jumpForward2);
		playerJumpForwardTextures.add(jumpForward3);
		playerJumpForward= new Animation(0.25f, playerJumpForwardTextures);
		playerJumpForward.setPlayMode(Animation.NORMAL);
		*/
	}
	
	public void disposeAll(){
		//testTex.getTexture().dispose();
	}
}//end class
