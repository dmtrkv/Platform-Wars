package com.mygdx.game.Multiplayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.WaitingScreen;

public class ClientListener extends Listener {

    private Main game;
    private String fighter;
    private String map;

    public ClientListener(Main game, String fighter, String map) {
        this.game = game;
        this.fighter = fighter;
        this.map = map;
    }

    public void received(Connection connection, Object object) {
        if (object instanceof PacketMessage) {
            PacketMessage message = (PacketMessage) object;
            Gdx.app.log("New Message on client: ", message.text);

            if (message.text.startsWith(Main.mapMessage)) {
                map = message.text.replace("map: ", "");
                Gdx.app.log("Received map: ", map);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        WaitingScreen.startgame(game, fighter, map);
                    }
                });
                PacketMessage fighterResponse = new PacketMessage();
                fighterResponse.text = String.format("fighter: %s", fighter);
                connection.sendTCP(fighterResponse);
            }
        }
    }
}
