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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;

import com.mygdx.game.Sprites.Fighters.Huntress;
import com.mygdx.game.Sprites.Fighters.King;
import com.mygdx.game.Sprites.Fighters.Samurai;
import com.mygdx.game.Sprites.Fighters.Warrior;
import com.mygdx.game.Sprites.Fighters.Wizard;
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

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private static World world;
    private final Box2DDebugRenderer b2dr;

    private static King king;
    private static Samurai samurai;
    private static Warrior warrior;
    private static Wizard wizard;
    private static Huntress huntress;
    private String firstPlayerFighter;
    private String secondPlayerFighter;


    public PlayScreen(Main game, String firstPlayerFighter, String mapName) {

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);

        this.firstPlayerFighter = firstPlayerFighter;
        Gdx.app.log("Fighter", firstPlayerFighter);
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(String.format("map/%s.tmx", mapName));

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
//        b2dr.setDrawBodies(false);
        switch (firstPlayerFighter) {
            case "King":
                king = new King(world);
                break;
            case "Samurai":
                samurai = new Samurai(world);
                break;
            case "Warrior":
                warrior = new Warrior(world);
                break;
            case "Wizard":
                wizard = new Wizard(world);
                break;
            case "Huntress":
                huntress = new Huntress(world);
                break;
        }
        new B2WorldCreator(world, map);

        world.setContactListener(new WorldContactListener());

        initButtons();
    }

    public void initButtons() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        BitmapFont font = new BitmapFont();
        Skin button_skin = new Skin();

        float buttonSize = Gdx.graphics.getWidth() / 10f;

        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        button_skin.addRegions(buttonTextureAtlas);

        TextButton.TextButtonStyle moveLeftButtonStyle = new TextButton.TextButtonStyle();
        moveLeftButtonStyle.font = font;
        moveLeftButtonStyle.up = button_skin.getDrawable("leftIdle");
        moveLeftButtonStyle.down = button_skin.getDrawable("leftPressed");
        moveLeftButton = new TextButton("", moveLeftButtonStyle);
        moveLeftButton.setSize(buttonSize, buttonSize);
        moveLeftButton.setPosition(10, 10);


        TextButton.TextButtonStyle moveRightButtonStyle = new TextButton.TextButtonStyle();
        moveRightButtonStyle.font = font;
        moveRightButtonStyle.up = button_skin.getDrawable("rightIdle"); //Не нажатая кнопка
        moveRightButtonStyle.down = button_skin.getDrawable("rightPressed"); //Нажатая кнопка
        moveRightButton = new TextButton("", moveRightButtonStyle);
        moveRightButton.setSize(buttonSize, buttonSize); //Размер кнопки, скорее всего надо изменить
        moveRightButton.setPosition(buttonSize + 10, 10);

        TextButton.TextButtonStyle jumpButtonStyle = new TextButton.TextButtonStyle();
        jumpButtonStyle.font = font;
        jumpButtonStyle.up = button_skin.getDrawable("jumpIdle"); //Не нажатая кнопка
        jumpButtonStyle.down = button_skin.getDrawable("jumpPressed"); //Нажатая кнопка
        jumpButton = new TextButton("", jumpButtonStyle);
        jumpButton.setSize(buttonSize, buttonSize);
        jumpButton.setPosition(Gdx.graphics.getWidth() - buttonSize * 2 - 10, 10);

        TextButton.TextButtonStyle attackButtonStyle = new TextButton.TextButtonStyle();
        attackButtonStyle.font = font;
        attackButtonStyle.up = button_skin.getDrawable("attackIdle"); //Не нажатая кнопка
        attackButtonStyle.down = button_skin.getDrawable("attackPressed"); //Нажатая кнопка
        attackButton = new TextButton("", attackButtonStyle);
        attackButton.setSize(buttonSize, buttonSize);
        attackButton.setPosition(Gdx.graphics.getWidth() - buttonSize - 10, 10);

        stage.addActor(moveLeftButton);
        stage.addActor(moveRightButton);
        stage.addActor(jumpButton);
        stage.addActor(attackButton);

        jumpButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (firstPlayerFighter) {
                    case "King":
                        king.jump();
                        break;
                    case "Samurai":
                        samurai.jump();
                        break;
                    case "Warrior":
                        warrior.jump();
                        break;
                    case "Wizard":
                        wizard.jump();
                        break;
                    case "Huntress":
                        huntress.jump();
                        break;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        attackButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (firstPlayerFighter) {
                    case "King":
                        king.attack();
                        break;
                    case "Samurai":
                        samurai.attack();
                        break;
                    case "Warrior":
                        warrior.attack();
                        break;
                    case "Wizard":
                        wizard.attack();
                        break;
                    case "Huntress":
                        huntress.attack();
                        break;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {

        switch (firstPlayerFighter) {
            case "King":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    king.jump();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && king.b2body.getLinearVelocity().x <= 2) {
                    king.moveRight();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && king.b2body.getLinearVelocity().x >= -2) {
                    king.moveLeft();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                    king.attack();
                }
                if (moveRightButton.isPressed() && king.b2body.getLinearVelocity().x <= 2) {
                    king.moveRight();
                }
                if (moveLeftButton.isPressed() && king.b2body.getLinearVelocity().x >= -2) {
                    king.moveLeft();
                }
                break;

            case "Samurai":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    samurai.jump();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && samurai.b2body.getLinearVelocity().x <= 2) {
                    samurai.moveRight();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && samurai.b2body.getLinearVelocity().x >= -2) {
                    samurai.moveLeft();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                    samurai.attack();
                }
                if (moveRightButton.isPressed() && samurai.b2body.getLinearVelocity().x <= 2) {
                    samurai.moveRight();
                }
                if (moveLeftButton.isPressed() && samurai.b2body.getLinearVelocity().x >= -2) {
                    samurai.moveLeft();
                }
                break;

            case "Warrior":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    warrior.jump();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && warrior.b2body.getLinearVelocity().x <= 2) {
                    warrior.moveRight();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && warrior.b2body.getLinearVelocity().x >= -2) {
                    warrior.moveLeft();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                    warrior.attack();
                }
                if (moveRightButton.isPressed() && warrior.b2body.getLinearVelocity().x <= 2) {
                    warrior.moveRight();
                }
                if (moveLeftButton.isPressed() && warrior.b2body.getLinearVelocity().x >= -2) {
                    warrior.moveLeft();
                }
                break;
            case "Wizard":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    wizard.jump();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && wizard.b2body.getLinearVelocity().x <= 2) {
                    wizard.moveRight();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && wizard.b2body.getLinearVelocity().x >= -2) {
                    wizard.moveLeft();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                    wizard.attack();
                }
                if (moveRightButton.isPressed() && wizard.b2body.getLinearVelocity().x <= 2) {
                    wizard.moveRight();
                }
                if (moveLeftButton.isPressed() && wizard.b2body.getLinearVelocity().x >= -2) {
                    wizard.moveLeft();
                }
                break;
            case "Huntress":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    huntress.jump();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && huntress.b2body.getLinearVelocity().x <= 2) {
                    huntress.moveRight();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && huntress.b2body.getLinearVelocity().x >= -2) {
                    huntress.moveLeft();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                    huntress.attack();
                }
                if (moveRightButton.isPressed() && huntress.b2body.getLinearVelocity().x <= 2) {
                    huntress.moveRight();
                }
                if (moveLeftButton.isPressed() && huntress.b2body.getLinearVelocity().x >= -2) {
                    huntress.moveLeft();
                }
                break;
        }
    }

    public void update(float dt) {
        handleInput(dt);

        updateFirstPlayer(dt);

        world.step(1 / 60f, 6, 2);

        updateFirstPlayerCamera(dt);

        gameCam.update();
        renderer.setView(gameCam);
    }

    private void updateFirstPlayerCamera(float dt) {
        switch (firstPlayerFighter) {
            case "King":
                if (king.b2body.getPosition().x > 2 && king.b2body.getPosition().x < 6) {
                    gameCam.position.x = king.b2body.getPosition().x;
                }
                break;
            case "Samurai":
                if (samurai.b2body.getPosition().x > 2 && samurai.b2body.getPosition().x < 6) {
                    gameCam.position.x = samurai.b2body.getPosition().x;
                }
                break;
            case "Warrior":
                if (warrior.b2body.getPosition().x > 2 && warrior.b2body.getPosition().x < 6) {
                    gameCam.position.x = warrior.b2body.getPosition().x;
                }
                break;
            case "Wizard":
                if (wizard.b2body.getPosition().x > 2 && wizard.b2body.getPosition().x < 6) {
                    gameCam.position.x = wizard.b2body.getPosition().x;
                }
                break;
            case "Huntress":
                if (huntress.b2body.getPosition().x > 2 && huntress.b2body.getPosition().x < 6) {
                    gameCam.position.x = huntress.b2body.getPosition().x;
                }
                break;
        }
    }

    private void updateFirstPlayer(float dt) {
        switch (firstPlayerFighter) {
            case "King":
                king.update(dt);
                break;
            case "Samurai":
                samurai.update(dt);
                break;
            case "Warrior":
                warrior.update(dt);
                break;
            case "Wizard":
                wizard.update(dt);
                break;
            case "Huntress":
                huntress.update(dt);
                break;
        }
    }

    public static void initSecondPlayer(String secondfighter) {
        switch (secondfighter) {
            case "King":
                king = new King(world);
                break;
            case "Samurai":
                samurai = new Samurai(world);
                break;
            case "Warrior":
                warrior = new Warrior(world);
                break;
            case "Wizard":
                wizard = new Wizard(world);
                break;
            case "Huntress":
                huntress = new Huntress(world);
                break;
        }
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

        drawFirstPlayer();
        drawSecondPlayer();

        game.batch.end();
        stage.draw();
    }

    private void drawSecondPlayer() {
        switch (secondPlayerFighter) {
            case "King":
                king.draw(game.batch);
                break;
            case "Samurai":
                samurai.draw(game.batch);
                break;
            case "Warrior":
                warrior.draw(game.batch);
                break;
            case "Wizard":
                wizard.draw(game.batch);
                break;
            case "Huntress":
                huntress.draw(game.batch);
                break;
        }
    }

    private void drawFirstPlayer() {
        switch (firstPlayerFighter) {
            case "King":
                king.draw(game.batch);
                break;
            case "Samurai":
                samurai.draw(game.batch);
                break;
            case "Warrior":
                warrior.draw(game.batch);
                break;
            case "Wizard":
                wizard.draw(game.batch);
                break;
            case "Huntress":
                huntress.draw(game.batch);
                break;
        }
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
    }
}
