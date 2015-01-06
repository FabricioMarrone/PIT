package com.tomgames.pit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tomgames.basic.ScreenClass;
import com.tomgames.basic.gui.Component;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.Entity;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.Raft;

public class Gameplay extends ScreenClass{

	SpriteBatch batch, batchGUI;
	ShapeRenderer shapeRender;
	private GameCamera camera;
	
	private Island currentIsland, island, islandE, islandN;
	public ShootSystem shootSystem;
	
	public Player player;
	Raft raft;
	
	float x, y;
	int mapTileSizeX= 80;
	int mapTileSizeY= 40;
	
	public Gameplay() {
		//ESTO DEBE HACERCE EN LOADING SCREEN
		Assets.loadAllResources();
		
		camera= new GameCamera();
		
		batch = new SpriteBatch();
		batchGUI = new SpriteBatch();
		shapeRender= new ShapeRenderer();
		
		shootSystem= new ShootSystem();
		
		//tiledMap = new TmxMapLoader().load("maps/testMap.tmx");
        //tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //TiledMapAnimator.loadAnimatedTiles(tiledMap);
		/*
        MapProperties prop = tiledMap.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;
        */
		
        island= new Island("testMap.tmx");
        islandE= new Island("testMapE.tmx");
        islandN= new Island("testMapN.tmx");
        
        island.setNeighborhoodIslands(islandN, null, islandE, null);
        islandE.setNeighborhoodIslands(null, null, null, island);
        islandN.setNeighborhoodIslands(null, island, null, null);
        
        changeToIsland(island);
        
        player= new Player(54 * 32, 8 *32);
        player.setLifePoints(90);
        player.setAmmo(15);
        player.setCurrentAttackMode(Entity.AttackMode.RANGED);
        raft= new Raft(54 * 32, 7 * 32);
        
        camera.follow(player);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.updateMatrixs();
		batch.setProjectionMatrix(camera.getCombined());
        shapeRender.setProjectionMatrix(camera.getCombined());
        
        currentIsland.renderLayersBeforePlayer(camera.getOrthographicCamera());
        CollisionSystem.render(shapeRender);
        
		batch.begin();
		currentIsland.renderItems(batch);
		raft.render(batch, shapeRender);
		player.render(batch, shapeRender);
		currentIsland.renderEnemies(batch);
		shootSystem.render(batch, shapeRender);
		batch.end();
		
		currentIsland.renderLayersAfterPlayer(camera.getOrthographicCamera());
		
		batch.begin();
		currentIsland.renderRecentlyTakedItems(batch);
		batch.end();
		
		//render gui
		batchGUI.begin();
		Assets.fonts.defaultFont.draw(batchGUI, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
		player.renderGUI(batchGUI);
		currentIsland.renderGUI(batchGUI);
		shootSystem.renderInfo(batchGUI);
		raft.renderGUI(batchGUI);
		PIT.instance.gui.render(batchGUI);
		batchGUI.end();
		
		this.update(PIT.confirmDelta(delta));
		//this.update(delta);
	}//end render

	public void update(float delta){
		//Check for island change
		//--Right limit
		if(player.getTilePosition().x >= mapTileSizeX-1){
			if(getCurrentIsland().getNeighborhoodIslandE()!=null){
				player.setPosition(2 * 32, player.getTilePosition().y * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandE());
			}
		}
		//--Left limit
		if(player.getTilePosition().x <= 1){
			if(getCurrentIsland().getNeighborhoodIslandW()!=null){
				player.setPosition((mapTileSizeX-2) * 32, player.getTilePosition().y * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandW());
			}
		}
		//--Top limit
		if(player.getTilePosition().y >= mapTileSizeY-1){
			if(getCurrentIsland().getNeighborhoodIslandN()!=null){
				player.setPosition(player.getTilePosition().x * 32, 2 * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandN());
			}
		}
		//--Bottom limit
		if(player.getTilePosition().y <= 1){
			if(getCurrentIsland().getNeighborhoodIslandS()!=null){
				player.setPosition(player.getTilePosition().x * 32, (mapTileSizeY-2) * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandS());
			}
		}
		
		player.update(delta);
		currentIsland.update(delta, player);
		shootSystem.update(delta, player, currentIsland.getDestructibleBlocks());
		if(!player.isRaftAvailable() && player.getZone().overlaps(raft.getZone())) player.jumpToRaft(raft);
		CollisionSystem.checkForEntityCollision(player, currentIsland.getDestructibleBlocks());
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) player.dig();
		
		camera.update(delta);
		
		PIT.instance.gui.update(delta);
		
		
	}//end update
	
	public void changeToIsland(Island i){
		currentIsland= i;
		CollisionSystem.setTiledMap(i.getTiledMap());
		camera.setLimits(i.getTiledMap());
		shootSystem.clearAll();
	}
	
	@Override
	public void gui_event(Component e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public Island getCurrentIsland(){
		return currentIsland;
	}

	public GameCamera getCamera() {
		return camera;
	}
	
	
}//end class
