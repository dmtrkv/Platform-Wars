package com.mygdx.game.Sprites.Fighters;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.PlayScreen;

public class King extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion kingIdleDefault;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKING}

    public static State currentState;
    public static State previousState;
    private Animation<TextureRegion> kingRun;
    private Animation<TextureRegion> kingJump;
    private Animation<TextureRegion> kingIdle;
    private Animation<TextureRegion> kingFall;
    private Animation<TextureRegion> kingAttack;
    private float stateTimer;
    private boolean runningRight;
    private float attackFrame;


    public King(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("RunAndJump"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        attackFrame = 0;

        initAnimations();
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public void initAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // run animation
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(), i * 160, 0, 160, 111));
        }
        kingRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // jump animation
        for (int i = 8; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 160, 0, 160, 111));
        }
        kingJump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // idle animation
        for (int i = 10; i < 18; i++) {
            frames.add(new TextureRegion(getTexture(), i * 160, 0, 160, 111));
        }
        kingIdle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // fall animation
        for (int i = 18; i < 20; i++) {
            frames.add(new TextureRegion(getTexture(), i * 160, 0, 160, 111));
        }
        kingFall = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // attack animation
        for (int i = 20; i < 26; i++) {
            frames.add(new TextureRegion(getTexture(), i * 160, 0, 160, 111));
        }
        kingAttack = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        kingIdleDefault = new TextureRegion(getTexture(), 0, 0, 160, 111);

        defineKing();
        setBounds(0, 0, 160 / Main.PPM, 160 / Main.PPM);
        setRegion(kingIdleDefault);
    }

    public static void attack() {
        previousState = State.ATTACKING;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState(dt);
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = kingJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = kingRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = kingFall.getKeyFrame(stateTimer, true);
                break;
            default:
                region = kingIdleDefault;
                break;
            case STANDING:
                region = kingIdle.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = kingAttack.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public static boolean canJump() {
        return currentState != State.FALLING && currentState != State.JUMPING && currentState != State.ATTACKING;
    }

    private State getState(float dt) {
        if (previousState == State.ATTACKING) {
            if (attackFrame > dt * 36) {
                attackFrame = 0;
                return State.STANDING;
            } else {
                attackFrame = attackFrame + dt;
                return State.ATTACKING;
            }
        } else if (b2body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void defineKing() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Main.PPM, 32 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-10 / Main.PPM, 34 / Main.PPM), new Vector2(10 / Main.PPM, 34 / Main.PPM));
        fdef.shape = head;
        b2body.createFixture(fdef).setUserData("head");

        EdgeShape left = new EdgeShape();
        left.set(new Vector2(-10 / Main.PPM, 34 / Main.PPM), new Vector2(-10 / Main.PPM, 0 / Main.PPM));
        fdef.shape = left;
        b2body.createFixture(fdef).setUserData("left");

        EdgeShape right = new EdgeShape();
        right.set(new Vector2(10 / Main.PPM, 34 / Main.PPM), new Vector2(10 / Main.PPM, 0 / Main.PPM));
        fdef.shape = right;
        b2body.createFixture(fdef).setUserData("right");
    }
}

