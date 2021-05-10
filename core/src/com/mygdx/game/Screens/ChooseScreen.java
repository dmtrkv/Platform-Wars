package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;

public class ChooseScreen implements Screen {

    private Stage stage;
    private TextButton startGameButton;
    private Table fightersTable;

    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final Main game;

    public ChooseScreen(Main game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);

        initFightersTable();
    }

    private void initFightersTable() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        BitmapFont font = new BitmapFont();
        Skin button_skin = new Skin();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        button_skin.addRegions(buttonTextureAtlas);

        fightersTable = new Table();
        fightersTable.defaults().expand();
        fightersTable.setPosition(0, 0);
        fightersTable.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        Image samuraiImage = new Image(new Texture("ChooseScreen/Samurai.png"));
        samuraiImage.setScaleX(2);
        samuraiImage.setScaleY(2);
        Label samuraiPassiveSkill = new Label("Double Jump", labelStyle);
        fightersTable.add(new Label("SAMURAI", labelStyle)).expandX();
        fightersTable.row().padTop(10);
        fightersTable.add(samuraiImage).expandX();
        fightersTable.add(samuraiPassiveSkill).expandX();
        fightersTable.row().padTop(10);

        Image warriorImage = new Image(new Texture("ChooseScreen/Warrior.png"));
        warriorImage.setScaleX(2);
        warriorImage.setScaleY(2);
        Label warriorPassiveSkill = new Label("Enormous Speed", labelStyle);
        fightersTable.add(new Label("WARRIOR", labelStyle)).expandX();
        fightersTable.row();
        fightersTable.add(warriorImage).expandX();
        fightersTable.add(warriorPassiveSkill).expandX();
        fightersTable.row();

        Image kingImage = new Image(new Texture("ChooseScreen/King.png"));
        kingImage.setScaleX(2);
        samuraiImage.setScaleY(2);
        Label kingPassiveSkill = new Label("Double HP", labelStyle);
        fightersTable.add(new Label("KING", labelStyle)).expandX();
        fightersTable.row();
        fightersTable.add(kingImage).expandX();
        fightersTable.add(kingPassiveSkill).expandX();
        fightersTable.row();

        stage.addActor(fightersTable);
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
