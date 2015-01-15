package com.tomgames.pit;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tomgames.basic.resources.Assets;

public class Message {

	private String message;
	private boolean visible;
	private float visibleTime;
	private float elapsedVisibleTime;
	
	public Message(){
		visible= false;
		message= "";
		visibleTime= 4f;	//default value
	}
	
	public void render(SpriteBatch batch, float posX, float posY){
		if(visible) Assets.fonts.defaultFont.draw(batch, message, posX, posY);
	}
	
	public void update(float delta){
		if(visible){
			elapsedVisibleTime+= delta;
			if(elapsedVisibleTime > visibleTime){
				elapsedVisibleTime= 0;
				visible= false;
			}
		}
	}
	
	public void show(String message, float visibleTime){
		this.show(message, visibleTime, null);
	}
	
	public void show(String message, float visibleTime, Sound audio){
		this.visible= true;
		this.message= message;
		setVisibleTime(visibleTime);
		elapsedVisibleTime= 0;
		if(audio!=null) audio.play();
		
	}
	
	public float getVisibleTime() {
		return visibleTime;
	}


	public void setVisibleTime(float visibleTime) {
		this.visibleTime = visibleTime;
	}
	
}//end lass
