package com.tomgames.pit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tomgames.basic.ScreenClass;
import com.tomgames.basic.gui.Component;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.CollisionSystem;
import com.tomgames.pit.GameCamera;
import com.tomgames.pit.Island;
import com.tomgames.pit.MapaMundi;
import com.tomgames.pit.PIT;
import com.tomgames.pit.ShootSystem;
import com.tomgames.pit.entities.BadPirate;
import com.tomgames.pit.entities.Entity;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.Raft;
import com.tomgames.pit.entities.items.Item;
import com.tomgames.pit.entities.items.MapItem;

public class Gameplay extends ScreenClass{

	SpriteBatch batch, batchGUI;
	ShapeRenderer shapeRender;
	private GameCamera camera;
	
	private Island currentIsland;
	private Island m1, m2, m3, m4, m5, m6, m7, m8, m9;
	
	private MapaMundi mapaMundi;
	private boolean showMapaMundi=false;
	
	public ShootSystem shootSystem;
	
	public Player player;
	public Raft raft;
	
	float x, y;
	int mapTileSizeX= 100;
	int mapTileSizeY= 100;
	
	public Gameplay() {
		//ESTO DEBE HACERCE EN LOADING SCREEN
		Assets.loadAllResources();
		
		camera= new GameCamera();
		
		batch = new SpriteBatch();
		batchGUI = new SpriteBatch();
		shapeRender= new ShapeRenderer();
		
		init();
      
	}
	
	public void init(){
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
		mapaMundi= new MapaMundi();
		
		m1= new Island("m1");
		m2= new Island("m2");
		m3= new Island("m3");
		m4= new Island("m4");
		m5= new Island("m5");
		m6= new Island("m6");
		m7= new Island("m7");
		m8= new Island("m8");
		m9= new Island("m9");
		
		m1.setNeighborhoodIslands(null, m4, m2, null);
		m2.setNeighborhoodIslands(null, m5, m3, m1);
		m3.setNeighborhoodIslands(null, m6, null, m2);
		m4.setNeighborhoodIslands(m1, m7, m5, null);
		m5.setNeighborhoodIslands(m2, m8, m6, m4);
		m6.setNeighborhoodIslands(m3, m9, null, m5);
		m7.setNeighborhoodIslands(m4, null, m8, null);
		m8.setNeighborhoodIslands(m5, null, m9, m7);
		m9.setNeighborhoodIslands(m6, null, null, m8);
		
		//set current island
		//changeToIsland(m8);
		
		changeToIsland(m4);
		player= new Player(79 * 32, (99-51) *32);
		raft= new Raft(85 * 32, (99-51) * 32);
		
		//raft= new Raft(66 * 32, (99-26) * 32);
		//player= new Player(29 * 32, (99-21) *32);
		player.setLifePoints(100);
		player.setAmmo(15);
		player.setCurrentAttackMode(Entity.AttackMode.RANGED);

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
		raft.render(batch, shapeRender);
		currentIsland.renderEnemies(batch);
		currentIsland.renderItems(batch);
		player.render(batch, shapeRender);
		shootSystem.render(batch, shapeRender);
		batch.end();
		
		currentIsland.renderLayersAfterPlayer(camera.getOrthographicCamera());
		
		batch.begin();
		currentIsland.renderRecentlyTakedItems(batch);
		player.renderTalk(batch);
		batch.end();
		
		//render gui
		batchGUI.begin();
		Assets.fonts.defaultFont.draw(batchGUI, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
		Assets.fonts.defaultFont.draw(batchGUI, "Map available: " + isMapAvailable(), Gdx.graphics.getWidth()-300, 300);
		Assets.fonts.defaultFont.draw(batchGUI, "Show mapa mundi: " +showMapaMundi, Gdx.graphics.getWidth()-300, 320);
		player.renderGUI(batchGUI);
		currentIsland.renderGUI(batchGUI);
		shootSystem.renderInfo(batchGUI);
		raft.renderGUI(batchGUI);
		if(showMapaMundi) mapaMundi.render(batchGUI);
		PIT.instance.gui.render(batchGUI);
		batchGUI.end();
		
		this.update(PIT.confirmDelta(delta));
		//this.update(delta);
	}//end render

	public void update(float delta){
		//Check for island change
		int offset= 2;	//two is minimun value!! (tested with 1 and doesnt work properly)
		//--Right limit
		if(player.getTilePosition().x >= mapTileSizeX-1){
			if(getCurrentIsland().getNeighborhoodIslandE()!=null){
				player.setPosition(offset * 32, player.getTilePosition().y * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandE());
			}
		}
		//--Left limit
		if(player.getTilePosition().x <= 1){
			if(getCurrentIsland().getNeighborhoodIslandW()!=null){
				player.setPosition((mapTileSizeX-offset) * 32, player.getTilePosition().y * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandW());
			}
		}
		//--Top limit
		if(player.getTilePosition().y >= mapTileSizeY-1){
			if(getCurrentIsland().getNeighborhoodIslandN()!=null){
				player.setPosition(player.getTilePosition().x * 32, offset * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandN());
			}
		}
		//--Bottom limit
		if(player.getTilePosition().y <= 1){
			if(getCurrentIsland().getNeighborhoodIslandS()!=null){
				player.setPosition(player.getTilePosition().x * 32, (mapTileSizeY-offset) * 32);
				changeToIsland(getCurrentIsland().getNeighborhoodIslandS());
			}
		}
		
		player.update(delta);
		currentIsland.update(delta, player);
		shootSystem.update(delta, player, currentIsland.getDestructibleBlocks());
		mapaMundi.update(delta);
		if(!player.isRaftAvailable() && player.getZone().overlaps(raft.getZone())) player.jumpToRaft(raft);
		CollisionSystem.checkForEntityCollision(player, currentIsland.getDestructibleBlocks());
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) player.dig();
		if(Gdx.input.isKeyJustPressed(Keys.L)) init();
		if(Gdx.input.isKeyJustPressed(Keys.M)) showMap();
		
		camera.update(delta);
		
		PIT.instance.gui.update(delta);
		
		
	}//end update
	
	public void showMap(){
		showMapaMundi= !showMapaMundi;
	}
	
	
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
	
	public boolean isMapAvailable(){
		return getCurrentIsland().isMapTaken();
	}
	
	public MapaMundi getMapaMundi(){
		return mapaMundi;
	}
}//end class
