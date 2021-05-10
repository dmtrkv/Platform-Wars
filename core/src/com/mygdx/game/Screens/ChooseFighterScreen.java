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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.Sprites.Fighters.Wizard;

import java.util.HashMap;

public class ChooseFighterScreen implements Screen {

    private Stage stage;
    private TextButton startGameButton;
    private TextButton previousFighterButton;
    private TextButton nextFighterButton;

    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final Main game;
    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();

    private final String[] fighters = {"Samurai", "King", "Warrior", "Wizard"};
    private int fighterIndex = 0;
    private Skin buttonSkin;
    private TextureAtlas buttonTextureAtlas;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Image fighterImage;
    private String currentFighter;

    private HashMap<String, String> passiveSkills;


    public ChooseFighterScreen(Main game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        buttonSkin = new Skin();
        buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);
        font = new BitmapFont();

        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        fighterImage = new Image();

        passiveSkills = new HashMap<>();
        passiveSkills.put("Samurai", "High jump");
        passiveSkills.put("Warrior", "Enormous speed");
        passiveSkills.put("King", "A lot of health");
        passiveSkills.put("Wizard", "None");

        createFightersTable();
        initButtons();
    }

    private void initButtons() {
        TextButton.TextButtonStyle previousFighterButtonStyle = new TextButton.TextButtonStyle();
        previousFighterButtonStyle.font = font;
        previousFighterButtonStyle.up = buttonSkin.getDrawable("leftIdle");
        previousFighterButtonStyle.down = buttonSkin.getDrawable("leftPressed");
        previousFighterButton = new TextButton("", previousFighterButtonStyle);
        previousFighterButton.setSize(150, 100);
        previousFighterButton.setPosition(WIDTH / 5 - previousFighterButton.getWidth() / 2,
                HEIGHT / 2 - 200 - previousFighterButton.getHeight() / 2);
        stage.addActor(previousFighterButton);

        TextButton.TextButtonStyle nextFighterButtonStyle = new TextButton.TextButtonStyle();
        nextFighterButtonStyle.font = font;
        nextFighterButtonStyle.up = buttonSkin.getDrawable("rightIdle");
        nextFighterButtonStyle.down = buttonSkin.getDrawable("rightPressed");
        nextFighterButton = new TextButton("", nextFighterButtonStyle);
        nextFighterButton.setSize(150, 100);
        nextFighterButton.setPosition(WIDTH - WIDTH / 5 - previousFighterButton.getWidth() / 2,
                HEIGHT / 2 - 200 - previousFighterButton.getHeight() / 2);
        stage.addActor(nextFighterButton);

        TextButton.TextButtonStyle startGameButtonStyle = new TextButton.TextButtonStyle();
        startGameButtonStyle.font = font;
        startGameButtonStyle.up = buttonSkin.getDrawable("playIdle");
        startGameButtonStyle.down = buttonSkin.getDrawable("playPressed");
        startGameButton = new TextButton("", startGameButtonStyle);
        startGameButton.setSize(150, 100);
        startGameButton.setPosition(WIDTH / 2 - startGameButton.getWidth() / 2, 50);
        stage.addActor(startGameButton);

        previousFighterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fighterIndex--;
                if (fighterIndex == -1) {
                    fighterIndex = fighters.length - 1;
                }
                createFightersTable();
            }
        });

        nextFighterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fighterIndex++;
                if (fighterIndex == fighters.length) {
                    fighterIndex = 0;
                }
                createFightersTable();
            }
        });

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(game, currentFighter, "map3"));
            }
        });
    }

    private void createFightersTable() {
        currentFighter = fighters[fighterIndex];
        fighterImage.setDrawable(new TextureRegionDrawable(new Texture(String.format("ChooseFighterScreen/%s.png", currentFighter))));

        float pictureHeight = HEIGHT / 3;
        float pictureWidth = WIDTH / 5;

        fighterImage.setBounds(WIDTH / 2 - pictureWidth / 2, HEIGHT / 2 - pictureHeight / 2 + 100,
                pictureWidth, pictureHeight);
        stage.addActor(fighterImage);
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

    }
}
