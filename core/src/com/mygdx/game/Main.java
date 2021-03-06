package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.MainMenuScreen;
import com.mygdx.game.Screens.PlayScreen;

import java.util.ArrayList;

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

	public static final String newPlayerEvent = "new_player_event";
	public static final String startGameEvent = "start_game_event";
	public static final String winnerMessage = "winner: ";
	public static final String mapMessage = "map: ";
	public static final String fighterMessage = "fighter: ";

	public static final String attackAction = "attack";
	public static final String runRightAction = "run_right";
	public static final String runLeftAction = "run_left";
	public static final String jumpAction = "jump";

	public static final String serverState = "server";
	public static final String clientState = "client";

	public static boolean playSounds = true;

	public static ArrayList<String> actions = new ArrayList<>();

	public static final int tcpPort = 27960;
	public static final int udpPort = 27960;

	public static Sound jumpSound;
	public static Sound samuraiAttackSound;
	public static Sound warriorAttackSound;
	public static Sound kingAttackSound;
	public static Sound huntressAttackSound;
	public static Sound wizardAttackSound;
	public static Sound buttonSound;
	public static Sound takeDamageSound;

	public static final String title = "Platform Wars";

	@Override
	public void create () {
		actions.add(attackAction);
		actions.add(runLeftAction);
		actions.add(runRightAction);
		actions.add(jumpAction);

		jumpSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/jump.mp3"));

		samuraiAttackSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Samurai/attack.mp3"));
		warriorAttackSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Warrior/attack.mp3"));
		kingAttackSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/King/attack.mp3"));
		huntressAttackSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Huntress/attack.mp3"));
		wizardAttackSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Wizard/attack.mp3"));
		buttonSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/button.mp3"));
		takeDamageSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/takeDamage.mp3"));

		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
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
