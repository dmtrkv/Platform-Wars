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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Main;
import com.mygdx.game.Multiplayer.ClientListener;
import com.mygdx.game.Multiplayer.PacketMessage;
import com.mygdx.game.Multiplayer.ServerListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class WaitingScreen implements Screen {

    private final Main game;
    private final String map;
    private final String fighter;

    private static final Server server = new Server();
    private static final Client client = new Client();
    private static String state;

    private final float WIDTH = Gdx.graphics.getWidth();
    private final float HEIGHT = Gdx.graphics.getHeight();

    private Stage stage;
    private Label label;
    private Label title;
    private TextButton backButton;

    public WaitingScreen(String Cstate, String map, String fighter, Main game) throws IOException {
        this.game = game;
        this.map = map;
        this.fighter = fighter;
        state = Cstate;

        init();

        if (state.equals(Main.serverState)) {
            createServer();
        } else if (state.equals(Main.clientState)) {
            createClient();
        }
    }

    private void init() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        font.getData().setScale(2.1f, 2.5f);
        titleFont.getData().setScale(3, 3);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        label = new Label("Waiting", labelStyle);
        label.setAlignment(Align.center);
        label.setWidth(WIDTH);

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Image backgroundImage = new Image();
        backgroundImage.setDrawable(new TextureRegionDrawable(new Texture("MainMenuScreen/background.png")));
        backgroundImage.setBounds(0, 0, WIDTH, HEIGHT);
        stage.addActor(backgroundImage);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        title = new Label(Main.title, titleStyle);
        title.setPosition(WIDTH / 2 - title.getWidth() / 2, HEIGHT / 6 * 5);
        stage.addActor(title);

        float buttonSize = WIDTH / 15;

        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = font;
        Skin buttonSkin = new Skin();
        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/Buttons.pack"));
        buttonSkin.addRegions(buttonTextureAtlas);
        backButtonStyle.up = buttonSkin.getDrawable("backIdle");
        backButtonStyle.down = buttonSkin.getDrawable("backPressed");
        backButton = new TextButton("", backButtonStyle);
        backButton.setSize(buttonSize, buttonSize);
        backButton.setPosition(10, 10);
        stage.addActor(backButton);

        label.setPosition(0, HEIGHT / 3 - label.getHeight() / 2);
        stage.addActor(label);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (state.equals(Main.clientState)) {
                    try {
                        client.stop();
                    } catch (Exception ignored) {
                    }
                    game.setScreen(new MainMenuScreen(game));
                } else if (state.equals(Main.serverState)) {
                    try {
                        server.stop();
                    } catch (Exception ignored) {
                    }
                    game.setScreen(new ChooseMapScreen(game));
                }
            }
        });

        preRender();
    }

    private void preRender() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    public static void startgame(Main cGame, String cFighter, String cFighter2, String cMap, String state) {
        if (state.equals(Main.serverState)) {
            PlayScreen playScreen = (new PlayScreen(cGame, cFighter, cFighter2, cMap, server, client, state));
            cGame.setScreen(playScreen);
        } else if (state.equals(Main.clientState)) {
            cGame.setScreen(new ChooseFighterScreen(cGame, cMap, state, cFighter2, client, server));
        }
    }

    private void createClient() throws IOException {
        client.getKryo().register(PacketMessage.class);
        client.start();
        client.addListener(new ClientListener(game, fighter, map));

        boolean connected = false;

        int c = 0;
        while (!connected) {
            if (c == 1) {
                noServersFound();
                break;
            }
            List<InetAddress> addressList = client.discoverHosts(Main.udpPort, 2000);
            System.out.println(addressList);
            for (int i = 0; i < addressList.size(); i++) {
                try {
                    client.connect(2000, addressList.get(i), Main.tcpPort, Main.udpPort);
                    connected = true;
                    break;
                } catch (Exception e) {
                    Gdx.app.log("Exception", " in connecting");
                }
            }
            c++;
        }

        PacketMessage request = new PacketMessage();
        request.text = Main.newPlayerEvent;
        client.sendTCP(request);
    }

    private void noServersFound() {
        label.setText("No games were found");
    }

    private void createServer() throws IOException {
        server.bind(Main.tcpPort, Main.udpPort);
        server.getKryo().register(PacketMessage.class);
        server.start();
        server.addListener(new ServerListener(game, fighter, map));
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
