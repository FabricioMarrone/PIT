package com.tomgames.pit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tomgames.basic.ScreenClass;
import com.tomgames.basic.gui.Component;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.SecretMessages;
import com.tomgames.pit.entities.items.ClueItem;
import com.tomgames.pit.entities.items.Item;

public class TestScreen extends ScreenClass {

	SpriteBatch batch, batchGUI;
	ShapeRenderer shapeRender;
	
	ClueItem item, item2, item3;
	
	public TestScreen() {
		Assets.loadAllResources();
		
		batch = new SpriteBatch();
		batchGUI = new SpriteBatch();
		shapeRender= new ShapeRenderer();
		
		init();
	}
	
	public void init(){
		item= new ClueItem(10, 10);
		item.setMessage(SecretMessages.m8_Msg_0);
		item.setCurrentState(Item.States.DISCOVERED);
		
		item2= new ClueItem(15, 10);
		item2.setMessage(SecretMessages.m8_Msg_1);
		item2.setCurrentState(Item.States.DISCOVERED);
		
		item3= new ClueItem(20, 10);
		item3.setMessage(SecretMessages.m8_Msg_2);
		item3.setCurrentState(Item.States.DISCOVERED);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		item.render(batch, null);
		item2.render(batch, null);
		item3.render(batch, null);
		batch.end();
		
		batchGUI.begin();
		item.renderGUI(batchGUI);
		item2.renderGUI(batchGUI);
		item3.renderGUI(batchGUI);
		batchGUI.end();
		
		item.update(delta);
		item2.update(delta);
		item3.update(delta);
		
		if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
			item.setCurrentState(Item.States.TAKEN);
			item.showMessage();
		}
		if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
			item2.setCurrentState(Item.States.TAKEN);
			item2.showMessage();
		}
		if(Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
			item3.setCurrentState(Item.States.TAKEN);
			item3.showMessage();
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) init();
	}

	@Override
	public void gui_event(Component e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}//end class
