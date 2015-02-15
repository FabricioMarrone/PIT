package com.tomgames.pit.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.Message;
import com.tomgames.pit.PIT;
import com.tomgames.pit.Settings;
import com.tomgames.pit.entities.Entity.States;
import com.tomgames.pit.entities.items.AmmoItem;
import com.tomgames.pit.entities.items.HealthItem;
import com.tomgames.pit.entities.items.Item;
import com.tomgames.pit.entities.items.ValueItem;
import com.tomgames.pit.entities.shoots.Shoot;

public class BadPirate extends Enemy{

	public static int range_attack= 10;
	private float shootInteval= 2f;
	private float elapsedTime;
	private Message talk;
	
	public BadPirate(int tileposX, int tileposY) {
		super(tileposX * 32, tileposY * 32);
		
		setCurrentKeyFrame(Assets.textures.badPirate);
		talk= new Message();
		
		//dead "anim"
		Array<TextureRegion> frames_dead= new Array<TextureRegion>();
		frames_dead.add(Assets.textures.deadbadpirate);
		Animation deadAnim= new Animation(1f, frames_dead, Animation.PlayMode.LOOP);
		setStateAnimations(States.DEAD, deadAnim);
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapeRender) {
		if(getCurrentState() == States.ALIVE) batch.draw(getCurrentKeyFrame(), (int)getPosition().x, (int)getPosition().y);
		else batch.draw(Assets.textures.deadbadpirate, (int)getPosition().x, (int)getPosition().y);
		
		talk.render(batch, getPosition().x - 5, getPosition().y + getZone().height*2);
	}

	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		//updateCurrentKeyFrameAnimation(delta);	//not for the moment
		
		talk.update(delta);
		
		if(getCurrentState()==States.DEAD) return;
		
		//if player is near, shoot him! (but only if he is alive)
		Player p= PIT.instance.getPlayer();
		if(p.getCurrentState() == States.ALIVE){
			elapsedTime+= delta;
			int distX= Math.abs(getTilePosition().x - p.getTilePosition().x);
			int distY= Math.abs(getTilePosition().y - p.getTilePosition().y);
			if(distX <= range_attack && distY <= range_attack){
				if(elapsedTime > shootInteval){
					Directions dir= Entity.getDirection(getTilePosition().x, getTilePosition().y, p.getTilePosition().x, p.getTilePosition().y);
					if(dir!=null){
						elapsedTime= 0;
						this.shoot(dir);
					}
				}
			}
		}
	}
	
	@Override
	public void shoot(Directions dir) {
		Shoot s= PIT.instance.gameplay.shootSystem.getPool().get();
		s.setType(Shoot.Type.PISTOL_BALL);
		Vector2 v= Entity.getOffsets(dir, 16);
		s.setInitialPosition(getPosition().x + v.x, getPosition().y + v.y);
		s.setDirection(dir);
		PIT.instance.gameplay.shootSystem.addBadShoot(s);
		
		talk.show("Take this!", 0.5f);
		if(Settings.sounds) Assets.audio.shoot2.play();
	}

	
	@Override
	public void applyDamage(int amount) {
		super.applyDamage(amount);
		
		talk.show("Ouch!", 0.5f);
		
		if(Settings.sounds) {
			if(PIT.instance.getPlayer().getCurrentAttackMode() == Entity.AttackMode.RANGED){
				Assets.audio.shootHit3.play();
			}
		}
		
		if(getCurrentState()==States.DEAD){
			//Drops an item (not always)
			if(PIT.instance.random.nextFloat() <= 0.85f){
				//35-25-40 ammo-health-gold
				float r= PIT.instance.random.nextFloat();
				Item i= null;
				if(r <= 0.35f){
					i= new AmmoItem(getTilePosition().x, getTilePosition().y);
					i.setValue(25);
					((AmmoItem)i).setCantAmmo(5);
				}else if(r <= 0.6f){
					i= new HealthItem(getTilePosition().x, getTilePosition().y);
					i.setValue(25);
					((HealthItem)i).setCantHealthPoints(10);
				}else{
					i= new ValueItem(getTilePosition().x, getTilePosition().y);
					i.setValue(50);
				}
				PIT.instance.gameplay.getCurrentIsland().addDiscoveredItem(i);
			}
		}
	}

	@Override
	public void moveTo(int tileX, int tileY) {
		// TODO Auto-generated method stub
		
	}

}//end class
