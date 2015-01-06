package com.tomgames.pit.entities.shoots;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.Entity;
import com.tomgames.pit.entities.GameObject;
import com.tomgames.pit.entities.Entity.Directions;

public class Shoot extends GameObject{

	public static enum Type{PISTOL_BALL, ARROW, FIRE_BALL}
	public static enum State{ALIVE, DEAD}
	
	private static int PistolBall_speed= 400;
	private static int PistolBall_range= 350;
	private static int Arrow_speed= 300;
	private static int Arrow_range= 300;
	private static int Fireball_speed= 250;
	private static int Fireball_range= 200;
	
	private Type type;
	private State currentState;
	private float range;	//in pixels
	private TextureRegion texture;		//TODO: this should be an animation
	private Vector2 initialPosition;
	
	private float speed;
	
	public Shoot(){
		this(Type.ARROW, 0, 0, Entity.Directions.S);
		
	}
	
	public Shoot(Type type, float initialPosX, float initialPosY, Entity.Directions direction) {
		super(initialPosX, initialPosY);
		
		this.initialPosition= new Vector2();
		this.setInitialPosition(initialPosX, initialPosY);
		this.setDirection(direction);
		this.setType(type);
		this.setCurrentState(State.ALIVE);
		
	}
	
	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapeRender) {
		if(getCurrentState()==State.ALIVE) {
			//batch.draw(getTexture(), getPosition().x, getPosition().y);
			batch.draw(getTexture(), getPosition().x, getPosition().y, getTexture().getRegionWidth()/2, getTexture().getRegionHeight()/2, getTexture().getRegionWidth(), getTexture().getRegionHeight(), 1, 1, getVelocity().angle());
		}
		/*
		batch.end();
		shapeRender.begin(ShapeType.Line);
		shapeRender.rect(getZone().x, getZone().y, getZone().width, getZone().height);
		shapeRender.rect(getPosition().x, getPosition().y, 5, 5);
		shapeRender.end();
		batch.begin();
		*/
	}

	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		
		if(getCurrentState()==State.ALIVE){
			//calculate new position
			float newX= getPosition().x + (getVelocity().x * delta);
			float newY= getPosition().y + (getVelocity().y * delta);
			setPosition(newX, newY);
			
			//if hits a block, the shoot dies
			
			
			//if the trayectory reachs the limit (range) the shoot dies.
			if(getInitialPosition().dst(getPosition()) >= getRange()){
				setCurrentState(State.DEAD);
			}
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
		switch(type){
		case ARROW: 
			setRange(Arrow_range);
			setTexture(Assets.textures.arrow);
			speed= Arrow_speed;
			break;
		case FIRE_BALL: 
			setRange(Fireball_range);
			setTexture(Assets.textures.fireball);
			speed= Fireball_speed;
			break;
		case PISTOL_BALL: 
			setRange(PistolBall_range);
			setTexture(Assets.textures.shoot);
			speed= PistolBall_speed;
			break;	
		}
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public Vector2 getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(Vector2 initialPosition) {
		this.setInitialPosition(initialPosition.x, initialPosition.y);
	}
	
	public void setInitialPosition(float x, float y) {
		this.initialPosition.set(x, y);
		this.setPosition(x, y);
	}
	
	@Override
	public void setZonePosition(float x, float y){
		getZone().setPosition(x + 10, y + 10);
		getZone().setSize(12, 12);
	}
	
	/**
	 * It sets the speed too.
	 * @param dir
	 */
	public void setDirection(Entity.Directions dir){
		Vector2 v= Entity.getOffsets(dir, speed);
		this.setVelocity(v.x, v.y);
	}
	
	public void reset(){
		this.setInitialPosition(0, 0);
		this.setCurrentState(State.DEAD);
		this.setCurrentState(State.ALIVE);
	}
	
	public static Type getShootType(String type){
		if(type.compareToIgnoreCase("arrow")==0) return Type.ARROW;
		if(type.compareToIgnoreCase("pistolball")==0) return Type.PISTOL_BALL;
		if(type.compareToIgnoreCase("fireball")==0) return Type.FIRE_BALL;
		
		return null;
	}
}//end class
