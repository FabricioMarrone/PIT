package com.tomgames.pit;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.Enemy;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.items.BigTreasure;
import com.tomgames.pit.entities.items.ClueItem;
import com.tomgames.pit.entities.items.Item;
import com.tomgames.pit.entities.items.MapItem;
import com.tomgames.pit.entities.items.ValueItem;

public class Island {

	private String islandName, islandGameName;
	private TiledMap tiledMap;
	private Island neighborhoodIslandN, neighborhoodIslandS, neighborhoodIslandE, neighborhoodIslandW;
	private int mapTileWidth, mapTileHeight, mapPixelWidth, mapPixelHeight, tileWidth, tileHeight;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private ArrayList<Item> items;
	private ArrayList<GridPoint2> digZones;
	private ArrayList<DestructibleBlock> destructibleBlocks;
	private ArrayList<Enemy> enemies;
	private int[] layersToRenderBeforePlayer= {TiledMapUtilities.LAYER_SCENE_1, TiledMapUtilities.LAYER_SCENE_2};
	private int[] layersToRenderAfterPlayer= {TiledMapUtilities.LAYER_SCENE_3, TiledMapUtilities.LAYER_SCENE_3a};
	private float total_secrets, secrets_Found, total_gold, gold_Found;
	private int total_bigTreasures, bigTreasures_Found, total_clues, clues_Found;
	private boolean allBigTreasuresTaken;
	private boolean mapTaken;
	private TextureRegion mapView;
	
	/**
	 * 
	 * @param islandName example: "testMap.tmx"
	 */
	public Island(String islandName){
		items= new ArrayList<Item>();
		destructibleBlocks= new ArrayList<DestructibleBlock>();
		enemies= new ArrayList<Enemy>();
		digZones= new ArrayList<GridPoint2>();
		tiledMap = new TmxMapLoader().load("maps/"+islandName+".tmx");
		
		this.islandName= islandName;
		
		MapProperties prop = tiledMap.getProperties();
		mapTileWidth = prop.get("width", Integer.class);
		mapTileHeight = prop.get("height", Integer.class);
		tileWidth = prop.get("tilewidth", Integer.class);
		tileHeight = prop.get("tileheight", Integer.class);
        mapPixelWidth = mapTileWidth * tileWidth;
        mapPixelHeight = mapTileHeight * tileHeight;
        islandGameName= prop.get("name", String.class);
        
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        TiledMapUtilities.loadAnimatedTiles(tiledMap);
        items= TiledMapUtilities.getItems(tiledMap);
        destructibleBlocks= TiledMapUtilities.getDestructibleBlocks(tiledMap);
        enemies= TiledMapUtilities.getEnemies(tiledMap);
        SecretMessages.setMessagesInBottles(islandName, getClueItems());
        digZones= TiledMapUtilities.getDigZones(tiledMap);
        
        allBigTreasuresTaken= false;
        mapTaken= false;
        bigTreasures_Found=0;
        total_bigTreasures=0;
		total_secrets=0;
		total_gold= 0;
		total_clues= 0;
		for(int i=0; i < items.size(); i++){
			if(items.get(i).getCurrentState() == Item.States.HIDDEN) total_secrets++;
			if(items.get(i) instanceof ValueItem) total_gold++;
			if(items.get(i) instanceof BigTreasure) total_bigTreasures++;
			if(items.get(i) instanceof ClueItem) total_clues++;
		}
		
		if(islandName.compareToIgnoreCase("m1")==0) setMapView(Assets.textures.mapView_m1);
		if(islandName.compareToIgnoreCase("m2")==0) setMapView(Assets.textures.mapView_m2);
		if(islandName.compareToIgnoreCase("m3")==0) setMapView(Assets.textures.mapView_m3);
		if(islandName.compareToIgnoreCase("m4")==0) setMapView(Assets.textures.mapView_m4);
		if(islandName.compareToIgnoreCase("m5")==0) setMapView(Assets.textures.mapView_m5);
		if(islandName.compareToIgnoreCase("m6")==0) setMapView(Assets.textures.mapView_m6);
		if(islandName.compareToIgnoreCase("m7")==0) setMapView(Assets.textures.mapView_m7);
		if(islandName.compareToIgnoreCase("m8")==0) setMapView(Assets.textures.mapView_m8);
		if(islandName.compareToIgnoreCase("m9")==0) setMapView(Assets.textures.mapView_m9);
	}
	
	public void renderLayersBeforePlayer(OrthographicCamera cam){
		tiledMapRenderer.setView(cam);
        tiledMapRenderer.render(layersToRenderBeforePlayer);
	}
	
	public void renderLayersAfterPlayer(OrthographicCamera cam){
		tiledMapRenderer.setView(cam);
        tiledMapRenderer.render(layersToRenderAfterPlayer);
	}
	
