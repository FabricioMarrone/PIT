package com.tomgames.pit.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.PIT;
import com.tomgames.pit.entities.shoots.Shoot;

public class Totem extends Enemy{

	private Shoot.Type shootType;
	private float shootInterval= 1.5f;
	private float elapsedTime;
	
	public Totem(int tilePosX, int tilePosY, Entity.Directions shootDir, Shoot.Type shootType) {
		super(tilePosX*32, tilePosY*32);
		this.setCurrentDirection(shootDir);
		this.setShootType(shootType);
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapeRender) {
		batch.draw(Assets.textures.totem, getPosition().x, getPosition().y);
		
	}

	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		elapsedTime+= delta;
		if(elapsedTime > shootInterval){
			elapsedTime= 0;
			shoot(getCurrentDirection());
		}
		
	}

	@Override
	public void shoot(Directions dir) {
		Shoot s= PIT.instance.gameplay.shootSystem.getPool().get();
		s.setType(getShootType());
		Vector2 v= Entity.getOffsets(dir, 16);
		s.setInitialPosition(getPosition().x + v.x, getPosition().y + v.y);
		s.setDirection(dir);
		PIT.instance.gameplay.shootSystem.addBadShoot(s);
	}

	@Override
	public void moveTo(int tileX, int tileY) {
		// TODO Auto-generated method stub
		
	}

	public Shoot.Type getShootType() {
		return shootType;
	}

	public void setShootType(Shoot.Type shootType) {
		this.shootType = shootType;
	}

	@Override
	public void applyDamage(int amount) {
		//No damage. U CANT KILL A TOTEM MUAHAHAH
	}

	

}//end class
