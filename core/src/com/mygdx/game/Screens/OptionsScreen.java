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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Main;

public class OptionsScreen implements Screen {

    private final Main game;

    private final float WIDTH = Gdx.graphics.getWidth();
    private final float HEIGHT = Gdx.graphics.getHeight();

    private Slider volumeSlider;
    private final Stage stage;

    public OptionsScreen(Main game) {
        this.game = game;

        stage = new Stage();
        Image backgroundImage = new Image();
        backgroundImage.setDrawable(new TextureRegionDrawable(new Texture("MainMenuScreen/background.png")));
        backgroundImage.setBounds(0, 0, WIDTH, HEIGHT);
        stage.addActor(backgroundImage);

        initButtons();
    }

    private void initButtons() {

        Gdx.input.setInputProcessor(stage);

        float buttonSize = WIDTH / 15;
        Skin buttonSkin = new Skin();
        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);

        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        BitmapFont pointsFont = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        titleFont.getData().setScale(3, 3);
        pointsFont.getData().setScale(2.1f, 2.5f);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        Label title = new Label(Main.title, titleStyle);
        title.setPosition(WIDTH / 2 - title.getWidth() / 2, HEIGHT / 6 * 5);
        stage.addActor(title);

        Label.LabelStyle volumeLabelStyle = new Label.LabelStyle();
        volumeLabelStyle.font = pointsFont;
        Label volumeLabel = new Label("Volume", volumeLabelStyle);
        volumeLabel.setPosition(WIDTH / 2 - volumeLabel.getWidth() / 2, HEIGHT / 5 * 2);
        stage.addActor(volumeLabel);

        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = pointsFont;
        backButtonStyle.up = buttonSkin.getDrawable("backIdle");
        backButtonStyle.down = buttonSkin.getDrawable("backPressed");
        TextButton backButton = new TextButton("", backButtonStyle);
        backButton.setSize(buttonSize, buttonSize);
        backButton.setPosition(10, 10);
        stage.addActor(backButton);

        TextButton.TextButtonStyle noVolumeButtonStyle = new TextButton.TextButtonStyle();
        noVolumeButtonStyle.font = pointsFont;
        noVolumeButtonStyle.up = buttonSkin.getDrawable("noVolumeIdle");
        noVolumeButtonStyle.down = buttonSkin.getDrawable("noVolumePressed");
        TextButton noVolumeButton = new TextButton("", noVolumeButtonStyle);
        noVolumeButton.setSize(buttonSize, buttonSize);
        noVolumeButton.setPosition(WIDTH / 5, HEIGHT / 5);
        stage.addActor(noVolumeButton);

        TextButton.TextButtonStyle fullVolumeButtonStyle = new TextButton.TextButtonStyle();
        fullVolumeButtonStyle.font = pointsFont;
        fullVolumeButtonStyle.up = buttonSkin.getDrawable("fullVolumeIdle");
        fullVolumeButtonStyle.down = buttonSkin.getDrawable("fullVolumePressed");
        TextButton fullVolumeButton = new TextButton("", fullVolumeButtonStyle);
        fullVolumeButton.setSize(buttonSize, buttonSize);
        fullVolumeButton.setPosition(WIDTH / 5 * 4 - fullVolumeButton.getWidth(), HEIGHT / 5);
        stage.addActor(fullVolumeButton);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                if (Main.playSounds)
                    Main.buttonSound.play();
            }
        });

        noVolumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.playSounds = false;
            }
        });

        fullVolumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.playSounds = true;
                Main.buttonSound.play();
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
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

    }
}