	/**
	 * If the item is "recently taked", it will not be rendered here.
	 * @param batch
	 */
	public void renderItems(SpriteBatch batch){
		//(we dont use the TILED ones, those are just for design purpose)
        for(int i=0; i < items.size(); i++){
        	if(!items.get(i).isRecentlyTaked()) items.get(i).render(batch, null);
        }
	}
	
	public void renderEnemies(SpriteBatch batch){
		for(int i=0; i < enemies.size(); i++){
			enemies.get(i).render(batch, null);
		}
	}
	
	public void renderRecentlyTakedItems(SpriteBatch batch){
		for(int i=0; i < items.size(); i++){
        	if(items.get(i).isRecentlyTaked()) items.get(i).render(batch, null);
        }
	}
	
	public void renderGUI(SpriteBatch batchGUI){
		/*
		//calculates how many items
		int totalItems= items.size();
		int hiddenCant=0;
		int visibleCant=0;
		int takenCant=0;
		for(int i=0; i < items.size(); i++){
			if(items.get(i).getCurrentState() == Item.States.HIDDEN) hiddenCant++;
			if(items.get(i).getCurrentState() == Item.States.DISCOVERED) visibleCant++;
			if(items.get(i).getCurrentState() == Item.States.TAKEN) takenCant++;
		}
		Assets.fonts.defaultFont.draw(batchGUI, "Items on island: H:"+hiddenCant+" D:"+visibleCant+" T:"+takenCant+" (Tot:"+totalItems+")", 10, 40);
		*/
		
		batchGUI.draw(Assets.textures.islandName_gui, (Gdx.graphics.getWidth()-Assets.textures.islandName_gui.getRegionWidth())/2, Gdx.graphics.getHeight() - 50);
		Assets.fonts.uiFontBig.draw(batchGUI, islandGameName, 380, Gdx.graphics.getHeight()-15);
		if(isAllBigTreasuresTaken()) batchGUI.draw(Assets.textures.laurels, 300, Gdx.graphics.getHeight() - 59);
		
		batchGUI.draw(Assets.textures.statsGui, Gdx.graphics.getWidth() - 280, Gdx.graphics.getHeight() - 162);
		
		Assets.fonts.uiFont.draw(batchGUI, this.clues_Found + "/" + this.total_clues, Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 95);
		
		if(total_secrets==0)Assets.fonts.uiFontWhite.draw(batchGUI, "SECRETS 100%", Gdx.graphics.getWidth() - 135, Gdx.graphics.getHeight() - 137);
		else Assets.fonts.uiFontWhite.draw(batchGUI, " SECRETS " + (int)((secrets_Found/total_secrets)*100) + "%", Gdx.graphics.getWidth() - 135, Gdx.graphics.getHeight() - 137);
		
		Assets.fonts.uiFont.draw(batchGUI, (int)((gold_Found/total_gold)*100) + "%", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 50);
		
		//if(isAllBigTreasuresTaken()) Assets.fonts.defaultFont.draw(batchGUI, "BIG TREASURES COMPLETED",Gdx.graphics.getWidth() - 300, 210);
		//else Assets.fonts.defaultFont.draw(batchGUI, "BIG TREASURES " + bigTreasures_Found + "/" + total_bigTreasures,Gdx.graphics.getWidth() - 300, 210);
		Assets.fonts.uiFont.draw(batchGUI, bigTreasures_Found + "/" + total_bigTreasures, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 95);
		
		if(isMapTaken()) Assets.fonts.uiFont.draw(batchGUI, "Yes", Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 50);
		else Assets.fonts.uiFont.draw(batchGUI, "No", Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 50);
		
		//render items GUI
		for(int i=0; i < items.size(); i++) items.get(i).renderGUI(batchGUI);
	}//end render gui
	
	/**
	 * Checks if the player collects items and stuff.
	 * @param delta
	 * @param player
	 */
	public void update(float delta, Player player){
		//check for island win condition
		if(bigTreasures_Found == total_bigTreasures){
			//THE ISLAND IS COMPLETE (on big treasures at least. Enough to win the game)
			allBigTreasuresTaken= true;
		}
		
		//check if player collect items
		checkForItemCollection(player);
		
		//update some stats (secrets and gold)
		int secrets_notFound= 0;
		gold_Found=0;
		total_gold= 0;
		clues_Found= 0;
		for(int i=0; i < items.size(); i++){
			if(items.get(i).getCurrentState() == Item.States.HIDDEN) secrets_notFound++;
			if(items.get(i) instanceof ValueItem){
				total_gold++;
				if(items.get(i).getCurrentState() == Item.States.TAKEN) gold_Found++;
			}
			if(items.get(i) instanceof ClueItem){
				if(items.get(i).getCurrentState() == Item.States.TAKEN) clues_Found++;
			}
		}
		secrets_Found= total_secrets - secrets_notFound;
		
		//check if player collides with enemies
		CollisionSystem.checkFor_PlayerEnemy_Collision(player, getEnemies());
		
		//update all items
		for(int i=0; i < items.size(); i++) items.get(i).update(delta);
		
		//check for destructible blocks with life 0 and remove them and change the tile sprite
		Iterator<DestructibleBlock> it= destructibleBlocks.iterator();
		while(it.hasNext()){
			DestructibleBlock block= it.next();
			if(block.getLife() == 0){
				TiledMapUtilities.putBlankTile(block.getTilePosition().x, block.getTilePosition().y, getTiledMap());
				it.remove();
			}
		}
		
		//update all enemies
		for(int i=0; i < enemies.size(); i++) enemies.get(i).update(delta);
		
		if(isMapTaken()) PIT.instance.gameplay.getMapaMundi().setMapView(getMapView(), islandName);
		
	}//end update

