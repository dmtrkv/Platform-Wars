package com.mygdx.game.Sprites.Fighters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

import java.lang.management.PlatformLoggingMXBean;

public abstract class Fighter extends Sprite {
    public World world;
    public Body b2body;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKING, TAKINGDAMAGE}

    public static State currentState;
    public static State previousState;
    protected Animation<TextureRegion> Run;
    protected Animation<TextureRegion> Jump;
    protected Animation<TextureRegion> Idle;
    protected Animation<TextureRegion> Fall;
    protected Animation<TextureRegion> Attack;
    protected Animation<TextureRegion> TakeDamage;
    public int health;
    protected float stateTimer;
    public boolean runningRight;
    protected float attackFrame;
    protected float damageFrame;

    public abstract void onSpikeHeat();
    public void a() {}

}
