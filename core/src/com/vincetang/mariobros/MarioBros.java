package com.vincetang.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vincetang.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {
	/** Virtual width and height **/
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public SpriteBatch batch;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		// delegates render method to the playscreen (or whatever screen is active)
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
