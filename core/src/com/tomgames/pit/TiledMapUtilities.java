package com.tomgames.pit;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.BadPirate;
import com.tomgames.pit.entities.Enemy;
import com.tomgames.pit.entities.Entity;
import com.tomgames.pit.entities.GameObject;
import com.tomgames.pit.entities.Totem;
import com.tomgames.pit.entities.items.AmmoItem;
import com.tomgames.pit.entities.items.BigTreasure;
import com.tomgames.pit.entities.items.ClueItem;
import com.tomgames.pit.entities.items.HealthItem;
import com.tomgames.pit.entities.items.Item;
import com.tomgames.pit.entities.items.MapItem;
import com.tomgames.pit.entities.items.ValueItem;
import com.tomgames.pit.entities.shoots.Shoot;

/**
 * This class encapsules all related to tiled maps that TILED "do not support" natively.
 * @author F. Marrone
 *
 */
public class TiledMapUtilities {
	
	public static final int TILESET_DESERT= 0;
	
	public static final int LAYER_ENEMIES= 6;
	public static final int LAYER_ITEMS= 5;
	public static final int LAYER_SCENE_3a= 4;
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
		    		
		    		if(enemyType.compareToIgnoreCase("badpirate")==0){
		    			e= new BadPirate(x, y);
		    		}
		    	}
		    	
		    	if(e != null){
		    		enemies.add(e);
		    	}
			}
		}
		return enemies;
	}
	
	public static ArrayList<GridPoint2> getDigZones(TiledMap tiledMap){
		ArrayList<GridPoint2> tiles= new ArrayList<GridPoint2>();
		TiledMapTileLayer layer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_COLLISION);
		for(int x= 0; x < layer.getWidth(); x++){
			for(int y= 0; y < layer.getHeight(); y++){
				Cell cell= layer.getCell(x, y);
		    	if(cell == null) continue;
		    	TiledMapTile tile= cell.getTile();
		    	if(tile == null) continue;
		    	
		    	if(tile.getProperties().containsKey("digZone")){
		    		tiles.add(new GridPoint2(x,y));
		    	}
			}
		}
		return tiles;
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
		TiledMapTileLayer layer3= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_3);
		Cell cell= layer3.getCell(tilePosX, tilePosY);
    	if(cell != null) cell.setTile(null);
    	
    	//TiledMapTileLayer layer3a= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_3a);
		//cell= layer3a.getCell(tilePosX, tilePosY);
    	//if(cell != null) cell.setTile(null);
    	
	}
	
	/**
	 * Returns true if the tile is able to be digged.
	 * @param tilePosX
	 * @param tilePosY
	 * @return
	 */
	//public static boolean isAbleToDig(TiledMap tiledMap, int tilePosX, int tilePosY){
		//we check if in the layer there is already the tile "104" (desert tileset) wich is what we use for "removed sand"
	//	TiledMapTileLayer layer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_1);
	//	if(layer.getCell(tilePosX, tilePosY).getTile().getId()==104) return false;
		
	//	return true;
	//}
	
	/**
	 * Changes the tile for another to create "removed sand" effect.
	 * @param tiledMap
	 * @param tileX
	 * @param tileY
	 */
	public static void removeSand(TiledMap tiledMap, int tileX, int tileY){
		TiledMapTileSet tileset =  tiledMap.getTileSets().getTileSet(TILESET_DESERT);
		TiledMapTileLayer layer= (TiledMapTileLayer)tiledMap.getLayers().get(LAYER_SCENE_1);
		//On the desert tileset we use tile id 364 (on TILED is id 363)
		layer.getCell(tileX, tileY).setTile(tileset.getTile(364));
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
		ArrayList<TiledMapTile> animatedTiles_olaAnimE= new ArrayList<TiledMapTile>();
		
		ArrayList<TiledMapTile> animatedTiles_waterCercaOrillaN= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_waterCercaOrillaS= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_waterCercaOrillaE= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_waterCercaOrillaW= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_middleWaterAnimA= new ArrayList<TiledMapTile>();	//NW
		ArrayList<TiledMapTile> animatedTiles_middleWaterAnimB= new ArrayList<TiledMapTile>();	//NE
		ArrayList<TiledMapTile> animatedTiles_middleWaterAnimC= new ArrayList<TiledMapTile>();	//SW
		ArrayList<TiledMapTile> animatedTiles_middleWaterAnimD= new ArrayList<TiledMapTile>();	//SE
		
		ArrayList<TiledMapTile> animatedTiles_orillaArenaE= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaArenaW= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaArenaN= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaArenaS= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaArenaNE= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaArenaNW= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaArenaSE= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaArenaSW= new ArrayList<TiledMapTile>();
		
		ArrayList<TiledMapTile> animatedTiles_orillaCornerA= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaCornerB= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaCornerC= new ArrayList<TiledMapTile>();
		ArrayList<TiledMapTile> animatedTiles_orillaCornerD= new ArrayList<TiledMapTile>();
		
		for(TiledMapTile tile:tileset){
			if(tile.getProperties().containsKey("testAnim")){
				animatedTiles_testAnim.add(tile);
			}
			if(tile.getProperties().containsKey("animwater")){
				animatedTiles_animwater.add(tile);
			}
			if(tile.getProperties().containsKey("ola_anim_E")){
				animatedTiles_olaAnimE.add(tile);
			}
			
			
			if(tile.getProperties().containsKey("waterCercaOrillaN")){
				animatedTiles_waterCercaOrillaN.add(tile);
			}
			if(tile.getProperties().containsKey("waterCercaOrillaS")){
				animatedTiles_waterCercaOrillaS.add(tile);
			}
			if(tile.getProperties().containsKey("waterCercaOrillaE")){
				animatedTiles_waterCercaOrillaE.add(tile);
			}
			if(tile.getProperties().containsKey("waterCercaOrillaW")){
				animatedTiles_waterCercaOrillaW.add(tile);
			}
			if(tile.getProperties().containsKey("middleWaterAnimA")){
				animatedTiles_middleWaterAnimA.add(tile);
			}
			if(tile.getProperties().containsKey("middleWaterAnimB")){
				animatedTiles_middleWaterAnimB.add(tile);
			}
			if(tile.getProperties().containsKey("middleWaterAnimC")){
				animatedTiles_middleWaterAnimC.add(tile);
			}
			if(tile.getProperties().containsKey("middleWaterAnimD")){
				animatedTiles_middleWaterAnimD.add(tile);
			}
			
			if(tile.getProperties().containsKey("orillaArenaE")){
				animatedTiles_orillaArenaE.add(tile);
			}
			if(tile.getProperties().containsKey("orillaArenaW")){
				animatedTiles_orillaArenaW.add(tile);
			}
			if(tile.getProperties().containsKey("orillaArenaN")){
				animatedTiles_orillaArenaN.add(tile);
			}
			if(tile.getProperties().containsKey("orillaArenaS")){
				animatedTiles_orillaArenaS.add(tile);
			}
			if(tile.getProperties().containsKey("orillaArenaNE")){
				animatedTiles_orillaArenaNE.add(tile);
			}
			if(tile.getProperties().containsKey("orillaArenaNW")){
				animatedTiles_orillaArenaNW.add(tile);
			}
			if(tile.getProperties().containsKey("orillaArenaSE")){
				animatedTiles_orillaArenaSE.add(tile);
			}
			if(tile.getProperties().containsKey("orillaArenaSW")){
				animatedTiles_orillaArenaSW.add(tile);
			}
			
			if(tile.getProperties().containsKey("orillaCornerA")){
				animatedTiles_orillaCornerA.add(tile);
			}
			if(tile.getProperties().containsKey("orillaCornerB")){
				animatedTiles_orillaCornerB.add(tile);
			}
			if(tile.getProperties().containsKey("orillaCornerC")){
				animatedTiles_orillaCornerC.add(tile);
			}
			if(tile.getProperties().containsKey("orillaCornerD")){
				animatedTiles_orillaCornerD.add(tile);
			}
		}

		//now we create the animated tiles
		float waterSpeed= 0.4f;
		
		//-testAnim
		Array<StaticTiledMapTile> frames_testAnim= new Array<StaticTiledMapTile>(3);
		for(int i= 0; i < animatedTiles_testAnim.size(); i++){
			frames_testAnim.add((StaticTiledMapTile)(animatedTiles_testAnim.get(i)));
		}
		AnimatedTiledMapTile animatedTile_testAnim= new AnimatedTiledMapTile(1f, frames_testAnim);
		
		//-animwater
		Array<StaticTiledMapTile> frames_animwater= new Array<StaticTiledMapTile>(2);
		for(int i= 0; i < animatedTiles_animwater.size(); i++){
			frames_animwater.add((StaticTiledMapTile)(animatedTiles_animwater.get(i)));
		}
		AnimatedTiledMapTile animatedTile_animwater= new AnimatedTiledMapTile(1f, frames_animwater);
		
		//-ola_anim_E
		Array<StaticTiledMapTile> frames_olaAnimE= new Array<StaticTiledMapTile>(5);
		for(int i= 0; i < animatedTiles_olaAnimE.size(); i++){
			frames_olaAnimE.add((StaticTiledMapTile)(animatedTiles_olaAnimE.get(i)));
		}
		AnimatedTiledMapTile animatedTile_olaAnimE= new AnimatedTiledMapTile(waterSpeed, frames_olaAnimE);
		
		//-waterCercaOrillaN
		Array<StaticTiledMapTile> frames_waterCercaOrillaN= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_waterCercaOrillaN.size(); i++){
			frames_waterCercaOrillaN.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaN.get(i)));
		}
		for(int i= animatedTiles_waterCercaOrillaN.size()-1; i >= 0; i--){
			frames_waterCercaOrillaN.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaN.get(i)));
		}
		AnimatedTiledMapTile animatedTile_waterCercaOrillaN= new AnimatedTiledMapTile(waterSpeed, frames_waterCercaOrillaN);
		
		//-waterCercaOrillaS
		Array<StaticTiledMapTile> frames_waterCercaOrillaS= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_waterCercaOrillaS.size(); i++){
			frames_waterCercaOrillaS.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaS.get(i)));
		}
		for(int i= animatedTiles_waterCercaOrillaS.size()-1; i >= 0; i--){
			frames_waterCercaOrillaS.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaS.get(i)));
		}
		AnimatedTiledMapTile animatedTile_waterCercaOrillaS= new AnimatedTiledMapTile(waterSpeed, frames_waterCercaOrillaS);
		
		//-waterCercaOrillaE
		Array<StaticTiledMapTile> frames_waterCercaOrillaE= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_waterCercaOrillaE.size(); i++){
			frames_waterCercaOrillaE.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaE.get(i)));
		}
		for(int i= animatedTiles_waterCercaOrillaE.size()-1; i >= 0; i--){
			frames_waterCercaOrillaE.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaE.get(i)));
		}
		AnimatedTiledMapTile animatedTile_waterCercaOrillaE= new AnimatedTiledMapTile(waterSpeed, frames_waterCercaOrillaE);
		
		//-waterCercaOrillaW
		Array<StaticTiledMapTile> frames_waterCercaOrillaW= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_waterCercaOrillaW.size(); i++){
			frames_waterCercaOrillaW.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaW.get(i)));
		}
		for(int i= animatedTiles_waterCercaOrillaW.size()-1; i >= 0; i--){
			frames_waterCercaOrillaW.add((StaticTiledMapTile)(animatedTiles_waterCercaOrillaW.get(i)));
		}
		AnimatedTiledMapTile animatedTile_waterCercaOrillaW= new AnimatedTiledMapTile(waterSpeed, frames_waterCercaOrillaW);
		
		//-middleWaterAnimA
		Array<StaticTiledMapTile> frames_middleWaterAnimA= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_middleWaterAnimA.size(); i++){
			frames_middleWaterAnimA.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimA.get(i)));
		}
		for(int i= animatedTiles_middleWaterAnimA.size()-1; i >= 0; i--){
			frames_middleWaterAnimA.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimA.get(i)));
		}
		AnimatedTiledMapTile animatedTile_middleWaterAnimA= new AnimatedTiledMapTile(waterSpeed, frames_middleWaterAnimA);
		
		//-middleWaterAnimB
		Array<StaticTiledMapTile> frames_middleWaterAnimB= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_middleWaterAnimB.size(); i++){
			frames_middleWaterAnimB.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimB.get(i)));
		}
		for(int i= animatedTiles_middleWaterAnimB.size()-1; i >= 0; i--){
			frames_middleWaterAnimB.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimB.get(i)));
		}
		AnimatedTiledMapTile animatedTile_middleWaterAnimB= new AnimatedTiledMapTile(waterSpeed, frames_middleWaterAnimB);
		
		//-middleWaterAnimC
		Array<StaticTiledMapTile> frames_middleWaterAnimC= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_middleWaterAnimC.size(); i++){
			frames_middleWaterAnimC.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimC.get(i)));
		}
		for(int i= animatedTiles_middleWaterAnimC.size()-1; i >= 0; i--){
			frames_middleWaterAnimC.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimC.get(i)));
		}
		AnimatedTiledMapTile animatedTile_middleWaterAnimC= new AnimatedTiledMapTile(waterSpeed, frames_middleWaterAnimC);
		
		//-middleWaterAnimD
		Array<StaticTiledMapTile> frames_middleWaterAnimD= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_middleWaterAnimD.size(); i++){
			frames_middleWaterAnimD.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimD.get(i)));
		}
		for(int i= animatedTiles_middleWaterAnimD.size()-1; i >= 0; i--){
			frames_middleWaterAnimD.add((StaticTiledMapTile)(animatedTiles_middleWaterAnimD.get(i)));
		}
		AnimatedTiledMapTile animatedTile_middleWaterAnimD= new AnimatedTiledMapTile(waterSpeed, frames_middleWaterAnimD);
		
		//-orillaArenaE
		Array<StaticTiledMapTile> frames_orillaArenaE= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaE.size(); i++) {
			frames_orillaArenaE.add((StaticTiledMapTile)(animatedTiles_orillaArenaE.get(i)));
		}
		for(int i= animatedTiles_orillaArenaE.size()-1; i >= 0; i--) {
			frames_orillaArenaE.add((StaticTiledMapTile)(animatedTiles_orillaArenaE.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaE= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaE);
		
		//-orillaArenaW
		Array<StaticTiledMapTile> frames_orillaArenaW= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaW.size(); i++) {
			frames_orillaArenaW.add((StaticTiledMapTile)(animatedTiles_orillaArenaW.get(i)));
		}
		for(int i= animatedTiles_orillaArenaW.size()-1; i >= 0; i--) {
			frames_orillaArenaW.add((StaticTiledMapTile)(animatedTiles_orillaArenaW.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaW= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaW);
		
		//-orillaArenaN
		Array<StaticTiledMapTile> frames_orillaArenaN= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaN.size(); i++) {
			frames_orillaArenaN.add((StaticTiledMapTile)(animatedTiles_orillaArenaN.get(i)));
		}
		for(int i= animatedTiles_orillaArenaN.size()-1; i >= 0; i--) {
			frames_orillaArenaN.add((StaticTiledMapTile)(animatedTiles_orillaArenaN.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaN= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaN);
		
		//-orillaArenaS
		Array<StaticTiledMapTile> frames_orillaArenaS= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaS.size(); i++) {
			frames_orillaArenaS.add((StaticTiledMapTile)(animatedTiles_orillaArenaS.get(i)));
		}
		for(int i= animatedTiles_orillaArenaS.size()-1; i >= 0; i--) {
			frames_orillaArenaS.add((StaticTiledMapTile)(animatedTiles_orillaArenaS.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaS= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaS);
				
		//-orillaArenaNE
		Array<StaticTiledMapTile> frames_orillaArenaNE= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaNE.size(); i++) {
			frames_orillaArenaNE.add((StaticTiledMapTile)(animatedTiles_orillaArenaNE.get(i)));
		}
		for(int i= animatedTiles_orillaArenaNE.size()-1; i >= 0; i--) {
			frames_orillaArenaNE.add((StaticTiledMapTile)(animatedTiles_orillaArenaNE.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaNE= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaNE);
		
		//-orillaArenaSE
		Array<StaticTiledMapTile> frames_orillaArenaSE= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaSE.size(); i++) {
			frames_orillaArenaSE.add((StaticTiledMapTile)(animatedTiles_orillaArenaSE.get(i)));
		}
		for(int i= animatedTiles_orillaArenaSE.size()-1; i >= 0; i--) {
			frames_orillaArenaSE.add((StaticTiledMapTile)(animatedTiles_orillaArenaSE.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaSE= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaSE);
		
		//-orillaArenaNW
		Array<StaticTiledMapTile> frames_orillaArenaNW= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaNW.size(); i++) {
			frames_orillaArenaNW.add((StaticTiledMapTile)(animatedTiles_orillaArenaNW.get(i)));
		}
		for(int i= animatedTiles_orillaArenaNW.size()-1; i >= 0; i--) {
			frames_orillaArenaNW.add((StaticTiledMapTile)(animatedTiles_orillaArenaNW.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaNW= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaNW);
		
		//-orillaArenaSW
		Array<StaticTiledMapTile> frames_orillaArenaSW= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaArenaSW.size(); i++) {
			frames_orillaArenaSW.add((StaticTiledMapTile)(animatedTiles_orillaArenaSW.get(i)));
		}
		for(int i= animatedTiles_orillaArenaSW.size()-1; i >= 0; i--) {
			frames_orillaArenaSW.add((StaticTiledMapTile)(animatedTiles_orillaArenaSW.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaArenaSW= new AnimatedTiledMapTile(waterSpeed, frames_orillaArenaSW);
			
		//-orillaCornerA
		Array<StaticTiledMapTile> frames_orillaCornerA= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaCornerA.size(); i++) {
			frames_orillaCornerA.add((StaticTiledMapTile)(animatedTiles_orillaCornerA.get(i)));
		}
		for(int i= animatedTiles_orillaCornerA.size()-1; i >= 0; i--) {
			frames_orillaCornerA.add((StaticTiledMapTile)(animatedTiles_orillaCornerA.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaCornerA= new AnimatedTiledMapTile(waterSpeed, frames_orillaCornerA);
		
		//-orillaCornerB
		Array<StaticTiledMapTile> frames_orillaCornerB= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaCornerB.size(); i++) {
			frames_orillaCornerB.add((StaticTiledMapTile)(animatedTiles_orillaCornerB.get(i)));
		}
		for(int i= animatedTiles_orillaCornerB.size()-1; i >= 0; i--) {
			frames_orillaCornerB.add((StaticTiledMapTile)(animatedTiles_orillaCornerB.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaCornerB= new AnimatedTiledMapTile(waterSpeed, frames_orillaCornerB);
		
		//-orillaCornerC
		Array<StaticTiledMapTile> frames_orillaCornerC= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaCornerC.size(); i++) {
			frames_orillaCornerC.add((StaticTiledMapTile)(animatedTiles_orillaCornerC.get(i)));
		}
		for(int i= animatedTiles_orillaCornerC.size()-1; i >= 0; i--) {
			frames_orillaCornerC.add((StaticTiledMapTile)(animatedTiles_orillaCornerC.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaCornerC= new AnimatedTiledMapTile(waterSpeed, frames_orillaCornerC);
		
		//-orillaCornerD
		Array<StaticTiledMapTile> frames_orillaCornerD= new Array<StaticTiledMapTile>(10);
		for(int i= 0; i < animatedTiles_orillaCornerD.size(); i++) {
			frames_orillaCornerD.add((StaticTiledMapTile)(animatedTiles_orillaCornerD.get(i)));
		}
		for(int i= animatedTiles_orillaCornerD.size()-1; i >= 0; i--) {
			frames_orillaCornerD.add((StaticTiledMapTile)(animatedTiles_orillaCornerD.get(i)));
		}
		AnimatedTiledMapTile animatedTile_orillaCornerD= new AnimatedTiledMapTile(waterSpeed, frames_orillaCornerD);
		
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
            	if(tile.getProperties().containsKey("ola_anim_E")){
            		cell.setTile(animatedTile_olaAnimE);
            	}
            	
            	if(tile.getProperties().containsKey("waterCercaOrillaN")){
            		cell.setTile(animatedTile_waterCercaOrillaN);
            	}
            	if(tile.getProperties().containsKey("waterCercaOrillaS")){
            		cell.setTile(animatedTile_waterCercaOrillaS);
            	}
            	if(tile.getProperties().containsKey("waterCercaOrillaE")){
            		cell.setTile(animatedTile_waterCercaOrillaE);
            	}
            	if(tile.getProperties().containsKey("waterCercaOrillaW")){
            		cell.setTile(animatedTile_waterCercaOrillaW);
            	}
            	if(tile.getProperties().containsKey("middleWaterAnimA")){
            		cell.setTile(animatedTile_middleWaterAnimA);
            	}
            	if(tile.getProperties().containsKey("middleWaterAnimB")){
            		cell.setTile(animatedTile_middleWaterAnimB);
            	}
            	if(tile.getProperties().containsKey("middleWaterAnimC")){
            		cell.setTile(animatedTile_middleWaterAnimC);
            	}
            	if(tile.getProperties().containsKey("middleWaterAnimD")){
            		cell.setTile(animatedTile_middleWaterAnimD);
            	}
            	
            	if(tile.getProperties().containsKey("orillaArenaE")){
            		cell.setTile(animatedTile_orillaArenaE);
            	}
            	if(tile.getProperties().containsKey("orillaArenaW")){
            		cell.setTile(animatedTile_orillaArenaW);
            	}
            	if(tile.getProperties().containsKey("orillaArenaN")){
            		cell.setTile(animatedTile_orillaArenaN);
            	}
            	if(tile.getProperties().containsKey("orillaArenaS")){
            		cell.setTile(animatedTile_orillaArenaS);
            	}
            	if(tile.getProperties().containsKey("orillaArenaNE")){
            		cell.setTile(animatedTile_orillaArenaNE);
            	}
            	if(tile.getProperties().containsKey("orillaArenaNW")){
            		cell.setTile(animatedTile_orillaArenaNW);
            	}
            	if(tile.getProperties().containsKey("orillaArenaSE")){
            		cell.setTile(animatedTile_orillaArenaSE);
            	}
            	if(tile.getProperties().containsKey("orillaArenaSW")){
            		cell.setTile(animatedTile_orillaArenaSW);
            	}
            	
            	if(tile.getProperties().containsKey("orillaCornerA")){
            		cell.setTile(animatedTile_orillaCornerA);
            	}
            	if(tile.getProperties().containsKey("orillaCornerB")){
            		cell.setTile(animatedTile_orillaCornerB);
            	}
            	if(tile.getProperties().containsKey("orillaCornerC")){
            		cell.setTile(animatedTile_orillaCornerC);
            	}
            	if(tile.getProperties().containsKey("orillaCornerD")){
            		cell.setTile(animatedTile_orillaCornerD);
            	}
            }
		}
		
	}//end load anim tiles
	
	/**
	 * Returns Items of the map (this creates entire object for every "item tile" on the map).
	 * An "item tile" has the following properties:
	 * 
	 * item: string (big/value/ammo/hp/clue/map)
	 * value: int (score to be added)
	 * hidden: boolean 
	 * [cant: int (only for ammo and hp)]]
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
            		
            		if(type.compareToIgnoreCase("map")==0){
            			item= new MapItem(x, y);
            			item.setValue(value);
            			if(hidden) item.setCurrentState(Item.States.HIDDEN);
            			else item.setCurrentState(Item.States.DISCOVERED);
            		}
            		
            		if(type.compareToIgnoreCase("big")==0){
            			item= new BigTreasure(x, y);
            			item.setValue(value);
            			if(hidden) item.setCurrentState(Item.States.HIDDEN);
            			else item.setCurrentState(Item.States.DISCOVERED);
            		}
            		
            		if(type.compareToIgnoreCase("value")==0){
            			item= new ValueItem(x, y);
            			item.setValue(value);
            			if(hidden) item.setCurrentState(Item.States.HIDDEN);
            			else item.setCurrentState(Item.States.DISCOVERED);
            			
            			if(value==100) item.setTexture(Assets.textures.coin);
            			if(value==250) item.setTexture(Assets.textures.coin2);
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
            			
            			if(cant==10) item.setTexture(Assets.textures.health);
            			if(cant==25) item.setTexture(Assets.textures.health3);
            		}
            		
            		if(type.compareToIgnoreCase("clue")==0){
            			item= new ClueItem(x, y);
            			item.setValue(value);
            			if(hidden) item.setCurrentState(Item.States.HIDDEN);
            			else item.setCurrentState(Item.States.DISCOVERED);
            			
            			//the messsage of the bottle will be added later (the island will do it)
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
