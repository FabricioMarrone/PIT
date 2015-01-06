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
import com.tomgames.pit.PIT;
import com.tomgames.pit.Rumble;
import com.tomgames.pit.TiledMapUtilities;
import com.tomgames.pit.entities.shoots.Shoot;

public class Player extends Entity{
	
	private int score;
	
	private float diggingTime= 1f;
	private float elapsedDiggingTime;
	
	public Player(float posX, float posY) {
		super(posX, posY);
		
		setRunSpeed(175);
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
	
	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		Assets.fonts.defaultFont.draw(batchGUI, "State: " + getCurrentState(), 10, Gdx.graphics.getHeight() - 40);
		Assets.fonts.defaultFont.draw(batchGUI, "Score: " + getScore(), 350, Gdx.graphics.getHeight() - 40);
		Assets.fonts.defaultFont.draw(batchGUI, "HP: " + getLifePoints(), 250, Gdx.graphics.getHeight() - 40);
		Assets.fonts.defaultFont.draw(batchGUI, "Ammo: " + getAmmo(), 250, Gdx.graphics.getHeight() - 60);
		Assets.fonts.defaultFont.draw(batchGUI, "Current Action: " + getCurrentAction(), 10, Gdx.graphics.getHeight() - 60);
		Assets.fonts.defaultFont.draw(batchGUI, "Attack Mode: " + getCurrentAttackMode(), 250, Gdx.graphics.getHeight() - 100);
		Assets.fonts.defaultFont.draw(batchGUI, "Direction: " + getCurrentDirection(), 10, Gdx.graphics.getHeight() - 80);
		Assets.fonts.defaultFont.draw(batchGUI, "Speed: ("+getVelocity().x+" / "+getVelocity().y+")", 250, Gdx.graphics.getHeight() - 80);
		Assets.fonts.defaultFont.draw(batchGUI, "Position: ("+getPosition().x+" / "+getPosition().y+")", 10, Gdx.graphics.getHeight() - 100);
		Assets.fonts.defaultFont.draw(batchGUI, "Tile Position: ("+getTilePosition().x+" / "+getTilePosition().y+")", 10, Gdx.graphics.getHeight() - 120);
	}

	@Override
	public void update(float delta) {	
		updateCurrentKeyFrameAnimation(delta);
		
		//nothing to do if the player is dead :P
		if(getCurrentState()==States.DEAD) return;
		
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
		if(isMeleeAttackOnCourse()) {
			updateMeleeAttack(delta);
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
				//player shoots (if there is enought ammo)
				if(getAmmo() > 0){
					subAmmo();
					Shoot s= PIT.instance.gameplay.shootSystem.getPool().get();
					s.setType(Shoot.Type.PISTOL_BALL);
					Vector2 v= Entity.getOffsets(dir, 16);
					s.setInitialPosition(getPosition().x + v.x, getPosition().y + v.y);
					s.setDirection(dir);
					PIT.instance.gameplay.shootSystem.addGoodShoot(s);
				}
			}
		}
	}
	
	public void jumpToRaft(Raft raft){
		if(!isRaftAvailable()){
			setRaft(raft);
			this.setPosition(raft.getPosition().x, raft.getPosition().y);
			setCurrentAction(Actions.RAFTING);
		}
	}
	
	public void jumpOffRaft(){
		if(isRaftAvailable()){
			getRaft().moveToLastTilePosition();
			setRaft(null);
			this.setPosition(getTilePosition().x * 32, getTilePosition().y * 32);
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
			}
		}
	}
	
	
	@Override
	public void applyDamage(int amount) {
		super.applyDamage(amount);
		//screen shake! yeey!
		PIT.instance.gameplay.getCamera().rumble(5f, 0.3f);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addScore(int amount){
		this.score+= amount;
	}
	
	
}//end class