	private void checkForItemCollection(Player player){
		for(int i=0; i < items.size(); i++){
			if(items.get(i).getZone().overlaps(player.getZone())){
				if(items.get(i).getCurrentState()== Item.States.DISCOVERED){
					if(items.get(i).conditionToTakeItem(player)){
						items.get(i).setCurrentState(Item.States.TAKEN);
						items.get(i).itemTaken(player);
						
						if(items.get(i) instanceof BigTreasure) bigTreasures_Found++;
					}
				}
			}
		}
	}
	
	public boolean isAWaterTile(int tilePosX, int tilePosY){
		return TiledMapUtilities.isAWaterTile(tilePosX, tilePosY, getTiledMap());
	}
	
	public boolean isAbleToDig(int tilePosX, int tilePosY){
		for(int i= 0; i < digZones.size(); i++){
			if(digZones.get(i).x == tilePosX && digZones.get(i).y == tilePosY) {
				return true;
			}
		}
		return false;
		//return TiledMapUtilities.isAbleToDig(getTiledMap(), tilePosX, tilePosY);
	}
	
	/**
	 * This method is called when someone finish digging on the specified tile.
	 * @param tilePosX
	 * @param tilePosY
	 */
	public void digFinished(int tilePosX, int tilePosY){
		//remove sand
		TiledMapUtilities.removeSand(getTiledMap(), tilePosX, tilePosY);
		
		//remove digZone from array (to not dig again)
		Iterator<GridPoint2> it= digZones.iterator();
		while(it.hasNext()){
			GridPoint2 point= it.next();
			if(point.x == tilePosX && point.y == tilePosY){
				it.remove();
			}
		}
		
		//check if there is something on the tile
		for(int i=0; i < items.size(); i++){
			Item item= items.get(i);
			if(item.getTilePosition().x == tilePosX && item.getTilePosition().y == tilePosY){
				if(item.getCurrentState()==Item.States.HIDDEN){
					item.setCurrentState(Item.States.DISCOVERED);
				}
			}
		}
	}
	
	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public ArrayList<DestructibleBlock> getDestructibleBlocks() {
		return destructibleBlocks;
	}

	public int getMapTileWidth() {
		return mapTileWidth;
	}

	public int getMapTileHeight() {
		return mapTileHeight;
	}

	public int getMapPixelWidth() {
		return mapPixelWidth;
	}

	public int getMapPixelHeight() {
		return mapPixelHeight;
	}
	
	public Island getNeighborhoodIslandN() {
		return neighborhoodIslandN;
	}

	public Island getNeighborhoodIslandS() {
		return neighborhoodIslandS;
	}

	public Island getNeighborhoodIslandE() {
		return neighborhoodIslandE;
	}

	public Island getNeighborhoodIslandW() {
		return neighborhoodIslandW;
	}

	public void setNeighborhoodIslands(Island islandN, Island islandS, Island islandE, Island islandW){
		this.neighborhoodIslandN= islandN;
		this.neighborhoodIslandS= islandS;
		this.neighborhoodIslandE= islandE;
		this.neighborhoodIslandW= islandW;
	}
	
	public void addEnemy(Enemy e){
		enemies.add(e);
	}
	
	public ArrayList<Enemy> getEnemies(){
		return enemies;
	}
	
	public ArrayList<ClueItem> getClueItems(){
		ArrayList<ClueItem> clues= new ArrayList<ClueItem>();
		for(int i=0; i< items.size(); i++){
			if(items.get(i) instanceof ClueItem) clues.add((ClueItem)items.get(i));
		}
		return clues;
	}

	public boolean isAllBigTreasuresTaken() {
		return allBigTreasuresTaken;
	}
	
	public void addDiscoveredItem(Item i){
		i.setCurrentState(Item.States.DISCOVERED);
		items.add(i);
	}

	public boolean isMapTaken() {
		if(mapTaken) return true;
		
		for(int i= 0; i < items.size(); i++){
			if(items.get(i) instanceof MapItem){
				if(items.get(i).getCurrentState()==Item.States.TAKEN){
					mapTaken= true;
					return true;
				}else{
					return false;
				}
			}
		}
		
		return false;
	}

	public TextureRegion getMapView() {
		return mapView;
	}

	public void setMapView(TextureRegion mapView) {
		this.mapView = mapView;
	}

	public String getIslandName() {
		return islandName;
	}
	
	
}//end class
