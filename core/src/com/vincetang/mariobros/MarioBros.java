package com.vincetang.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vincetang.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {
	/** Virtual width and height **/
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short GROUND_BIT = 1;   // 00001
	public static final short MARIO_BIT = 2; // 00010
	public static final short BRICK_BIT = 4; // 00100
	public static final short COIN_BIT = 8;	//	01000
	public static final short DESTROYED_BIT = 16; // 10000
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT= 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;

	public SpriteBatch batch;

	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.finishLoading();


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
		manager.dispose();
	}
}
