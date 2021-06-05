package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
import com.mygdx.game.Main;

import java.io.IOException;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private TextButton createServerButton;
    private TextButton connectToServerButton;
    private TextButton settingsButton;

    private Label title;
    private Label createServerLabel;
    private Label connectToServerLabel;
    private Label settingsLabel;

    private final Main game;
    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();

    public MainMenuScreen(Main game) {
        this.game = game;

        stage = new Stage();
        Image backgroundImage = new Image();
        backgroundImage.setDrawable(new TextureRegionDrawable(new Texture("MainMenuScreen/background.png")));
        backgroundImage.setBounds(0, 0, WIDTH, HEIGHT);
        stage.addActor(backgroundImage);

        init();
    }

    private void init() {
        Gdx.input.setInputProcessor(stage);
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        BitmapFont pointsFont = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        Skin buttonSkin = new Skin();

        float buttonSize = WIDTH / 15;

        titleFont.getData().setScale(3, 3);
        pointsFont.getData().setScale(2.1f, 2.5f);

        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);

        TextButton.TextButtonStyle createServerButtonStyle = new TextButton.TextButtonStyle();
        createServerButtonStyle.font = titleFont;
        createServerButtonStyle.up = buttonSkin.getDrawable("localIdle");
        createServerButtonStyle.down = buttonSkin.getDrawable("localPressed");
        createServerButton = new TextButton("", createServerButtonStyle);
        createServerButton.setSize(buttonSize, buttonSize);
        createServerButton.setPosition(WIDTH / 4, HEIGHT / 6 * 3);
        stage.addActor(createServerButton);

        TextButton.TextButtonStyle connectToServerButtonStyle = new TextButton.TextButtonStyle();
        connectToServerButtonStyle.font = titleFont;
        connectToServerButtonStyle.up = buttonSkin.getDrawable("onlineIdle");
        connectToServerButtonStyle.down = buttonSkin.getDrawable("onlinePressed");
        connectToServerButton = new TextButton("", connectToServerButtonStyle);
        connectToServerButton.setSize(buttonSize, buttonSize);
        connectToServerButton.setPosition(WIDTH / 4, HEIGHT / 6 * 2);
        stage.addActor(connectToServerButton);

        TextButton.TextButtonStyle settingsButtonStyle = new TextButton.TextButtonStyle();
        settingsButtonStyle.font = titleFont;
        settingsButtonStyle.up = buttonSkin.getDrawable("settingsIdle");
        settingsButtonStyle.down = buttonSkin.getDrawable("settingsPressed");
        settingsButton = new TextButton("", settingsButtonStyle);
        settingsButton.setSize(buttonSize, buttonSize);
        settingsButton.setPosition(WIDTH / 4, HEIGHT / 6 * 1);
        stage.addActor(settingsButton);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        title = new Label(Main.title, titleStyle);
        title.setPosition(WIDTH / 2 - title.getWidth() / 2, HEIGHT / 6 * 5);
        stage.addActor(title);

        Label.LabelStyle pointsStyle = new Label.LabelStyle();
        pointsStyle.font = pointsFont;

        createServerLabel = new Label("Create a game", pointsStyle);
        createServerLabel.setPosition(WIDTH / 4 * 1.3f, HEIGHT / 6 * 3);
        stage.addActor(createServerLabel);

        connectToServerLabel = new Label("Connect to a game", pointsStyle);
        connectToServerLabel.setPosition(WIDTH / 4 * 1.3f, HEIGHT / 6 * 2);
        stage.addActor(connectToServerLabel);

        settingsLabel = new Label("Settings", pointsStyle);
        settingsLabel.setPosition(WIDTH / 4 * 1.3f, HEIGHT / 6 * 1);
        stage.addActor(settingsLabel);

        createServerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createNewGamge();
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        connectToServerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    connectToGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openSettings();
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });
    }

    private void openSettings() {
        game.setScreen(new OptionsScreen(game));
    }

    private void connectToGame() throws IOException {
        game.setScreen(new WaitingScreen(Main.clientState, "", "", game));
    }

    private void createNewGamge() {
        game.setScreen(new ChooseMapScreen(game));
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
//        gamePort.update(width, height);
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
