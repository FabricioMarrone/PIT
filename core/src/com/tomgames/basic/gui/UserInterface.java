package com.tomgames.basic.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.tomgames.pit.PIT;

public class UserInterface {

	private ArrayList<Component> components;
	private Rectangle auxRect;
	
	public UserInterface(){
		components= new ArrayList<Component>();
		auxRect= new Rectangle();
	}
	
	public void update(float delta){
		//mouse current position
		int x= Gdx.input.getX();
		int y= Gdx.graphics.getHeight() - Gdx.input.getY();
		
		//we call update for every visible component
		for(int i= 0; i < components.size(); i++){
			if(components.get(i).isVisible()){
				components.get(i).update(delta);
				if(components.get(i).getZone().contains(x, y)) components.get(i).setMouseOverComponent(true);
				else  components.get(i).setMouseOverComponent(false);
			}
		}
		
		//we check for clicks
		if(Gdx.input.isTouched()){
			for(int i= 0; i < components.size(); i++){
				if(components.get(i).isVisible()){
					if(components.get(i).getZone().contains(x, y)){
						components.get(i).clickOn();
						PIT.instance.getActualScreen().gui_event(components.get(i));
					}else{
						components.get(i).clickOff();
					}
				}
			}
		}
	}//end update
	
	public void render(SpriteBatch batch){
		for(int i= 0; i < components.size(); i++){
			if(components.get(i).isVisible()) components.get(i).render(batch);
		}
	}
	
	public void renderZones(ShapeRenderer shapeRender){
		for(int i= 0; i < components.size(); i++){
			if(components.get(i).isVisible()) components.get(i).renderZone(shapeRender);
		}
	}
	
	public void addComponent(Component c){
		components.add(c);
	}
	
}//end class
