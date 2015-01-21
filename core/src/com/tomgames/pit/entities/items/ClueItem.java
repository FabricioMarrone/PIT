package com.tomgames.pit.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.entities.Player;

public class ClueItem extends Item{

	private String message;
	private boolean showMessage;
	//private float messageTime= 5f;
	//private float elapsedTime;
	
	public ClueItem(int tilePosX, int tilePosY) {
		super(tilePosX, tilePosY);
		
		setMessage("");
		setTexture(Assets.textures.clue);
	}
	
	@Override
	public void renderGUI(SpriteBatch batchGUI) {
		super.renderGUI(batchGUI);
		
		if(showMessage) {
			batchGUI.draw(Assets.textures.letter, 100, 50);
			Assets.fonts.defaultFont.drawMultiLine(batchGUI, getMessage(), 165, 350);
			Assets.fonts.defaultFont.drawMultiLine(batchGUI, "Press C to close", 190, 120);
		}
	}

	


	@Override
	public void update(float delta) {
		super.update(delta);
		
		if(showMessage){
			//elapsedTime+= delta;
			//if(elapsedTime > messageTime){
			//	elapsedTime= 0;
			//	showMessage= false;
			//}
			if(Gdx.input.isKeyJustPressed(Keys.C)) showMessage= false;
		}
	}

	@Override
	public void itemTaken(Player player) {
		super.itemTaken(player);
		showMessage= true;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * This should be used only for re-read messages.
	 */
	public void showMessage(){
		this.showMessage= true;
	}
}//end class
