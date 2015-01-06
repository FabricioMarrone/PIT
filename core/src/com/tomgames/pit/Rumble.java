package com.tomgames.pit;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Rumble {

	private float time;
	private Random random;
	private float x, y;
	private float current_time;
	private float power;
	private float current_power;
	private boolean finish;
	
	public Rumble(){
		random = new Random();
		time = 0;
		current_time = 0;
		power = 0;
		current_power = 0;
		finish= false;
	}
	
	/**
	 * Call this function with the force of the shake  and how long it should last   
	 */
	public void rumble(float power, float time) {
		this.power = power;
		this.time = time;
		this.current_time = 0;
		finish= false;
	}
	
	public void update(float delta, OrthographicCamera cam, float correctX, float correctY){
		if(current_time <= time) {
			current_time += delta;
			current_power = power * ((time - current_time) / time);
			// generate random new x and y values taking into account
			// how much force was passed in
			x = (random.nextFloat() - 0.5f) * 2 * current_power;
			y = (random.nextFloat() - 0.5f) * 2 * current_power;

			// reset to original position
			cam.position.set(correctX, correctY, 0);
			cam.update();
			
			// Set the camera to this new x/y position           
			cam.translate(-x, -y);
		} else {
			if(!finish){
				finish= true;
				// When the shaking is over move the camera back to the original position
				cam.position.x = correctX;
				cam.position.y = correctY;
			}
		}
	}
}//end class 
