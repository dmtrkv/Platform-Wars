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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Main;

public class ChooseMapScreen implements Screen {

    private final Main game;
    private final Stage stage;
    private final float WIDTH = Gdx.graphics.getWidth();
    private final float HEIGHT = Gdx.graphics.getHeight();
    private final Skin buttonSkin;
    private final BitmapFont font;
    private final Image mapImage;
    private String currentMap;
    private int mapIndex = 0;
    private final String[] maps = {"Dojo", "Dungeon", "Desert"};
    private final Label mapLabel;

    public ChooseMapScreen(Main game) {
        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        buttonSkin = new Skin();
        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);
        font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        mapLabel = new Label("", labelStyle);
        mapLabel.setWidth(WIDTH);
        mapLabel.setFontScale(1.5f);
        mapLabel.setAlignment(Align.center);
        mapLabel.setPosition(WIDTH / 2 - mapLabel.getWidth() / 2, HEIGHT / 9 * 8);

        mapImage = new Image();

        createMapTable();
        initButtons();
    }

    private void createMapTable() {
        currentMap = maps[mapIndex];
        mapImage.setDrawable(new TextureRegionDrawable(new Texture(String.format("ChooseMapScreen/%s.png", currentMap))));

        mapImage.setBounds(0, 0, WIDTH, HEIGHT);
        stage.addActor(mapImage);

        mapLabel.setText(currentMap);

        Gdx.app.log(String.format("%s", WIDTH), String.format("%s", HEIGHT));

        stage.addActor(mapLabel);
    }

    private void initButtons() {

        float buttonSize = WIDTH / 15;

        TextButton.TextButtonStyle previousMapButtonStyle = new TextButton.TextButtonStyle();
        previousMapButtonStyle.font = font;
        previousMapButtonStyle.up = buttonSkin.getDrawable("leftIdle");
        previousMapButtonStyle.down = buttonSkin.getDrawable("leftPressed");
        TextButton previousMapButton = new TextButton("", previousMapButtonStyle);
        previousMapButton.setSize(buttonSize, buttonSize);
        previousMapButton.setPosition(WIDTH / 5 * 2, 50);
        stage.addActor(previousMapButton);

        TextButton.TextButtonStyle nextMapButtonStyle = new TextButton.TextButtonStyle();
        nextMapButtonStyle.font = font;
        nextMapButtonStyle.up = buttonSkin.getDrawable("rightIdle");
        nextMapButtonStyle.down = buttonSkin.getDrawable("rightPressed");
        TextButton nextMapButton = new TextButton("", nextMapButtonStyle);
        nextMapButton.setSize(buttonSize, buttonSize);
        nextMapButton.setPosition(WIDTH / 5 * 3 - nextMapButton.getWidth(), 50);
        stage.addActor(nextMapButton);

        TextButton.TextButtonStyle startGameButtonStyle = new TextButton.TextButtonStyle();
        startGameButtonStyle.font = font;
        startGameButtonStyle.up = buttonSkin.getDrawable("playIdle");
        startGameButtonStyle.down = buttonSkin.getDrawable("playPressed");
        TextButton startGameButton = new TextButton("", startGameButtonStyle);
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

        previousMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mapIndex--;
                if (mapIndex == -1) {
                    mapIndex = maps.length - 1;
                }
                createMapTable();
                if (Main.playSounds)
                    Main.buttonSound.play();
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
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ChooseFighterScreen(game, currentMap, Main.serverState, "-", new Client(), new Server()));
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                if (Main.playSounds)
                    Main.buttonSound.play();
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
