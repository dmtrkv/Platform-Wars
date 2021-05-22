package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.MainMenuScreen;
import com.mygdx.game.Screens.PlayScreen;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Main extends Game {
	public SpriteBatch batch;
	public static int V_WIDTH = 400;
	public static int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short DEFAULT_BIT = 1;
	public static final short WARRIOR_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short SPIKE_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short SAMURAI_BIT = 32;
	public static final short KING_BIT = 64;
	public static final short WARRIOR_ATTACK_BIT = 128;
	public static final short SAMURAI_ATTACK_BIT = 256;
	public static final short KING_ATTACK_BIT = 512;
	public static final short WIZARD_BIT = 1024;
	public static final short WIZARD_ATTACK_BIT = 2048;
	public static final short HUNTRESS_BIT = 4096;
	public static final short HUNTRESS_ATTACK_BIT = 8192;

	private Socket socket;


	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	private void connectSocket() {
		try {
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		}
		catch (Exception e) {
			Gdx.app.log("exception", e.toString());
		}
	}

	public void changeScreen (Screen screen) {
		setScreen(screen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
