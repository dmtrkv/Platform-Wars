package com.mygdx.game.Multiplayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Screens.WaitingScreen;

public class ServerListener extends Listener {

    private Main game;
    private String fighter;
    private String map;

    public ServerListener(Main game, String fighter, String map) {
        this.game = game;
        this.fighter = fighter;
        this.map = map;
    }

    public void received(Connection connection, Object object) {
        if (object instanceof PacketMessage) {
            PacketMessage message = (PacketMessage) object;
            Gdx.app.log("New Message on server: ", message.text);

            if (message.text.equals(Main.newPlayerEvent)) {
                Gdx.app.log("New screen: ", "PlayScreen");

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        WaitingScreen.startgame(game, fighter, map);
                    }
                });

                PacketMessage mapResponse = new PacketMessage();
                mapResponse.text = String.format("map: %s", map);
                connection.sendTCP(mapResponse);
                Gdx.app.log("Send a new message on client: ", mapResponse.text);
            }


            if (message.text.startsWith(Main.fighterMessage)) {
                final String secondPlayerFighter = message.text.replace(Main.fighterMessage, "");
                Gdx.app.log("Second player fighter: ", secondPlayerFighter);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        PlayScreen.initSecondPlayer(secondPlayerFighter);
                    }
                });
            }
        }
    }
}
