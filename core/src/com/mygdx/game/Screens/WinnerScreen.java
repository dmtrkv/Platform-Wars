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

public class WinnerScreen implements Screen {

    private final Stage stage;
    private final Main game;

    private final float WIDTH = Gdx.graphics.getWidth();
    private final float HEIGHT = Gdx.graphics.getHeight();

    private final String winner;

    public WinnerScreen(Main game, String winner) {
        this.game = game;
        this.winner = winner;

        stage = new Stage();
        Image backgroundImage = new Image();
        backgroundImage.setDrawable(new TextureRegionDrawable(new Texture("MainMenuScreen/background.png")));
        backgroundImage.setBounds(0, 0, WIDTH, HEIGHT);
        stage.addActor(backgroundImage);

        init();
    }

    private void init() {
        Gdx.input.setInputProcessor(stage);

        BitmapFont font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        titleFont.getData().setScale(3, 3);
        font.getData().setScale(2.1f, 2.5f);

        Skin buttonSkin = new Skin();

        float buttonSize = WIDTH / 15;

        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);

        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = font;
        backButtonStyle.up = buttonSkin.getDrawable("backIdle");
        backButtonStyle.down = buttonSkin.getDrawable("backPressed");
        TextButton backButton = new TextButton("", backButtonStyle);
        backButton.setSize(buttonSize, buttonSize);
        backButton.setPosition(10, 10);
        stage.addActor(backButton);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        Label title = new Label(Main.title, titleStyle);
        title.setPosition(WIDTH / 2 - title.getWidth() / 2, HEIGHT / 6 * 5);
        stage.addActor(title);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        String winnerString = String.format("%s is Winner!", winner);
        Label winnerLabel = new Label(winnerString, labelStyle);
        winnerLabel.setPosition(WIDTH / 2 - winnerLabel.getWidth() / 2, HEIGHT / 5 * 2);
        stage.addActor(winnerLabel);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
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

    }
}
