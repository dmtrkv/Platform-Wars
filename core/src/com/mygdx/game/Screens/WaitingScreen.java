package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private Stage stage;
    private Label label;
    private final float WIDTH = Gdx.graphics.getWidth();
    private final float HEIGHT = Gdx.graphics.getHeight();
    private static String state;

    public WaitingScreen(String Cstate, String map, String fighter, Main game) throws IOException {
        this.game = game;
        this.map = map;
        this.fighter = fighter;
        state = Cstate;

        init();

        if (state.equals("server")) {
            createServer();
        } else if (state.equals("client")) {
            createClient();
        }
    }

    private void init() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
        font.getData().setScale(2);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        label = new Label("Waiting", labelStyle);
        label.setAlignment(Align.center);
        label.setWidth(WIDTH);

        stage = new Stage();

        label.setPosition(0, HEIGHT / 2 - label.getHeight() / 2);
        stage.addActor(label);

        preRender();
    }

    private void preRender() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    public static void startgame(Main cGame, String cFighter, String cMap) {
        PlayScreen playScreen = (new PlayScreen(cGame, cFighter, cMap, server, client, state));
        cGame.setScreen(playScreen);
    }

    private void createClient() throws IOException {
        client.getKryo().register(PacketMessage.class);
        client.start();
        client.addListener(new ClientListener(game, fighter, map));

        boolean connected = false;

        int c = 0;
        while (!connected) {
            if (c == 2) {
                noServersFound();
                c = 0;
                break;
            }
            List<InetAddress> addressList = client.discoverHosts(Main.udpPort, 3000);
            System.out.println(addressList);
            for (int i = 0; i < addressList.size(); i++) {
                try {
                    client.connect(3000, addressList.get(i), Main.tcpPort, Main.udpPort);
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
