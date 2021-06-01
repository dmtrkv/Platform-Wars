package com.mygdx.game.Multiplayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.PlayScreen;

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
            Gdx.app.log("New Message: ", message.text);

            if (message.text == "new_player_event") {
                game.setScreen(new PlayScreen(game, fighter, map));

                PacketMessage mapResponse = new PacketMessage();
                mapResponse.text = String.format("map: %s", map);
                connection.sendTCP(mapResponse);

                PacketMessage eventResponse = new PacketMessage();
                eventResponse.text = "start_game_event";
                connection.sendTCP(eventResponse);
            }
        }
    }
}
