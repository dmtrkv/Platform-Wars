package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Main;
import com.mygdx.game.Multiplayer.PacketMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChooseFighterScreen implements Screen {

    private final Stage stage;

    private final String map;

    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final Main game;
    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();

    public ArrayList<String> fighters = new ArrayList<>();
    private int fighterIndex = 0;
    private final Skin buttonSkin;
    private final BitmapFont font;
    private final Image fighterImage;
    private String currentFighter;
    private final Label fighterLabel;

    private final HashMap<String, String> passiveSkills;
    private final String state;

    private Client client;
    private Server server;
    private String secondFighter;


    public ChooseFighterScreen(Main game, String map, String state, String removeFighter, Client client, Server server) {

        fighters.add("Samurai");
        fighters.add("King");
        fighters.add("Warrior");
        fighters.add("Wizard");
        fighters.add("Huntress");

        if (!removeFighter.equals("-")) {
            fighters.remove(removeFighter);
        }

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);

        this.map = map;
        this.state = state;
        this.client = client;
        this.server = server;
        this.secondFighter = removeFighter;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        buttonSkin = new Skin();
        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);
        font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);


        fighterImage = new Image();
        Image backgroundImage = new Image();
        backgroundImage.setDrawable(new TextureRegionDrawable(new Texture("ChooseFighterScreen/background.png")));
        backgroundImage.setBounds(0, 0, WIDTH, HEIGHT);
        stage.addActor(backgroundImage);

        fighterLabel = new Label("", labelStyle);
        fighterLabel.setWidth(WIDTH);
        fighterLabel.setFontScale(1.5f);
        fighterLabel.setAlignment(Align.center);
        fighterLabel.setPosition(WIDTH / 2 - fighterLabel.getWidth() / 2, HEIGHT / 9 * 8);

        passiveSkills = new HashMap<>();
        passiveSkills.put("Samurai", "High jump");
        passiveSkills.put("Warrior", "Enormous speed");
        passiveSkills.put("King", "A lot of health");
        passiveSkills.put("Wizard", "None");

        createFightersTable();
        initButtons();
    }

    private void initButtons() {

        final float buttonSize = WIDTH / 15;

        TextButton.TextButtonStyle previousFighterButtonStyle = new TextButton.TextButtonStyle();
        previousFighterButtonStyle.font = font;
        previousFighterButtonStyle.up = buttonSkin.getDrawable("leftIdle");
        previousFighterButtonStyle.down = buttonSkin.getDrawable("leftPressed");
        TextButton previousFighterButton = new TextButton("", previousFighterButtonStyle);
        previousFighterButton.setSize(buttonSize, buttonSize);
        previousFighterButton.setPosition(WIDTH / 5 * 2, 50);
        stage.addActor(previousFighterButton);

        TextButton.TextButtonStyle nextFighterButtonStyle = new TextButton.TextButtonStyle();
        nextFighterButtonStyle.font = font;
        nextFighterButtonStyle.up = buttonSkin.getDrawable("rightIdle");
        nextFighterButtonStyle.down = buttonSkin.getDrawable("rightPressed");
        TextButton nextFighterButton = new TextButton("", nextFighterButtonStyle);
        nextFighterButton.setSize(buttonSize, buttonSize);
        nextFighterButton.setPosition(WIDTH / 5 * 3 - nextFighterButton.getWidth(), 50);
        stage.addActor(nextFighterButton);

        TextButton.TextButtonStyle startGameButtonStyle = new TextButton.TextButtonStyle();
        startGameButtonStyle.font = font;
        startGameButtonStyle.up = buttonSkin.getDrawable("playIdle");
        startGameButtonStyle.down = buttonSkin.getDrawable("playPressed");
        final TextButton startGameButton = new TextButton("", startGameButtonStyle);
        startGameButton.setSize(buttonSize, buttonSize);
        startGameButton.setPosition(WIDTH / 2 - startGameButton.getWidth() / 2, 50);
        stage.addActor(startGameButton);

        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = font;
        backButtonStyle.up = buttonSkin.getDrawable("backIdle");
        backButtonStyle.down = buttonSkin.getDrawable("backPressed");
        TextButton backButton = new TextButton("", backButtonStyle);
        backButton.setSize(buttonSize, buttonSize);
        backButton.setPosition(10, 10);
        stage.addActor(backButton);

        previousFighterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fighterIndex--;
                if (fighterIndex == -1) {
                    fighterIndex = fighters.size() - 1;
                }
                createFightersTable();
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        nextFighterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fighterIndex++;
                if (fighterIndex == fighters.size()) {
                    fighterIndex = 0;
                }
                createFightersTable();
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    if (state.equals(Main.clientState)) {
                        PacketMessage fighterResponse = new PacketMessage();
                        fighterResponse.text = String.format("fighter: %s", currentFighter);
                        client.sendTCP(fighterResponse);
                        game.setScreen(new PlayScreen(game, currentFighter, secondFighter, map, server, client, Main.clientState));
                    } else if (state.equals(Main.serverState)) {
                        game.setScreen(new WaitingScreen(Main.serverState, map, currentFighter, game));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (state.equals(Main.serverState)) {
                    game.setScreen(new ChooseMapScreen(game));
                } else if (state.equals(Main.clientState)) {
                    game.setScreen(new MainMenuScreen(game));
                }
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });
    }

    private void createFightersTable() {
        currentFighter = fighters.get(fighterIndex);
        fighterImage.setDrawable(new TextureRegionDrawable(new Texture(String.format("ChooseFighterScreen/%s.png", currentFighter))));

        float pictureHeight = HEIGHT / 4;
        float pictureWidth = WIDTH / 8;

        fighterImage.setBounds(WIDTH / 2 - pictureWidth / 2, HEIGHT / 14 * 8 - 5, pictureWidth, pictureHeight);
        stage.addActor(fighterImage);

        fighterLabel.setText(currentFighter);
//        fighterLabel.setDebug(true);
        stage.addActor(fighterLabel);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
