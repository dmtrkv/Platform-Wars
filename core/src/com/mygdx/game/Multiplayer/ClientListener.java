package com.mygdx.game.Multiplayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.PlayScreen;

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
            Gdx.app.log("New Message: ", message.text);

            if (message.text == "start_game_event") {
                game.setScreen(new PlayScreen(game, fighter, map));
            }

            if (message.text.startsWith("map: ")) {
                map = message.text.replace("map: ", "");
            }
        }
    }
}
