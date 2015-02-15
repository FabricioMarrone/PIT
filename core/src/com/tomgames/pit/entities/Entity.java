package com.tomgames.pit.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.PIT;

public abstract class Entity extends GameObject{

	public static enum States{ALIVE, DEAD}
	public static enum Directions{N,S,E,W,NE,SE,SW,NW}
	public static enum Actions{EXPLORING, DIGGING, RAFTING}
	public static enum AttackMode{MELEE, RANGED}
	
	private int lifePoints;
	private int ammo;
	private float runSpeed;
	private States currentState;
	private Directions currentDirection;
	private Actions currentAction;
	private AttackMode currentAttackMode;
	private boolean meleeAttackOnCourse;
	private Directions meleeAttackCurrentDirection;
	private float meleeAttackTimeDuration= 0.1f;
	protected float elapsedTime_meleeAttackTimeDuration;
	private Raft raft;
	private boolean boatAvailable;
	private Animation[] stateAnimations;
	private Animation[] directionAnimations;
	private Animation digginAnimation;
	private TextureRegion currentKeyFrame;
	/** aux var used on animations. Everytime you reset the current state or direction, the elapsed time resets too. */
	private float elapsedTime;
	
	protected float raftSpeedMultiplier= 1.2f;
	protected float waterDamping= 2f;
	protected float normalDamping= 15f;
	
	public Entity(float posX, float posY) {
		super(posX, posY);
		this.setCurrentState(States.ALIVE);
		this.setCurrentDirection(Directions.S);
		this.setCurrentAction(Actions.EXPLORING);
		this.setLifePoints(100);
		this.stateAnimations= new Animation[2];
		this.directionAnimations= new Animation[8];
	}

	protected void updateCurrentKeyFrameAnimation(float delta){
		elapsedTime+= delta;
		TextureRegion tr;
		
		if(getCurrentState() == States.DEAD){
			tr= getStateAnimations(States.DEAD).getKeyFrame(elapsedTime);
		}else{
			switch(getCurrentAction()){
			case DIGGING: tr= getDigginAnimation().getKeyFrame(elapsedTime);
				break;
			case EXPLORING: tr= getDirectionAnimations(getCurrentDirection()).getKeyFrame(elapsedTime);
				break;
			case RAFTING: tr= Assets.textures.raft;
				break;
			
			default: 
				//this should never happen
				tr= getDirectionAnimations(getCurrentDirection()).getKeyFrame(elapsedTime);
				break;
			}
		}
		setCurrentKeyFrame(tr);
	}//end update animation
	
	protected void updateMeleeAttack(float delta){
		elapsedTime_meleeAttackTimeDuration+= delta;
		if(elapsedTime_meleeAttackTimeDuration > getMeleeAttackTimeDuration()){
			elapsedTime_meleeAttackTimeDuration= 0;
			setMeleeAttackOnCourse(false);
		}
	}
	
	public TextureRegion getCurrentKeyFrame() {
		return currentKeyFrame;
	}

	public void setCurrentKeyFrame(TextureRegion currentKeyFrame) {
		this.currentKeyFrame = currentKeyFrame;
	}
	
	public int getLifePoints() {
		return lifePoints;
	}

	public void setLifePoints(int lifePoints) {
		this.lifePoints = lifePoints;
	}

	/**
	 * The damage applies directly into the life points. If it reach zero, the entity dies.
	 * @param amount
	 */
	public void applyDamage(int amount){
		this.lifePoints-= amount;
		if(lifePoints <= 0){
			lifePoints= 0;
			this.setCurrentState(States.DEAD);
			PIT.instance.getPlayer().addScore(100);
		}
	}
	
	public void addLifePoints(int amount){
		this.lifePoints+= amount;
		if(this.lifePoints > 100) lifePoints= 100;
	}
	
	/**
	 * Run speed will be faster if the entity is on a raft.
	 * @return
	 */
	public float getRunSpeed() {
		if(getCurrentAction() == Actions.RAFTING) return runSpeed * raftSpeedMultiplier;
		else return runSpeed;
		//return runSpeed;
	}

	public void setRunSpeed(float runSpeed) {
		this.runSpeed = runSpeed;
	}

	public States getCurrentState() {
		return currentState;
	}

	/**
	 * Using this method aumatically reset the elapsedtime used on animations (but only if there is a real change of state)
	 * @param currentState
	 */
	public void setCurrentState(States currentState) {
		if(this.currentState != currentState){
			this.currentState = currentState;
			this.elapsedTime= 0;
		}
	}

	public Directions getCurrentDirection() {
		return currentDirection;
	}

	/**
	 * Using this method aumatically reset the elapsedtime used on animations (but only if there is a real change of direction)
	 * @param currentDirection
	 */
	public void setCurrentDirection(Directions currentDirection) {
		if(this.currentDirection != currentDirection){
			this.currentDirection = currentDirection;
			this.elapsedTime= 0;
		}
	}

	public Animation[] getStateAnimations() {
		return stateAnimations;
	}
	
	public Animation getStateAnimations(States state) {
		return stateAnimations[state.ordinal()];
	}

	public void setStateAnimations(States state, Animation anim) {
		this.stateAnimations[state.ordinal()] = anim;
	}

	public Animation[] getDirectionAnimations() {
		return directionAnimations;
	}
	
	public Animation getDirectionAnimations(Directions dir) {
		return directionAnimations[dir.ordinal()];
	}

