package com.tomgames.pit;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tomgames.pit.entities.Enemy;
import com.tomgames.pit.entities.Entity;
import com.tomgames.pit.entities.GameObject;
import com.tomgames.pit.entities.Totem;
import com.tomgames.pit.entities.items.AmmoItem;
import com.tomgames.pit.entities.items.HealthItem;
import com.tomgames.pit.entities.items.Item;
import com.tomgames.pit.entities.shoots.Shoot;

/**
 * This class encapsules all related to tiled maps that TILED "do not support" natively.
 * @author F. Marrone
 *
 */
public class TiledMapUtilities {
	
	public static final int TILESET_DESERT= 0;
	
	public static final int LAYER_ENEMIES= 5;
	public static final int LAYER_ITEMS= 4;
	public static final int LAYER_SCENE_3= 3;
	public static final int LAYER_SCENE_2= 2;
	public static final int LAYER_SCENE_1= 1;
	public static final int LAYER_COLLISION= 0;
	
	public static ArrayList<Enemy> getEnemies(TiledMap tiledMap){
		ArrayList<Enemy> enemies= new ArrayList<Enemy>();
		TiledMapTileLayer enemiesLayer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_ENEMIES);
		for(int x= 0; x < enemiesLayer.getWidth(); x++){
			for(int y= 0; y < enemiesLayer.getHeight(); y++){
				Cell cell= enemiesLayer.getCell(x, y);
		    	if(cell == null) continue;
		    	TiledMapTile tile= cell.getTile();
		    	if(tile == null) continue;
		    	
		    	Enemy e= null;
		    	
		    	if(tile.getProperties().containsKey("enemy")){
		    		String enemyType= tile.getProperties().get("enemy").toString();
		    		
		    		if(enemyType.compareToIgnoreCase("totem")==0){
		    			String direction= tile.getProperties().get("direction").toString();
			    		String shootType= tile.getProperties().get("shoot").toString();
			    		
			    		e= new Totem(x, y, Entity.getDirection(direction), Shoot.getShootType(shootType));
		    		}
		    		
		    	}
		    	
		    	if(e != null){
		    		enemies.add(e);
		    	}
			}
		}
		return enemies;
	}
	
	
	public static boolean isAWaterTile(int tilePosX, int tilePosY, TiledMap tiledMap){
		TiledMapTileLayer layer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_COLLISION);
		Cell cell= layer.getCell(tilePosX, tilePosY);
    	if(cell == null) return false;
    	TiledMapTile tile= cell.getTile();
    	if(tile == null) return false;
    	
		if(tile.getProperties().containsKey("water")) return true;
		
		return false;
	}
	
	/**
	 * Changes the tile for an "empty tile". This works with SCENE 3 LAYER.
	 * @param tilePosX
	 * @param tilePosY
	 */
	public static void putBlankTile(int tilePosX, int tilePosY, TiledMap tiledMap){
		TiledMapTileLayer layer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_3);
		Cell cell= layer.getCell(tilePosX, tilePosY);
    	if(cell == null) return;
    	
    	cell.setTile(null);
	}
	
	/**
	 * Returns true if the tile is able to be digged.
	 * @param tilePosX
	 * @param tilePosY
	 * @return
	 */
	public static boolean isAbleToDig(TiledMap tiledMap, int tilePosX, int tilePosY){
		//we check if in the layer there is already the tile "104" (desert tileset) wich is what we use for "removed sand"
		TiledMapTileLayer layer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_1);
		if(layer.getCell(tilePosX, tilePosY).getTile().getId()==104) return false;
		
		return true;
	}
	
	/**
	 * Changes the tile for another to create "removed sand" effect.
	 * @param tiledMap
	 * @param tileX
	 * @param tileY
	 */
	public static void removeSand(TiledMap tiledMap, int tileX, int tileY){
		TiledMapTileSet tileset =  tiledMap.getTileSets().getTileSet(TILESET_DESERT);
		TiledMapTileLayer layer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_1);
		//On the desert tileset we use tile id 104 (on TILED is id 103)
		layer.getCell(tileX, tileY).setTile(tileset.getTile(104));
	}
	
	/**
	 * Returns near "blocks" to the entity. The "blocks" means that the entity cant pass thru them, and are returned as "Rectangles".
	 * @param entity
	 * @param tiledMap
	 * @return
	 */
	public static ArrayList<Rectangle> getCloseBlocks(GameObject gameobj, TiledMap tiledMap){
		return getCloseCells(gameobj, tiledMap, "block");
	}
	
	/**
	 * Returns near "water" to the entity.
	 * @param entity
	 * @param tiledMap
	 * @return
	 */
	public static ArrayList<Rectangle> getCloseWater(Entity entity, TiledMap tiledMap){
		return getCloseCells(entity, tiledMap, "water");
	}
	
	public static ArrayList<Rectangle> getCloseCells(GameObject gameobj, TiledMap tiledMap, String prop){
		TiledMapTileLayer collisionLayer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_COLLISION);
		ArrayList<Rectangle> cellsCloseToEntity= new ArrayList<Rectangle>();
		int entityTileX= gameobj.getTilePosition().x;
		int entityTileY= gameobj.getTilePosition().y;
		for(int i= 0; i < collisionLayer.getWidth(); i++){
			for(int ii= 0; ii < collisionLayer.getHeight(); ii++){
				Cell cell= collisionLayer.getCell(i, ii);
				if(cell == null) continue;
				
				int distanceX= Math.abs(i - entityTileX);
				int distanceY= Math.abs(ii - entityTileY);
				
				if(distanceX<=1 && distanceY<=1) {
					TiledMapTile tile= cell.getTile();
					if(tile == null) continue;
					
					if(tile.getProperties().containsKey(prop)){
						//all final values in pixels
						float x= i*32;
						float y= ii*32;
						float width= 32;
						float height= 32;
						Rectangle rectTile= new Rectangle(x, y, width, height);
						cellsCloseToEntity.add(rectTile);
					}
				}
			}
		}
		return cellsCloseToEntity;
	}
	/**
	 * Adds to the tiledMap the proper animated tiles that TILED do not support natively.
	 * @param tiledMap
	 */
	public static void loadAnimatedTiles(TiledMap tiledMap){
		//search for animated tiles (just testAnim for the moment)
		TiledMapTileSet tileset =  tiledMap.getTileSets().getTileSet(0);
		ArrayList<TiledMapTile> animatedTiles_testAnim= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_animwater= new ArrayList<TiledMapTile>();

		for(TiledMapTile tile:tileset){
			if(tile.getProperties().containsKey("testAnim")){
				animatedTiles_testAnim.add(tile);
			}
			if(tile.getProperties().containsKey("animwater")){
				animatedTiles_animwater.add(tile);
			}
		}

		//now we create the animated tiles
		//-testAnim
		Array<StaticTiledMapTile> frames_testAnim= new Array<StaticTiledMapTile>(3);
		for(int i= 0; i < animatedTiles_testAnim.size(); i++){
			//¿no hay q cheackear el frame index? al parecer sale bien de una
			frames_testAnim.add((StaticTiledMapTile)(animatedTiles_testAnim.get(i)));
		}
		AnimatedTiledMapTile animatedTile_testAnim= new AnimatedTiledMapTile(1f, frames_testAnim);
		//-animwater
		Array<StaticTiledMapTile> frames_animwater= new Array<StaticTiledMapTile>(3);
		for(int i= 0; i < animatedTiles_animwater.size(); i++){
			//¿no hay q cheackear el frame index? al parecer sale bien de una
			frames_animwater.add((StaticTiledMapTile)(animatedTiles_animwater.get(i)));
		}
		AnimatedTiledMapTile animatedTile_animwater= new AnimatedTiledMapTile(1f, frames_animwater);
		
		//we get the layer to put the animated tile where must be (could be a lot of places)
		TiledMapTileLayer esceneOneLayer = (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_1);
		for(int x = 0; x < esceneOneLayer.getWidth();x++){
            for(int y = 0; y < esceneOneLayer.getHeight();y++){
            	Cell cell= esceneOneLayer.getCell(x, y);
            	if(cell == null) continue;
            	TiledMapTile tile= cell.getTile();
            	if(tile == null) continue;
            	
            	if(tile.getProperties().containsKey("testAnim")){
            		cell.setTile(animatedTile_testAnim);
            	}
            	if(tile.getProperties().containsKey("animwater")){
            		cell.setTile(animatedTile_animwater);
            	}
            }
		}
		
	}//end load anim tiles
	
	/**
	 * Returns Items of the map (this creates entire object for every "item tile" on the map).
	 * An "item tile" has the following properties:
	 * 
	 * item: value/ammo/hp/life/clue
	 * value: int
	 * hidden: boolean 
	 * [cant: int (only for ammo and hp)] or [msg: string (only for clue)]
	 * 
	 * @param tiledMap
	 * @return
	 */
	public static ArrayList<Item> getItems(TiledMap tiledMap){
		ArrayList<Item> items= new ArrayList<Item>();
		TiledMapTileLayer itemsLayer = (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_ITEMS);
		for(int x = 0; x < itemsLayer.getWidth();x++){
            for(int y = 0; y < itemsLayer.getHeight();y++){
            	Cell cell= itemsLayer.getCell(x, y);
            	if(cell == null) continue;
            	TiledMapTile tile= cell.getTile();
            	if(tile == null) continue;
            	
            	if(tile.getProperties().containsKey("item")){
            		Item item= null;
            		String type= tile.getProperties().get("item").toString();
            		int value= Integer.parseInt(tile.getProperties().get("value").toString());
            		boolean hidden= false;
            		if(tile.getProperties().get("hidden").toString().compareToIgnoreCase("true")==0) hidden= true;
            		
            		if(type.compareToIgnoreCase("value")==0){
            			item= new Item(x, y);
            			item.setValue(value);
            			if(hidden) item.setCurrentState(Item.States.HIDDEN);
            			else item.setCurrentState(Item.States.DISCOVERED);
            		}
            		
            		if(type.compareToIgnoreCase("ammo")==0){
            			int cant= Integer.parseInt(tile.getProperties().get("cant").toString());
            			
            			item= new AmmoItem(x, y);
            			item.setValue(value);
            			if(hidden) item.setCurrentState(Item.States.HIDDEN);
            			else item.setCurrentState(Item.States.DISCOVERED);
            			
            			((AmmoItem)item).setCantAmmo(cant);
            		}
            		
            		if(type.compareToIgnoreCase("hp")==0){
            			int cant= Integer.parseInt(tile.getProperties().get("cant").toString());
            			
            			item= new HealthItem(x, y);
            			item.setValue(value);
            			if(hidden) item.setCurrentState(Item.States.HIDDEN);
            			else item.setCurrentState(Item.States.DISCOVERED);
            			
            			((HealthItem)item).setCantHealthPoints(cant);
            		}
            		
            		if(item != null) items.add(item);
            	}//end if contains key "item"
            }
		}
		return items;
	}
	
	public static ArrayList<DestructibleBlock> getDestructibleBlocks(TiledMap tiledMap){
		ArrayList<DestructibleBlock> blocks= new ArrayList<DestructibleBlock>();
		TiledMapTileLayer collisionLayer = (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_COLLISION);
		for(int x = 0; x < collisionLayer.getWidth();x++){
            for(int y = 0; y < collisionLayer.getHeight();y++){
            	Cell cell= collisionLayer.getCell(x, y);
            	if(cell == null) continue;
            	TiledMapTile tile= cell.getTile();
            	if(tile == null) continue;
            	
            	if(tile.getProperties().containsKey("fakeBlock")){
            		blocks.add(new DestructibleBlock(x, y));
            	}
            }
		}
		return blocks;
	}
}//end class
