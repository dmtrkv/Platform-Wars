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

public class Samurai extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion samuraiIdleDefault;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKING}

    public static State currentState;
    public static State previousState;
    private Animation<TextureRegion> samuraiRun;
    private Animation<TextureRegion> samuraiJump;
    private Animation<TextureRegion> samuraiIdle;
    private Animation<TextureRegion> samuraiFall;
    private Animation<TextureRegion> samuraiAttack;
    private float stateTimer;
    private boolean runningRight;
    private float attackFrame;


    public Samurai(World world, PlayScreen screen) {
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
            frames.add(new TextureRegion(getTexture(), i * 200, 0, 200, 220));
        }
        samuraiRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // jump animation
        for (int i = 8; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 200, 0, 200, 220));
        }
        samuraiJump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // idle animation
        for (int i = 10; i < 18; i++) {
            frames.add(new TextureRegion(getTexture(), i * 200, 0, 200, 220));
        }
        samuraiIdle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // fall animation
        for (int i = 18; i < 20; i++) {
            frames.add(new TextureRegion(getTexture(), i * 200, 0, 200, 220));
        }
        samuraiFall = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // attack animation
        for (int i = 20; i < 26; i++) {
            frames.add(new TextureRegion(getTexture(), i * 200, 0, 200, 220));
        }
        samuraiAttack = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        samuraiIdleDefault = new TextureRegion(getTexture(), 0, 0, 200, 220);

        defineSamurai();
        setBounds(0, 0, 200 / Main.PPM, 200 / Main.PPM);
        setRegion(samuraiIdleDefault);
    }

    public static void attack() {
        previousState = State.ATTACKING;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState(dt);
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = samuraiJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = samuraiRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = samuraiFall.getKeyFrame(stateTimer, true);
                break;
            default:
                region = samuraiIdleDefault;
                break;
            case STANDING:
                region = samuraiIdle.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = samuraiAttack.getKeyFrame(stateTimer, true);
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

    public void defineSamurai() {
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
