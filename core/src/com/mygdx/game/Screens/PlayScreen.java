package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Fighters.King;
import com.mygdx.game.Sprites.Fighters.Samurai;
import com.mygdx.game.Sprites.Fighters.Warrior;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {

    private final Main game;

    private Stage stage;
    private TextButton moveLeftButton;
    private TextButton moveRightButton;
    private TextButton jumpButton;
    private TextButton attackButton;

    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final Hud hud;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world;
    private final Box2DDebugRenderer b2dr;

    private final Warrior player1;
    private final Samurai player2;
    private final King player3;

    public PlayScreen(Main game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);
        hud = new Hud(game.batch);

        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("map/map2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);
        player1 = new Warrior(world, this);
        player2 = new Samurai(world, this);
        player3 = new King(world, this);
        new B2WorldCreator(world, map);

        world.setContactListener(new WorldContactListener());

        initButtons();
    }

    public void initButtons() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        BitmapFont font = new BitmapFont();
        Skin button_skin = new Skin();

        TextureAtlas button_textureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        button_skin.addRegions(button_textureAtlas);

        TextButton.TextButtonStyle moveLeftButtonStyle = new TextButton.TextButtonStyle();
        moveLeftButtonStyle.font = font;
        moveLeftButtonStyle.up = button_skin.getDrawable("leftIdle"); //Не нажатая кнопка
        moveLeftButtonStyle.down = button_skin.getDrawable("leftPressed"); //Нажатая кнопка
        moveLeftButton = new TextButton("", moveLeftButtonStyle);
        moveLeftButton.setSize(100, 100); //Размер кнопки, скорее всего надо изменить
        moveLeftButton.setPosition(10, 10);


        TextButton.TextButtonStyle moveRightButtonStyle = new TextButton.TextButtonStyle();
        moveRightButtonStyle.font = font;
        moveRightButtonStyle.up = button_skin.getDrawable("rightIdle"); //Не нажатая кнопка
        moveRightButtonStyle.down = button_skin.getDrawable("rightPressed"); //Нажатая кнопка
        moveRightButton = new TextButton("", moveRightButtonStyle);
        moveRightButton.setSize(100, 100); //Размер кнопки, скорее всего надо изменить
        moveRightButton.setPosition(120, 10);

        TextButton.TextButtonStyle jumpButtonStyle = new TextButton.TextButtonStyle();
        jumpButtonStyle.font = font;
        jumpButtonStyle.up = button_skin.getDrawable("jumpIdle"); //Не нажатая кнопка
        jumpButtonStyle.down = button_skin.getDrawable("jumpPressed"); //Нажатая кнопка
        jumpButton = new TextButton("", jumpButtonStyle);
        jumpButton.setSize(100, 100);
        jumpButton.setPosition(Gdx.graphics.getWidth() - 220, 10);

        TextButton.TextButtonStyle attackButtonStyle = new TextButton.TextButtonStyle();
        attackButtonStyle.font = font;
        attackButtonStyle.up = button_skin.getDrawable("attackIdle"); //Не нажатая кнопка
        attackButtonStyle.down = button_skin.getDrawable("attackPressed"); //Нажатая кнопка
        attackButton = new TextButton("", attackButtonStyle);
        attackButton.setSize(100, 100);
        attackButton.setPosition(Gdx.graphics.getWidth() - 110, 10);

        stage.addActor(moveLeftButton);
        stage.addActor(moveRightButton);
        stage.addActor(jumpButton);
        stage.addActor(attackButton);

        jumpButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player1.jump();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        attackButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player1.attack();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player1.jump();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player1.b2body.getLinearVelocity().x <= 2) {
            player1.moveRight();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player1.b2body.getLinearVelocity().x >= -2) {
            player1.moveLeft();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            player1.attack();
        }

        if (moveRightButton.isPressed()) {
            player1.moveRight();
        }

        if (moveLeftButton.isPressed()) {
            player1.moveLeft();
        }
    }

    public void update(float dt) {
        handleInput(dt);
        player1.update(dt);
        player2.update(dt);
        player3.update(dt);
        world.step(1 / 60f, 6, 2);

        if (player1.b2body.getPosition().x > 2 && player1.b2body.getPosition().x < 6) {
            gameCam.position.x = player1.b2body.getPosition().x;
        }

        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();
        player1.draw(game.batch);
        player2.draw(game.batch);
        player3.draw(game.batch);
        game.batch.end();

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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}