package com.mygdx.game.Multiplayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Screens.WaitingScreen;

public class ClientListener extends Listener {

    private Main game;
    private String fighter;
    private String map;
    private String secondPlayerFighter;

    public ClientListener(Main game, String fighter, String map) {
        this.game = game;
        this.fighter = fighter;
        this.map = map;
    }

    public void received(Connection connection, Object object) {
        if (object instanceof PacketMessage) {
            final PacketMessage message = (PacketMessage) object;
            Gdx.app.log("New Message on client: ", message.text);

            if (message.text.startsWith(Main.mapMessage)) {
                String[] mapAndFighter = message.text.replace("map: ", "").replace("fighter: ", "").split(" ");

                map = mapAndFighter[0];
                secondPlayerFighter = mapAndFighter[1];

                Gdx.app.log("Received map: ", map);
                Gdx.app.log("Received fighter: ", secondPlayerFighter);

                PacketMessage fighterResponse = new PacketMessage();
                fighterResponse.text = String.format("fighter: %s", fighter);
                connection.sendTCP(fighterResponse);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        WaitingScreen.startgame(game, "", secondPlayerFighter, map, Main.clientState);
                    }
                });
            }

            if (Main.actions.contains(message.text)) {
                Gdx.app.log("New Action: ", message.text);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        PlayScreen.updateSecondPlayerActions(message.text);
                    }
                });
            }
        }


    }
}
