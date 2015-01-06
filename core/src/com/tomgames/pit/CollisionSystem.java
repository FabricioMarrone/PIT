package com.tomgames.pit;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tomgames.pit.entities.Entity;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.shoots.Shoot;

public class CollisionSystem {

	private static ArrayList<Rectangle> cells, cellsForShoots;
	private static TiledMap tiledMap;
	
	public static void render(ShapeRenderer shapeRender){
		//TODO all test code
		if(cells != null){
			shapeRender.begin(ShapeType.Line);
			for(int i= 0; i < cells.size(); i++){
				shapeRender.rect(cells.get(i).x, cells.get(i).y, cells.get(i).width, cells.get(i).height);
			}
			shapeRender.end();
		}
		if(cellsForShoots != null){
			shapeRender.begin(ShapeType.Line);
			for(int i= 0; i < cellsForShoots.size(); i++){
				shapeRender.rect(cellsForShoots.get(i).x, cellsForShoots.get(i).y, cellsForShoots.get(i).width, cellsForShoots.get(i).height);
			}
			shapeRender.end();
		}
	}
	
	/**
	 * Checks whenever an entity collides with a "block". No pass allowed.
	 * @param entity
	 * @param tiledMap
	 */
	public static void checkForEntityCollision(Entity entity, ArrayList<DestructibleBlock> destructibleBloks){
		//getting only close "cells" to entity
		ArrayList<Rectangle> blocksCloseToEntity= TiledMapUtilities.getCloseBlocks(entity, tiledMap);
		ArrayList<Rectangle> waterCloseToEntity= TiledMapUtilities.getCloseWater(entity, tiledMap);
		
		//check collision with "blocks"
		for(int i= 0; i < blocksCloseToEntity.size(); i++){
			Rectangle rectTile= blocksCloseToEntity.get(i);
			
			if(rectTile.overlaps(entity.getZone())){	
				manageCollision(rectTile, entity);
			}
		}
		
		//check collision with "destructible blocks" that are "alive"
		for(int i= 0; i < destructibleBloks.size(); i++){
			if(destructibleBloks.get(i).getLife() > 0){
				if(destructibleBloks.get(i).getZone().overlaps(entity.getZone())){	
					manageCollision(destructibleBloks.get(i).getZone(), entity);
				}
			}
		}
		
		//check collision with "water"
		for(int i= 0; i < waterCloseToEntity.size(); i++){
			Rectangle rectTile= waterCloseToEntity.get(i);
			
			if(rectTile.overlaps(entity.getZone())){
				if(!entity.isRaftAvailable()){
					manageCollision(rectTile, entity);
				}
			}
		}
		
		cells= blocksCloseToEntity;
	}
	
	/**
	 * Checks whenever an alive shoot collides a "normal block" and "destructible ones".
	 * @param shoot
	 */
	public static void checkForShootCollision(Shoot shoot, ArrayList<DestructibleBlock> destructibleBlocks){
		if(shoot.getCurrentState() == Shoot.State.DEAD) return;
		ArrayList<Rectangle> blocksCloseToShoot= TiledMapUtilities.getCloseBlocks(shoot, tiledMap);
		
		for(int i= 0; i < blocksCloseToShoot.size(); i++){
			Rectangle rectTile= blocksCloseToShoot.get(i);
			
			if(rectTile.overlaps(shoot.getZone())){	
				//the shoot hits a "block" (the shoot system will manage the recycle pool)
				shoot.setCurrentState(Shoot.State.DEAD);
			}
		}
		
		cellsForShoots= blocksCloseToShoot;
		if(shoot.getCurrentState() == Shoot.State.DEAD) return;
		
		//now we check destructible blocks
		for(int i=0; i < destructibleBlocks.size(); i++){
			if(destructibleBlocks.get(i).getLife() > 0){
				if(shoot.getZone().overlaps(destructibleBlocks.get(i).getZone())){
					destructibleBlocks.get(i).applyDamage();
					shoot.setCurrentState(Shoot.State.DEAD);
				}
			}
		}
	}
	
	/**
	 * Manages the positive entity collision with a "block".
	 * @param rectTile
	 * @param entity
	 */
	private static void manageCollision(Rectangle rectTile, Entity entity){
		//Bottom point
		if(rectTile.contains(entity.getContactPoints()[0].x, entity.getContactPoints()[0].y)){
			entity.setPosition(entity.getPosition().x, rectTile.y + rectTile.height);
			entity.setVelY(0);
		}
		//rigth point
		if(rectTile.contains(entity.getContactPoints()[1].x, entity.getContactPoints()[1].y)){
			entity.setPosition(rectTile.x - entity.getZone().width, entity.getPosition().y);
			entity.setVelX(0);
		}
		//left point
		if(rectTile.contains(entity.getContactPoints()[2].x, entity.getContactPoints()[2].y)){
			entity.setPosition(rectTile.x + rectTile.width, entity.getPosition().y);
			entity.setVelX(0);
		}
		//top point
		if(rectTile.contains(entity.getContactPoints()[3].x, entity.getContactPoints()[3].y)){
			entity.setPosition(entity.getPosition().x, rectTile.y - entity.getZone().height);
			entity.setVelY(0);
		}
		System.out.println("entro a manage collision");
	}

	/**
	 * Checks if the sword attack collides a destructible (alive) block. In that case, the block its destroyed.
	 * This method also checks if the sword attack impact on any enemy.
	 * @param swordArea
	 * @param destructibleBloks
	 */
	public static void checkForHit(Rectangle swordArea, ArrayList<DestructibleBlock> destructibleBloks){
		//check for hitting an enemy
		//TODO
		
		//check for hitting a destructible block (that are alive)
		for(int i= 0; i < destructibleBloks.size(); i++){
			if(destructibleBloks.get(i).getLife() > 0){
				if(destructibleBloks.get(i).getZone().overlaps(swordArea)){	
					destructibleBloks.get(i).applyMassiveDamage();
				}
			}
		}
	}
	
	/**
	 * Checks if a bad shoot hits player.
	 * @param p
	 */
	public static void checkIfShootHitsPlayer(Shoot shoot, Player p){
		if(shoot.getCurrentState()==Shoot.State.ALIVE){
			if(shoot.getZone().overlaps(p.getZone())){
				if(p.getCurrentState() != Entity.States.DEAD){
					p.applyDamage(15);
					shoot.setCurrentState(Shoot.State.DEAD);
				}
			}
		}
	}
	
	public static void setTiledMap(TiledMap map) {
		tiledMap = map;
	}
	
	
}//end class
