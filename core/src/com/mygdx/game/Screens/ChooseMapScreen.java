package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;

public class ChooseMapScreen implements Screen {

    private Main game;
    private final Viewport gamePort;
    private final OrthographicCamera gameCam;
    private final Stage stage;
    private TextButton startGameButton;
    private TextButton previousMapButton;
    private TextButton nextMapButton;
    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();
    private Skin buttonSkin;
    private TextureAtlas buttonTextureAtlas;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Image mapImage;
    private String currentMap;
    private int mapIndex = 0;
    private String fighter;
    private TextButton.TextButtonStyle previousMapButtonStyle;
    private TextButton.TextButtonStyle nextMapButtonStyle;
    private TextButton.TextButtonStyle startGameButtonStyle;
    private String[] maps = {"Dojo", "Dungeon", "Desert"};

    public ChooseMapScreen(Main game, String fighter) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        buttonSkin = new Skin();
        buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);
        font = new BitmapFont();

        mapImage = new Image();
        this.fighter = fighter;

        createMapTable();
        initButtons();
    }

    private void createMapTable() {
        currentMap = maps[mapIndex];
        mapImage.setDrawable(new TextureRegionDrawable(new Texture(String.format("ChooseMapScreen/%s.png", currentMap))));

        float pictureHeight = HEIGHT / 2;
        float pictureWidth = pictureHeight;

        mapImage.setBounds(WIDTH / 2 - pictureWidth / 2, HEIGHT / 2 - pictureHeight / 2 + 100,
                pictureWidth, pictureHeight);
        stage.addActor(mapImage);
    }

    private void initButtons() {
        previousMapButtonStyle = new TextButton.TextButtonStyle();
        previousMapButtonStyle.font = font;
        previousMapButtonStyle.up = buttonSkin.getDrawable("leftIdle");
        previousMapButtonStyle.down = buttonSkin.getDrawable("leftPressed");
        previousMapButton = new TextButton("", previousMapButtonStyle);
        previousMapButton.setSize(150, 100);
        previousMapButton.setPosition(WIDTH / 5 - previousMapButton.getWidth() / 2,
                HEIGHT / 2 - 200 - previousMapButton.getHeight() / 2);
        stage.addActor(previousMapButton);

        nextMapButtonStyle = new TextButton.TextButtonStyle();
        nextMapButtonStyle.font = font;
        nextMapButtonStyle.up = buttonSkin.getDrawable("rightIdle");
        nextMapButtonStyle.down = buttonSkin.getDrawable("rightPressed");
        nextMapButton = new TextButton("", nextMapButtonStyle);
        nextMapButton.setSize(150, 100);
        nextMapButton.setPosition(WIDTH - WIDTH / 5 - previousMapButton.getWidth() / 2,
                HEIGHT / 2 - 200 - previousMapButton.getHeight() / 2);
        stage.addActor(nextMapButton);

        startGameButtonStyle = new TextButton.TextButtonStyle();
        startGameButtonStyle.font = font;
        startGameButtonStyle.up = buttonSkin.getDrawable("playIdle");
        startGameButtonStyle.down = buttonSkin.getDrawable("playPressed");
        startGameButton = new TextButton("", startGameButtonStyle);
        startGameButton.setSize(150, 100);
        startGameButton.setPosition(WIDTH / 2 - startGameButton.getWidth() / 2, 50);
        stage.addActor(startGameButton);

        previousMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mapIndex--;
                if (mapIndex == -1) {
                    mapIndex = maps.length - 1;
                }
                createMapTable();
            }
        });

        nextMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mapIndex++;
                if (mapIndex == maps.length) {
                    mapIndex = 0;
                }
                createMapTable();
            }
        });

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(game, fighter, currentMap));
            }
        });
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
        stage.dispose();
    }
}
