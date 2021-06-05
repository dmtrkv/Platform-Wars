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
    private String secondPlayerFighter;

    public ServerListener(Main game, String fighter, String map) {
        this.game = game;
        this.fighter = fighter;
        this.map = map;
    }

    public void received(final Connection connection, Object object) {
        if (object instanceof PacketMessage) {
            final PacketMessage message = (PacketMessage) object;
            Gdx.app.log("New Message on server: ", message.text);

            if (message.text.equals(Main.newPlayerEvent)) {
                PacketMessage response = new PacketMessage();
                response.text = String.format("map: %s fighter: %s", map, fighter);

                connection.sendTCP(response);

                Gdx.app.log("Send a new message on client: ", response.text);
            }

            if (message.text.equals(Main.startGameEvent)) {
                Gdx.app.log("New Screen:", "PlayScreen");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        WaitingScreen.startgame(game, fighter, secondPlayerFighter, map, Main.serverState);
                    }
                });
            }

            if (message.text.startsWith(Main.fighterMessage)) {
                secondPlayerFighter = message.text.replace(Main.fighterMessage, "");
                Gdx.app.log("Second player fighter: ", secondPlayerFighter);
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
