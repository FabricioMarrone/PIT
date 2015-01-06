package com.tomgames.pit;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.Enemy;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.items.Item;

public class Island {

	private TiledMap tiledMap;
	private Island neighborhoodIslandN, neighborhoodIslandS, neighborhoodIslandE, neighborhoodIslandW;
	private int mapTileWidth, mapTileHeight, mapPixelWidth, mapPixelHeight, tileWidth, tileHeight;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private ArrayList<Item> items;
	private ArrayList<DestructibleBlock> destructibleBlocks;
	private ArrayList<Enemy> enemies;
	private int[] layersToRenderBeforePlayer= {TiledMapUtilities.LAYER_SCENE_1, TiledMapUtilities.LAYER_SCENE_2};
	private int[] layersToRenderAfterPlayer= {TiledMapUtilities.LAYER_SCENE_3};
	
	/**
	 * 
	 * @param islandName example: "testMap.tmx"
	 */
	public Island(String islandName){
		items= new ArrayList<Item>();
		destructibleBlocks= new ArrayList<DestructibleBlock>();
		enemies= new ArrayList<Enemy>();
		tiledMap = new TmxMapLoader().load("maps/"+islandName);
		
		MapProperties prop = tiledMap.getProperties();
		mapTileWidth = prop.get("width", Integer.class);
		mapTileHeight = prop.get("height", Integer.class);
		tileWidth = prop.get("tilewidth", Integer.class);
		tileHeight = prop.get("tileheight", Integer.class);
        mapPixelWidth = mapTileWidth * tileWidth;
        mapPixelHeight = mapTileHeight * tileHeight;
        
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        TiledMapUtilities.loadAnimatedTiles(tiledMap);
        items= TiledMapUtilities.getItems(tiledMap);
        destructibleBlocks= TiledMapUtilities.getDestructibleBlocks(tiledMap);
        enemies= TiledMapUtilities.getEnemies(tiledMap);
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
		
	}//end render gui
	
	/**
	 * Checks if the player collects items and stuff.
	 * @param delta
	 * @param player
	 */
	public void update(float delta, Player player){
		//check if player collect items
		checkForItemCollection(player);
		
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
		
	}//end update

	private void checkForItemCollection(Player player){
		for(int i=0; i < items.size(); i++){
			if(items.get(i).getZone().overlaps(player.getZone())){
				if(items.get(i).getCurrentState()== Item.States.DISCOVERED){
					if(items.get(i).conditionToTakeItem(player)){
						items.get(i).setCurrentState(Item.States.TAKEN);
						items.get(i).itemTaked(player);
					}
				}
			}
		}
	}
	
	public boolean isAWaterTile(int tilePosX, int tilePosY){
		return TiledMapUtilities.isAWaterTile(tilePosX, tilePosY, getTiledMap());
	}
	
	public boolean isAbleToDig(int tilePosX, int tilePosY){
		return TiledMapUtilities.isAbleToDig(getTiledMap(), tilePosX, tilePosY);
	}
	
	/**
	 * This method is called when someone finish digging on the specified tile.
	 * @param tilePosX
	 * @param tilePosY
	 */
	public void digFinished(int tilePosX, int tilePosY){
		//remove sand
		TiledMapUtilities.removeSand(getTiledMap(), tilePosX, tilePosY);
		
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
}//end class
