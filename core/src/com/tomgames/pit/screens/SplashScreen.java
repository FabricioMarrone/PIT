package com.tomgames.pit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.tomgames.basic.ScreenClass;
import com.tomgames.basic.gui.Component;
import com.tomgames.basic.resources.Assets;
import com.tomgames.pit.GameCamera;
import com.tomgames.pit.PIT;
import com.tomgames.pit.TiledMapUtilities;

public class SplashScreen extends ScreenClass{

	private SpriteBatch batch;
	private ShapeRenderer shapeRender;
	private GameCamera camera;
	
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	
	public SplashScreen() {
		Assets.loadAllResources();
		
		batch = new SpriteBatch();
		shapeRender= new ShapeRenderer();
		camera= new GameCamera();
		
		tiledMap = new TmxMapLoader().load("maps/empty.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		TiledMapUtilities.loadAnimatedTiles(tiledMap);
		
		tiledMapRenderer.setView(camera.getOrthographicCamera());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0.5f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tiledMapRenderer.render();
		String str= "          Welcome brave Pirate!\n\n    Collect all the Treasures\n     to complete the odyssey!";
		batch.begin();
		batch.draw(Assets.textures.splash, 0, 0);
		batch.draw(Assets.textures.letter2, 250, 100);
		Assets.fonts.clueFont.drawMultiLine(batch, str, 320, 320);
		batch.draw(Assets.textures.dead, 315, 290);
		batch.draw(Assets.textures.dead, 545, 290);
		batch.draw(Assets.textures.bigTreasure, 530, 232);
		Assets.fonts.uiFont.draw(batch, "Click to START", 395, 180);
		batch.end();
		
		if(Gdx.input.isTouched()){
			PIT.instance.setScreen(PIT.instance.gameplay);
		}
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

	@Override
	public void gui_event(Component e) {
		// TODO Auto-generated method stub
		
	}

}//end class
