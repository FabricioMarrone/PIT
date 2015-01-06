package com.tomgames.basic.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Component {

	private Rectangle zone;
	private boolean visible;
	private boolean isMouseOverComponent;
	private boolean hasFocus;
	
	public Component(Rectangle r){
		this.setZone(r);
		this.setVisible(true);
		this.setMouseOverComponent(false);
		this.setHasFocus(false);
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(float delta);
	public abstract void clickOn();
	public abstract void clickOff();
	
	public void renderZone(ShapeRenderer shapeRender){
		shapeRender.rect(zone.x, zone.y, zone.width, zone.height);
	}
	
	public void setLocation(float x, float y){
		zone.setPosition(x, y);
	}
	
	public Rectangle getZone() {
		return zone;
	}
	
	public void setZone(Rectangle zone) {
		this.zone = zone;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isMouseOverComponent() {
		return isMouseOverComponent;
	}

	public void setMouseOverComponent(boolean isMouseOverComponent) {
		this.isMouseOverComponent = isMouseOverComponent;
	}

	public boolean isHasFocus() {
		return hasFocus;
	}

	public void setHasFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}
	
}//end class
