package com.tomgames.pit.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.CollisionSystem;
import com.tomgames.pit.Message;
import com.tomgames.pit.PIT;
import com.tomgames.pit.Rumble;
import com.tomgames.pit.Settings;
import com.tomgames.pit.TiledMapUtilities;
import com.tomgames.pit.entities.shoots.Shoot;

public class Player extends Entity{
	
	private int lifes= 3;
	private int score;
	private final int SCORE_FOR_LIFE= 50000;
	private int score_for_life_count;
	
	private float cadenceTime= 0.125f;
	private float elapsedCadenceTime;
	
	private float diggingTime= 1f;
	private float elapsedDiggingTime;
	
	private float deadTime= 3.5f;
	private float elapsedDeadTime;
	private float elapsedDeadTime_forAnim;
	
	private Message talk;
	
	public Player(float posX, float posY) {
		super(posX, posY);
		
		setRunSpeed(175);
		talk= new Message();
		setCurrentKeyFrame(Assets.textures.pirateS);
		
		//dead "anim"
		Array<TextureRegion> frames_dead= new Array<TextureRegion>();
		frames_dead.add(Assets.textures.dead);
		Animation deadAnim= new Animation(1f, frames_dead, Animation.PlayMode.LOOP);
		setStateAnimations(States.DEAD, deadAnim);
		
		//anim N
		Array<TextureRegion> frames_dirN= new Array<TextureRegion>();
		frames_dirN.add(Assets.textures.pirateN);
		Animation dirN= new Animation(1f, frames_dirN, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.N, dirN);
		//anim S
		Array<TextureRegion> frames_dirS= new Array<TextureRegion>();
		frames_dirS.add(Assets.textures.pirateS);
		Animation dirS= new Animation(1f, frames_dirS, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.S, dirS);
		//anim E
		Array<TextureRegion> frames_dirE= new Array<TextureRegion>();
		frames_dirE.add(Assets.textures.pirateE);
		Animation dirE= new Animation(1f, frames_dirE, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.E, dirE);
		//anim W
		Array<TextureRegion> frames_dirW= new Array<TextureRegion>();
		frames_dirW.add(Assets.textures.pirateW);
		Animation dirW= new Animation(1f, frames_dirW, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.W, dirW);
		//anim SW
		Array<TextureRegion> frames_dirSW= new Array<TextureRegion>();
		frames_dirSW.add(Assets.textures.pirateSW);
		Animation dirSW= new Animation(1f, frames_dirSW, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.SW, dirSW);
		//anim SE
		Array<TextureRegion> frames_dirSE= new Array<TextureRegion>();
		frames_dirSE.add(Assets.textures.pirateSE);
		Animation dirSE= new Animation(1f, frames_dirSE, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.SE, dirSE);
		//anim NW
		Array<TextureRegion> frames_dirNW= new Array<TextureRegion>();
		frames_dirNW.add(Assets.textures.pirateNW);
		Animation dirNW= new Animation(1f, frames_dirNW, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.NW, dirNW);
		//anim NE
		Array<TextureRegion> frames_dirNE= new Array<TextureRegion>();
		frames_dirNE.add(Assets.textures.pirateNE);
		Animation dirNE= new Animation(1f, frames_dirNE, Animation.PlayMode.LOOP);
		setDirectionAnimations(Directions.NE, dirNE);
		
		//creamos asi nomas una diggin animation
		Array<TextureRegion> frames_digginAnim= new Array<TextureRegion>();
		frames_digginAnim.add(Assets.textures.pirateSE);
		frames_digginAnim.add(Assets.textures.pirateSW);
		Animation digginAnim= new Animation(0.2f, frames_digginAnim, Animation.PlayMode.LOOP);
		setDigginAnimation(digginAnim);
		
	}//end constructor

	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapeRender) {
		
		batch.draw(getCurrentKeyFrame(), (int)getPosition().x, (int)getPosition().y);
		
		if(isMeleeAttackOnCourse()) renderSword(batch);
		
		if(getCurrentState() == States.DEAD){
			if(this.lifes==0){
				if(elapsedDeadTime_forAnim < deadTime) batch.draw(Assets.textures.angel, (int)getPosition().x, (int)getPosition().y + (elapsedDeadTime_forAnim*150));
			}
			else batch.draw(Assets.textures.angel, (int)getPosition().x, (int)getPosition().y + (elapsedDeadTime_forAnim*50));
		}
		/*
		//TODO: test code (it renders contact points)
		batch.end();
		shapeRender.begin(ShapeType.Line);
		shapeRender.rect(getZone().x, getZone().y, getZone().width, getZone().height);
		shapeRender.end();
		shapeRender.begin(ShapeType.Filled);
		for(int i= 0; i < 4; i++) shapeRender.circle(getContactPoints()[i].x, getContactPoints()[i].y, 2);
		shapeRender.end();
		batch.begin();
		*/
	}

	protected void renderSword(SpriteBatch batch){
		Vector2 v= Entity.getOffsets(getMeleeAttackCurrentDirection(), 32);
		batch.draw(Assets.textures.sword, getPosition().x + v.x, getPosition().y + v.y);
	}
	
	public void renderTalk(SpriteBatch batch){
		talk.render(batch, getPosition().x - 5, getPosition().y + getZone().height*2);
	}
	
	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		//Assets.fonts.defaultFont.draw(batchGUI, "State: " + getCurrentState(), 10, Gdx.graphics.getHeight() - 40);
		batchGUI.draw(Assets.textures.score_gui, -90, Gdx.graphics.getHeight() - 120);
		Assets.fonts.uiFont.draw(batchGUI, getScore() + "", 40, Gdx.graphics.getHeight() - 90);
		
		
		batchGUI.draw(Assets.textures.playerStats_gui, 300, -60);
		Assets.fonts.uiFont.draw(batchGUI, getLifePoints()+"", 375, 30);
		Assets.fonts.uiFont.draw(batchGUI, "x "+getAmmo(), 455, 30);
		Assets.fonts.uiFont.draw(batchGUI, "x "+lifes, 540, 30);
		//Assets.fonts.defaultFont.draw(batchGUI, "Current Action: " + getCurrentAction(), 10, Gdx.graphics.getHeight() - 60);
		//Assets.fonts.defaultFont.draw(batchGUI, "Attack Mode: " + getCurrentAttackMode(), 250, Gdx.graphics.getHeight() - 100);
		//Assets.fonts.defaultFont.draw(batchGUI, "Direction: " + getCurrentDirection(), 10, Gdx.graphics.getHeight() - 80);
		//Assets.fonts.defaultFont.draw(batchGUI, "Speed: ("+getVelocity().x+" / "+getVelocity().y+")", 250, Gdx.graphics.getHeight() - 80);
		//Assets.fonts.defaultFont.draw(batchGUI, "Position: ("+getPosition().x+" / "+getPosition().y+")", 10, Gdx.graphics.getHeight() - 100);
		//Assets.fonts.defaultFont.draw(batchGUI, "Tile Position: ("+getTilePosition().x+" / "+getTilePosition().y+")", 10, Gdx.graphics.getHeight() - 120);
	}

	@Override
	public void update(float delta) {	
		updateCurrentKeyFrameAnimation(delta);
		
		//update talk
		talk.update(delta);
				
		//nothing to do if the player is dead :P
		if(getCurrentState()==States.DEAD){
			if(this.lifes == 0){
				elapsedDeadTime_forAnim+= delta;
				return;
			}else{
				elapsedDeadTime+= delta;
				if(elapsedDeadTime <= deadTime/2) elapsedDeadTime_forAnim+= delta;
				else elapsedDeadTime_forAnim-= delta;
				
				if(elapsedDeadTime > deadTime){
					elapsedDeadTime= 0;
					elapsedDeadTime_forAnim= 0;
					this.lifes--;
					setCurrentState(States.ALIVE);
					setLifePoints(100);
					if(getAmmo() < 15) setAmmo(15);
				}else{
					return;
				}
			}
		}
		
		//we check current tile to see if its water or what
		if(PIT.instance.gameplay.getCurrentIsland().isAWaterTile(getTilePosition().x, getTilePosition().y)){
			if(isRaftAvailable()){
				setCurrentAction(Actions.RAFTING);
			}
		}else{
			//player is NOT on water
			if(getCurrentAction() != Actions.DIGGING && getCurrentAction() == Actions.RAFTING){
				jumpOffRaft();
				setCurrentAction(Actions.EXPLORING);
			}
		}
		
		switch(getCurrentAction()){
		case DIGGING: updateDigging(delta); break;
		case EXPLORING: updateExploring(delta); break;
		case RAFTING: updateRafting(delta); break;
		}
		
	}//end update
	
	private void updateDigging(float delta){
		elapsedDiggingTime+= delta;
		if(elapsedDiggingTime > diggingTime){
			//dig ended. time to see if there is something...
			PIT.instance.gameplay.getCurrentIsland().digFinished(getTilePosition().x, getTilePosition().y);
			setCurrentAction(Actions.EXPLORING);
		}
	}
	
	private void updateExploring(float delta){
		//weapon switch
		if(Gdx.input.isKeyJustPressed(Keys.E)){
			if(getCurrentAttackMode() == AttackMode.MELEE) setCurrentAttackMode(AttackMode.RANGED);
			else setCurrentAttackMode(AttackMode.MELEE);
		}
		
		updateMovement(delta);
		
		//update attack
		if(isMeleeAttackOnCourse()) {
			updateMeleeAttack(delta);
			//check for hits on destructible blocks AND enemies (bad pirates only, for the moment)
			CollisionSystem.checkForHit(getSwordArea(), PIT.instance.gameplay.getCurrentIsland().getDestructibleBlocks());
		}
		updateAttack(delta);
		
	}

	private void updateRafting(float delta){
		updateMovement(delta);
		//copy position to the raft
		getRaft().setPosition(getPosition().x, getPosition().y);
	}
	
	private void updateMovement(float delta){
		//damping
		applyDamping(delta);

		//input
		boolean pressA=false, pressD=false, pressS=false, pressW=false;
		if(Gdx.input.isKeyPressed(Keys.A)) {
			pressA= true;
			setVelX(-getRunSpeed());
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			pressD= true;
			setVelX(getRunSpeed());
		}
		if(Gdx.input.isKeyPressed(Keys.W)) {
			pressW= true;
			setVelY(getRunSpeed());
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			pressS= true;
			setVelY(-getRunSpeed());
		}

		//calculating new position
		float newX= getPosition().x + (getVelocity().x * delta);
		float newY= getPosition().y + (getVelocity().y * delta);
		setPosition(newX, newY);

		//updating direction
		if(pressD && pressW) setCurrentDirection(Directions.NE);
		else if(pressA && pressW) setCurrentDirection(Directions.NW);
		else if(pressD && pressS) setCurrentDirection(Directions.SE);
		else if(pressA && pressS) setCurrentDirection(Directions.SW);
		else if(pressW) setCurrentDirection(Directions.N);
		else if(pressS) setCurrentDirection(Directions.S);
		else if(pressA) setCurrentDirection(Directions.W);
		else if(pressD) setCurrentDirection(Directions.E);
	}

	private void updateAttack(float delta){
		elapsedCadenceTime+= delta;
		
		//check for the direction of the attack (if any)
		boolean pressLeft=false, pressRight=false, pressDown=false, pressUp=false;
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)) pressLeft= true;
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) pressRight= true;
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)) pressDown= true;
		if(Gdx.input.isKeyJustPressed(Keys.UP)) pressUp= true;
		
		Entity.Directions dir= null;
		if(pressRight && pressUp) dir= Directions.NE;
		else if(pressLeft && pressUp) dir= Directions.NW;
		else if(pressRight && pressDown) dir= Directions.SE;
		else if(pressLeft && pressDown) dir= Directions.SW;
		else if(pressUp) dir= Directions.N;
		else if(pressDown) dir= Directions.S;
		else if(pressLeft) dir= Directions.W;
		else if(pressRight) dir= Directions.E;
		
		//if there is a direction, then the player attacked
		if(dir != null){
			if(getCurrentAttackMode() == AttackMode.MELEE){
				if(!isMeleeAttackOnCourse()){
					setMeleeAttackOnCourse(true);
					setMeleeAttackCurrentDirection(dir);
				}
			}
			if(getCurrentAttackMode() == AttackMode.RANGED){
				if(elapsedCadenceTime > cadenceTime){
					elapsedCadenceTime= 0;
					//player shoots (if there is enought ammo)
					if(getAmmo() > 0){
						subAmmo();
						Shoot s= PIT.instance.gameplay.shootSystem.getPool().get();
						s.setType(Shoot.Type.PISTOL_BALL);
						Vector2 v= Entity.getOffsets(dir, 16);
						s.setInitialPosition(getPosition().x + v.x, getPosition().y + v.y);
						s.setDirection(dir);
						PIT.instance.gameplay.shootSystem.addGoodShoot(s);
						if(Settings.sounds) Assets.audio.shoot.play();
					}else{
						talk.show("No ammo!", 2.7f);
					}
				}
				
			}
		}
	}
	
	public void jumpToRaft(Raft raft){
		if(!isRaftAvailable()){
			setRaft(raft);
			this.setPosition(raft.getPosition().x, raft.getPosition().y);
			setCurrentAction(Actions.RAFTING);
			talk.show("Ai!Ai!", 0.5f);
		}
	}
	
	public void jumpOffRaft(){
		if(isRaftAvailable()){
			getRaft().moveToLastTilePosition();
			setRaft(null);
			this.setPosition(getTilePosition().x * 32, getTilePosition().y * 32);
			talk.show("Glup!", 0.5f);
		}
	}
	
	public void dig(){
		if(getCurrentAction() != Actions.DIGGING){
			//first we ask if we are able to dig on the current location
			if(PIT.instance.gameplay.getCurrentIsland().isAbleToDig(getTilePosition().x, getTilePosition().y)){
				setCurrentAction(Actions.DIGGING);
				elapsedDiggingTime= 0;
				//move the player "to the tile"
				setPosition((getTilePosition().x*32), (getTilePosition().y*32));
				//talk
				talk.show("Dig dig dig...I love dig!", 1f);
			}else{
				talk.show("Cant dig here!", 1f);
			}
		}
	}
	
	
	@Override
	public void applyDamage(int amount) {
		super.applyDamage(amount);
		//screen shake! yeey!
		PIT.instance.gameplay.getCamera().rumble(5f, 0.3f);
		//pirate talks!
		talk.show("Arghh!", 0.5f);
		
		if(Settings.sounds) Assets.audio.shootHit2.play();
	}

	public int getScore() {
		return score;
	}

	//public void setScore(int score) {
	//	this.score = score;
	//}

	public void addScore(int amount){
		this.score+= amount;
		this.score_for_life_count+= amount;
		if(this.score_for_life_count >= SCORE_FOR_LIFE){
			this.score_for_life_count= this.score_for_life_count - SCORE_FOR_LIFE;
			this.lifes++;
		}
	}

	@Override
	public void setCurrentState(States currentState) {
		super.setCurrentState(currentState);
		
		if(currentState == States.DEAD){
			//Start revive count (if there is lives available)
			elapsedDeadTime= 0;
			elapsedDeadTime_forAnim= 0;
		}
	}
	
	
	
}//end class
