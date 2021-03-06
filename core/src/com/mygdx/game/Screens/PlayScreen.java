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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Main;

import com.mygdx.game.Multiplayer.PacketMessage;
import com.mygdx.game.Sprites.Fighters.Huntress;
import com.mygdx.game.Sprites.Fighters.King;
import com.mygdx.game.Sprites.Fighters.Samurai;
import com.mygdx.game.Sprites.Fighters.Warrior;
import com.mygdx.game.Sprites.Fighters.Wizard;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {

    private static Main game;

    private Stage stage;
    private TextButton moveLeftButton;
    private TextButton moveRightButton;

    private Label firstPlayerHealthLabel;
    private Label secondPlayerHealthLabel;

    private final OrthographicCamera gameCam;
    private final Viewport gamePort;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final float WIDTH = Gdx.graphics.getWidth();
    private final float HEIGHT = Gdx.graphics.getHeight();

    private static World world;
    private final Box2DDebugRenderer b2dr;

    private static King king;
    private static Samurai samurai;
    private static Warrior warrior;
    private static Wizard wizard;
    private static Huntress huntress;
    private final String firstPlayerFighter;
    private static String secondPlayerFighter;

    private final Server server;
    private final Client client;
    private final String state;

    private int x1;
    private int x2;

    public PlayScreen(Main game, String firstPlayerFighter, String CsecondPlayerFighter, String mapName, Server server, Client client, String state) {

        PlayScreen.game = game;
        this.server = server;
        this.client = client;
        this.state = state;

        Gdx.app.log("State: ", state);

        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);

        this.firstPlayerFighter = firstPlayerFighter;
        secondPlayerFighter = CsecondPlayerFighter;
        Gdx.app.log("Fighter", firstPlayerFighter);
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(String.format("map/%s.tmx", mapName));

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);

        int y1 = 32;
        int y2 = 32;

        if (state.equals(Main.serverState)) {
            x1 = 20;
            x2 = 780;
        } else if (state.equals(Main.clientState)) {
            x1 = 780;
            x2 = 20;
            gameCam.position.x = 6;
        }

        initFirstPlayer(x1, y1);
        initSecondPlayer(x2, y2);
        new B2WorldCreator(world, map);

        world.setContactListener(new WorldContactListener());

        init();
    }

    private void initSecondPlayer(int x, int y) {
        switch (secondPlayerFighter) {
            case "King":
                king = new King(world, x, y);
                break;
            case "Samurai":
                samurai = new Samurai(world, x, y);
                break;
            case "Warrior":
                warrior = new Warrior(world, x, y);
                break;
            case "Wizard":
                wizard = new Wizard(world, x, y);
                break;
            case "Huntress":
                huntress = new Huntress(world,x , y);
                break;
        }
    }

    private void initFirstPlayer(int x, int y) {
        switch (firstPlayerFighter) {
            case "King":
                king = new King(world, x, y);
                break;
            case "Samurai":
                samurai = new Samurai(world, x, y);
                break;
            case "Warrior":
                warrior = new Warrior(world, x, y);
                break;
            case "Wizard":
                wizard = new Wizard(world, x, y);
                break;
            case "Huntress":
                huntress = new Huntress(world, x, y);
                break;
        }
    }

    public void init() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        BitmapFont font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
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
        moveRightButtonStyle.up = button_skin.getDrawable("rightIdle"); //???? ?????????????? ????????????
        moveRightButtonStyle.down = button_skin.getDrawable("rightPressed"); //?????????????? ????????????
        moveRightButton = new TextButton("", moveRightButtonStyle);
        moveRightButton.setSize(buttonSize, buttonSize); //???????????? ????????????, ???????????? ?????????? ???????? ????????????????
        moveRightButton.setPosition(buttonSize + 10, 10);

        final TextButton.TextButtonStyle jumpButtonStyle = new TextButton.TextButtonStyle();
        jumpButtonStyle.font = font;
        jumpButtonStyle.up = button_skin.getDrawable("jumpIdle"); //???? ?????????????? ????????????
        jumpButtonStyle.down = button_skin.getDrawable("jumpPressed"); //?????????????? ????????????
        TextButton jumpButton = new TextButton("", jumpButtonStyle);
        jumpButton.setSize(buttonSize, buttonSize);
        jumpButton.setPosition(Gdx.graphics.getWidth() - buttonSize * 2 - 10, 10);

        TextButton.TextButtonStyle attackButtonStyle = new TextButton.TextButtonStyle();
        attackButtonStyle.font = font;
        attackButtonStyle.up = button_skin.getDrawable("attackIdle"); //???? ?????????????? ????????????
        attackButtonStyle.down = button_skin.getDrawable("attackPressed"); //?????????????? ????????????
        TextButton attackButton = new TextButton("", attackButtonStyle);
        attackButton.setSize(buttonSize, buttonSize);
        attackButton.setPosition(Gdx.graphics.getWidth() - buttonSize - 10, 10);

        stage.addActor(moveLeftButton);
        stage.addActor(moveRightButton);
        stage.addActor(jumpButton);
        stage.addActor(attackButton);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label firstPlayerLabel = new Label(firstPlayerFighter, labelStyle);
        firstPlayerLabel.setPosition(10, HEIGHT - 50);
        Label secondPlayerLabel = new Label(secondPlayerFighter, labelStyle);
        secondPlayerLabel.setPosition(10, HEIGHT - 70 - firstPlayerLabel.getHeight());

        firstPlayerHealthLabel = new Label(getFighterHealth(firstPlayerFighter), labelStyle);
        firstPlayerHealthLabel.setPosition(20 + firstPlayerLabel.getWidth(), HEIGHT - 50);
        secondPlayerHealthLabel = new Label(getFighterHealth(secondPlayerFighter), labelStyle);
        secondPlayerHealthLabel.setPosition(20 + secondPlayerLabel.getWidth(), HEIGHT - 70 - firstPlayerLabel.getHeight());

        stage.addActor(firstPlayerLabel);
        stage.addActor(secondPlayerLabel);
        stage.addActor(firstPlayerHealthLabel);
        stage.addActor(secondPlayerHealthLabel);

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
                sendActionUdp(Main.jumpAction, state);
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
                sendActionUdp(Main.attackAction, state);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private String getFighterHealth(String fighter) {
        switch (fighter) {
            case "King":
                return Integer.toString(king.health);
            case "Samurai":
                return Integer.toString(samurai.health);
            case "Warrior":
                return Integer.toString(warrior.health);
            case "Wizard":
                return Integer.toString(wizard.health);
            case "Huntress":
                return Integer.toString(huntress.health);
        }
        return "";
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        switch (firstPlayerFighter) {
            case "King":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    king.jump();
                    sendActionUdp(Main.jumpAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && king.b2body.getLinearVelocity().x <= 2) {
                    king.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && king.b2body.getLinearVelocity().x >= -2) {
                    king.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                if (moveRightButton.isPressed() && king.b2body.getLinearVelocity().x <= 2) {
                    king.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (moveLeftButton.isPressed() && king.b2body.getLinearVelocity().x >= -2) {
                    king.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                break;

            case "Samurai":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    samurai.jump();
                    sendActionUdp(Main.jumpAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && samurai.b2body.getLinearVelocity().x <= 2) {
                    samurai.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && samurai.b2body.getLinearVelocity().x >= -2) {
                    samurai.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                if (moveRightButton.isPressed() && samurai.b2body.getLinearVelocity().x <= 2) {
                    samurai.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (moveLeftButton.isPressed() && samurai.b2body.getLinearVelocity().x >= -2) {
                    samurai.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                break;

            case "Warrior":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    warrior.jump();
                    sendActionUdp(Main.jumpAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && warrior.b2body.getLinearVelocity().x <= 2) {
                    warrior.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && warrior.b2body.getLinearVelocity().x >= -2) {
                    warrior.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                if (moveRightButton.isPressed() && warrior.b2body.getLinearVelocity().x <= 2) {
                    warrior.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (moveLeftButton.isPressed() && warrior.b2body.getLinearVelocity().x >= -2) {
                    warrior.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                break;
            case "Wizard":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    wizard.jump();
                    sendActionUdp(Main.jumpAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && wizard.b2body.getLinearVelocity().x <= 2) {
                    wizard.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && wizard.b2body.getLinearVelocity().x >= -2) {
                    wizard.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                if (moveRightButton.isPressed() && wizard.b2body.getLinearVelocity().x <= 2) {
                    wizard.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (moveLeftButton.isPressed() && wizard.b2body.getLinearVelocity().x >= -2) {
                    wizard.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                break;
            case "Huntress":
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    huntress.jump();
                    sendActionUdp(Main.jumpAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D) && huntress.b2body.getLinearVelocity().x <= 2) {
                    huntress.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) && huntress.b2body.getLinearVelocity().x >= -2) {
                    huntress.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                if (moveRightButton.isPressed() && huntress.b2body.getLinearVelocity().x <= 2) {
                    huntress.moveRight();
                    sendActionUdp(Main.runRightAction, state);
                }
                if (moveLeftButton.isPressed() && huntress.b2body.getLinearVelocity().x >= -2) {
                    huntress.moveLeft();
                    sendActionUdp(Main.runLeftAction, state);
                }
                break;
        }
    }

    private void sendActionUdp(String action, String state) {
        PacketMessage actionMessage = new PacketMessage();
        actionMessage.text = action;
        if (state.equals("server")) {
            server.sendToAllUDP(actionMessage);
        } else if (state.equals("client")) {
            client.sendUDP(actionMessage);
        }
    }

    public void update(float dt) {
        handleInput(dt);

        updateFirstPlayer(dt);
        updateFighterHealth();
        try {
            updateSecondPlayer(dt);
        } catch (Exception ignored) {

        }

        world.step(1 / 60f, 6, 2);

        updateFirstPlayerCamera(dt);

        gameCam.update();
        renderer.setView(gameCam);
    }

    private void updateFighterHealth() {
        String firstPlayerHealth = getFighterHealth(firstPlayerFighter);
        String secondPlayerHealth = getFighterHealth(secondPlayerFighter);

        firstPlayerHealthLabel.setText(firstPlayerHealth);
        secondPlayerHealthLabel.setText(secondPlayerHealth);

        if (Integer.parseInt(firstPlayerHealth) <= 0) {
            PacketMessage winnerMessage = new PacketMessage();
            winnerMessage.text = String.format("winner: %s", secondPlayerFighter);
            if (state.equals(Main.serverState)) {
                server.sendToAllTCP(winnerMessage);
            } else if (state.equals(Main.clientState)) {
                client.sendTCP(winnerMessage);
            }
            setWinnerScreen(secondPlayerFighter);
        }

        if (Integer.parseInt(secondPlayerHealth) <= 0) {
            PacketMessage winnerMessage = new PacketMessage();
            winnerMessage.text = String.format("winner: %s", firstPlayerFighter);
            if (state.equals(Main.serverState)) {
                server.sendToAllTCP(winnerMessage);
            } else if (state.equals(Main.clientState)) {
                client.sendTCP(winnerMessage);
            }
            setWinnerScreen(firstPlayerFighter);
        }
    }

    public static void setWinnerScreen(String winner) {
        game.setScreen(new WinnerScreen(game, winner));
    }

    public static void updateSecondPlayerActions(String action) {
        switch (action) {
            case Main.attackAction:
                switch (secondPlayerFighter) {
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
                break;
            case Main.jumpAction:
                switch (secondPlayerFighter) {
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
                break;
            case Main.runRightAction:
                switch (secondPlayerFighter) {
                    case "King":
                        if (king.b2body.getLinearVelocity().x <= 2) {
                            king.moveRight();
                        }
                        break;
                    case "Samurai":
                        if (samurai.b2body.getLinearVelocity().x <= 2) {
                            samurai.moveRight();
                        }
                        break;
                    case "Warrior":
                        if (warrior.b2body.getLinearVelocity().x <= 2) {
                            warrior.moveRight();
                        }
                        break;
                    case "Wizard":
                        if (wizard.b2body.getLinearVelocity().x <= 2) {
                            wizard.moveRight();
                        }
                        break;
                    case "Huntress":
                        if (huntress.b2body.getLinearVelocity().x <= 2) {
                            huntress.moveRight();
                        }
                        break;
                }
                break;
            case Main.runLeftAction:
                switch (secondPlayerFighter) {
                    case "King":
                        if (king.b2body.getLinearVelocity().x >= -2) {
                            king.moveLeft();
                        }
                        break;
                    case "Samurai":
                        if (samurai.b2body.getLinearVelocity().x >= -2) {
                            samurai.moveLeft();
                        }
                        break;
                    case "Warrior":
                        if (warrior.b2body.getLinearVelocity().x >= -2) {
                            warrior.moveLeft();
                        }
                        break;
                    case "Wizard":
                        if (wizard.b2body.getLinearVelocity().x >= -2) {
                            wizard.moveLeft();
                        }
                        break;
                    case "Huntress":
                        if (huntress.b2body.getLinearVelocity().x >= -2) {
                            huntress.moveLeft();
                        }
                        break;
                }
                break;
        }
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

    private void updateSecondPlayer(float dt) {
        switch (secondPlayerFighter) {
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
        try {
            drawSecondPlayer();
//            Gdx.app.log("Second player: ", secondPlayerFighter);
        } catch (Exception ignored) {
//            Gdx.app.log("Second player: ", secondPlayerFighter);
        }

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
    public void pause() {}

    @Override
    public void resume() {}

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
