package com.mygdx.game.Sprites.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

public class Spike extends InteractiveTileObject {
    public Spike(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Main.SPIKE_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Spike", "Collision");
        setCategoryFilter(Main.DESTROYED_BIT);
    }
}
