package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;

public class ChooseMapScreen implements Screen {

    private Main game;
    private final Viewport gamePort;
    private final OrthographicCamera gameCam;
    private final Stage stage;
    private TextButton startGameButton;
    private TextButton previousFighterButton;
    private TextButton nextFighterButton;
    private float WIDTH = Gdx.graphics.getWidth();
    private float HEIGHT = Gdx.graphics.getHeight();
    private Skin buttonSkin;
    private TextureAtlas buttonTextureAtlas;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Image fighterImage;
    private String currentFighter;
    private TextButton.TextButtonStyle previousFighterButtonStyle;
    private TextButton.TextButtonStyle nextFighterButtonStyle;
    private TextButton.TextButtonStyle startGameButtonStyle;

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
        initButtons();
    }

    private void initButtons() {
        previousFighterButtonStyle = new TextButton.TextButtonStyle();
        previousFighterButtonStyle.font = font;
        previousFighterButtonStyle.up = buttonSkin.getDrawable("leftIdle");
        previousFighterButtonStyle.down = buttonSkin.getDrawable("leftPressed");
        previousFighterButton = new TextButton("", previousFighterButtonStyle);
        previousFighterButton.setSize(150, 100);
        previousFighterButton.setPosition(WIDTH / 5 - previousFighterButton.getWidth() / 2,
                HEIGHT / 2 - 200 - previousFighterButton.getHeight() / 2);
        stage.addActor(previousFighterButton);

        nextFighterButtonStyle = new TextButton.TextButtonStyle();
        nextFighterButtonStyle.font = font;
        nextFighterButtonStyle.up = buttonSkin.getDrawable("rightIdle");
        nextFighterButtonStyle.down = buttonSkin.getDrawable("rightPressed");
        nextFighterButton = new TextButton("", nextFighterButtonStyle);
        nextFighterButton.setSize(150, 100);
        nextFighterButton.setPosition(WIDTH - WIDTH / 5 - previousFighterButton.getWidth() / 2,
                HEIGHT / 2 - 200 - previousFighterButton.getHeight() / 2);
        stage.addActor(nextFighterButton);

        startGameButtonStyle = new TextButton.TextButtonStyle();
        startGameButtonStyle.font = font;
        startGameButtonStyle.up = buttonSkin.getDrawable("playIdle");
        startGameButtonStyle.down = buttonSkin.getDrawable("playPressed");
        startGameButton = new TextButton("", startGameButtonStyle);
        startGameButton.setSize(150, 100);
        startGameButton.setPosition(WIDTH / 2 - startGameButton.getWidth() / 2, 50);
        stage.addActor(startGameButton);

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