	public void setDirectionAnimations(Directions dir, Animation anim) {
		this.directionAnimations[dir.ordinal()] = anim;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void addAmmo(int cant){
		this.ammo+= cant;
	}
	/**
	 * Subtracts one unit of ammo.
	 */
	public void subAmmo(){
		this.ammo-= 1;
		if(this.ammo < 0) ammo= 0;
	}

	public boolean isRaftAvailable() {
		if(getRaft()==null) return false;
		else return true;
	}

	public Raft getRaft(){
		return raft;
	}
	
	public void setRaft(Raft raft) {
		this.raft = raft;
	}

	public boolean isBoatAvailable() {
		return boatAvailable;
	}

	public void setBoatAvailable(boolean boatAvailable) {
		this.boatAvailable = boatAvailable;
	}
	
	public Actions getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(Actions currentAction) {
		this.currentAction = currentAction;
	}
	
	
	public Animation getDigginAnimation() {
		return digginAnimation;
	}

	public void setDigginAnimation(Animation digginAnimation) {
		this.digginAnimation = digginAnimation;
	}

	public void applyDamping(float delta){
		float velX, velY;
		if(getCurrentAction() == Actions.RAFTING) {
			velX= getVelocity().x - (getVelocity().x * waterDamping * delta);
			velY= getVelocity().y - (getVelocity().y * waterDamping * delta);
		}
		else{
			velX= getVelocity().x - (getVelocity().x * normalDamping * delta);
			velY= getVelocity().y - (getVelocity().y * normalDamping * delta);
		}
		
		if(Math.abs(velX) < 5f) velX= 0;
		if(Math.abs(velY) < 5f) velY= 0;
		
		setVelocity(velX, velY);
	}

	public AttackMode getCurrentAttackMode() {
		return currentAttackMode;
	}

	public void setCurrentAttackMode(AttackMode currentAttackMode) {
		this.currentAttackMode = currentAttackMode;
	}

	public boolean isMeleeAttackOnCourse() {
		return meleeAttackOnCourse;
	}

	public void setMeleeAttackOnCourse(boolean meleeAttackOnCourse) {
		this.meleeAttackOnCourse = meleeAttackOnCourse;
	}

	public Directions getMeleeAttackCurrentDirection() {
		return meleeAttackCurrentDirection;
	}

	public void setMeleeAttackCurrentDirection(
			Directions meleeAttackCurrentDirection) {
		this.meleeAttackCurrentDirection = meleeAttackCurrentDirection;
	}

	public float getMeleeAttackTimeDuration() {
		return meleeAttackTimeDuration;
	}

	public void setMeleeAttackTimeDuration(float meleeAttackTimeDuration) {
		this.meleeAttackTimeDuration = meleeAttackTimeDuration;
	}
	
	/**
	 * Given a direction, this method calculates the offset values to be used, for example, on renders or collisions.
	 * @param dir
	 * @param offX
	 * @param offY
	 */
	public static Vector2 getOffsets(Directions dir, float value){
		float offX= value;
		float offY= value;
		switch(dir){
		case E: 
			offX= value;
			offY= 0;
			break;
		case N:
			offX= 0;
			offY= value;
			break;
		case NE:
			offX= value;
			offY= value;
			break;
		case NW:
			offX= -value;
			offY= value;
			break;
		case S:
			offX= 0;
			offY= -value;
			break;
		case SE:
			offX= value;
			offY= -value;
			break;
		case SW:
			offX= -value;
			offY= -value;
			break;
		case W:
			offX= -value;
			offY= 0;
			break;
		}
		return new Vector2(offX, offY);
	}
	
	public Rectangle getSwordArea(){
		Vector2 v= Entity.getOffsets(getMeleeAttackCurrentDirection(), 32);
		return new Rectangle(getZone().x + v.x, getZone().y + v.y, getZone().width, getZone().height);
	}
	
	public static Directions getDirection(String direction){
		if(direction.compareToIgnoreCase("N")==0) return Directions.N;
		if(direction.compareToIgnoreCase("S")==0) return Directions.S;
		if(direction.compareToIgnoreCase("E")==0) return Directions.E;
		if(direction.compareToIgnoreCase("W")==0) return Directions.W;
		if(direction.compareToIgnoreCase("NE")==0) return Directions.NE;
		if(direction.compareToIgnoreCase("NW")==0) return Directions.NW;
		if(direction.compareToIgnoreCase("SE")==0) return Directions.SE;
		if(direction.compareToIgnoreCase("SW")==0) return Directions.SW;
		
		return null;
	}
	
	/**
	 * Given 2 points (A and B), this method returns the direction (aprox) to join with a "line" the point A (x1,y1) with B (x2,y2)
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static Directions getDirection(int x1, int y1, int x2, int y2){
		//check up and down
		if(x1==x2){
			//check N
			if(y1<=y2) return Directions.N;
			//check S
			if(y1>=y2) return Directions.S;
		}
		//check left and rigth
		if(y1==y2){
			//check left
			if(x1<=x2) return Directions.E;
			//check rigth
			if(x1>=x2) return Directions.W;
		}
		//check NS
		for(int i= 0; i < 10; i++){
			if((x1==(x2-i)) && (y1==(y2-i))) return Directions.NE;
		}
		//check WS
		for(int i= 0; i < 10; i++){
			if((x1==(x2+i)) && (y1==(y2+i))) return Directions.SW;
		}
		//check NW
		for(int i= 0; i < 10; i++){
			if((x1==(x2+i)) && (y1==(y2-i))) return Directions.NW;
		}
		//check SE
		for(int i= 0; i < 10; i++){
			if((x1==(x2-i)) && (y1==(y2+i))) return Directions.SE;
		}
		
		return null;
	}
}//end class
