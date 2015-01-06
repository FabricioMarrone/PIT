package com.tomgames.pit;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.Player;
import com.tomgames.pit.entities.shoots.Shoot;
import com.tomgames.pit.entities.shoots.ShootPool;

public class ShootSystem {

	private ArrayList<Shoot> badShoots, goodShoots;
	private ShootPool pool;
	
	public ShootSystem(){
		badShoots= new ArrayList<Shoot>();
		goodShoots= new ArrayList<Shoot>();
		pool= new ShootPool();
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shapeRender){
		//bad ones
		for(int i=0; i < badShoots.size(); i++){
			badShoots.get(i).render(batch, shapeRender);
		}
		
		//good ones
		for(int i=0; i < goodShoots.size(); i++){
			goodShoots.get(i).render(batch, shapeRender);
		}
	}
	
	public void renderInfo(SpriteBatch batchGUI){
		int alive=0, dead=0, tot;
		for(int i=0; i < badShoots.size(); i++){
			if(badShoots.get(i).getCurrentState() == Shoot.State.ALIVE) alive++;
			if(badShoots.get(i).getCurrentState() == Shoot.State.DEAD) dead++;
		}
		for(int i=0; i < goodShoots.size(); i++){
			if(goodShoots.get(i).getCurrentState() == Shoot.State.ALIVE) alive++;
			if(goodShoots.get(i).getCurrentState() == Shoot.State.DEAD) dead++;
		}
		tot= alive+dead;
		Assets.fonts.defaultFont.draw(batchGUI, "Shoots: A: "+alive+ " D: "+dead+" Tot:"+tot, 10, 60);
		Assets.fonts.defaultFont.draw(batchGUI, getPool().debug(), 200, 60);
	}
	
	public void update(float delta, Player player, ArrayList<DestructibleBlock> destructibleBlocks){
		//update every shoot
		for(int i=0; i < badShoots.size(); i++){
			badShoots.get(i).update(delta);
		}
		for(int i=0; i < goodShoots.size(); i++){
			goodShoots.get(i).update(delta);
		}
		
		//check for hits with blocks
		for(int i=0; i < badShoots.size(); i++){
			CollisionSystem.checkForShootCollision(badShoots.get(i), destructibleBlocks);
		}
		for(int i=0; i < goodShoots.size(); i++){
			CollisionSystem.checkForShootCollision(goodShoots.get(i), destructibleBlocks);
		}
		
		//check for hits with entities
		for(int i=0; i < badShoots.size(); i++){
			CollisionSystem.checkIfShootHitsPlayer(badShoots.get(i), player);
		}
		//here must be checked hits on enemies
		
		//check for dead shoots to be re-used
		//bad ones
		Iterator<Shoot> it= badShoots.iterator();
		while(it.hasNext()){
			Shoot s= it.next();
			if(s.getCurrentState() == Shoot.State.DEAD){
				s.reset();
				getPool().recycle(s);
				it.remove();
			}
		}
		//good ones
		Iterator<Shoot> it_good= goodShoots.iterator();
		while(it_good.hasNext()){
			Shoot s= it_good.next();
			if(s.getCurrentState() == Shoot.State.DEAD){
				s.reset();
				getPool().recycle(s);
				it_good.remove();
			}
		}
	}
	
	public void addBadShoot(Shoot s){
		badShoots.add(s);
	}
	
	public void removeBadShoot(Shoot s){
		badShoots.remove(s);
	}
	
	public void addGoodShoot(Shoot s){
		goodShoots.add(s);
	}
	
	public void removeGoodShoot(Shoot s){
		goodShoots.remove(s);
	}
	
	public void clearAll(){
		badShoots.clear();
		goodShoots.clear();
	}
	
	public ShootPool getPool(){
		return pool;
	}
}//end class
