package com.mygdx.game.Sprites.Fighters;


import com.badlogic.gdx.graphics.Texture;
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

public class Wizard extends Sprite {

    public World world;
    public Body b2body;
    public Body attack;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKING, TAKINGDAMAGE, DEAD}

    public State currentState;
    public State previousState;
    protected Animation<TextureRegion> Run;
    protected Animation<TextureRegion> Jump;
    protected Animation<TextureRegion> Idle;
    protected Animation<TextureRegion> Fall;
    protected Animation<TextureRegion> Attack;
    protected Animation<TextureRegion> TakeDamage;
    protected Animation<TextureRegion> Death;
    public int health;
    protected float stateTimer;
    public boolean runningRight;
    protected float attackFrame;
    protected float damageFrame;

    public Wizard(World world) {
        currentState = State.STANDING;
        previousState = State.STANDING;

        this.world = world;
        runningRight = true;

        stateTimer = 0;
        attackFrame = 0;

        health = 50;
        damageFrame = 0;
        initAnimations();
    }

    public void moveRight() {
        if (currentState != State.DEAD)
            b2body.applyLinearImpulse(new Vector2(0.15f, 0), b2body.getWorldCenter(), true);
    }

    public void moveLeft() {
        if (currentState != State.DEAD)
            b2body.applyLinearImpulse(new Vector2(-0.15f, 0), b2body.getWorldCenter(), true);
    }

    public void jump() {
        if (currentState != State.DEAD)
            if (canJump()) {
                b2body.applyLinearImpulse(new Vector2(0, 3.5f), b2body.getWorldCenter(), true);
            }
    }

    public void attack() {
        if (currentState != State.DEAD && currentState != State.ATTACKING && previousState != State.ATTACKING) {
            b2body.setLinearVelocity(0f, 0f);

            previousState = State.ATTACKING;

            BodyDef bdef = new BodyDef();
            bdef.position.set(b2body.getPosition().x, b2body.getPosition().y - 0.2f);
            bdef.type = BodyDef.BodyType.DynamicBody;
            attack = world.createBody(bdef);

            FixtureDef attackDef = new FixtureDef();
            attackDef.filter.categoryBits = Main.WIZARD_ATTACK_BIT;
            attackDef.filter.maskBits = Main.WARRIOR_BIT | Main.KING_BIT | Main.SAMURAI_BIT;
            attackDef.isSensor = true;

            EdgeShape attackShape = new EdgeShape();
            attack.setGravityScale(0.25f);

            if (runningRight) {
                attackShape.set(new Vector2(40 / Main.PPM, 34 / Main.PPM), new Vector2(0 / Main.PPM, 0 / Main.PPM));
            } else {
                attackShape.set(new Vector2(-40 / Main.PPM, 34 / Main.PPM), new Vector2(0 / Main.PPM, 0 / Main.PPM));
            }

            attackDef.shape = attackShape;
            attack.createFixture(attackDef);
        }
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public Animation<TextureRegion> createAnimation(String animation, int framesNum) {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < framesNum; i++) {
            frames.add(new TextureRegion(new Texture(
                    String.format("Fighters/Wizard/%s.png", animation)),
                    i * 231, 0, 226, 240));
        }

        return new Animation<TextureRegion>(0.1f, frames);
    }


    public void initAnimations() {
        Run = createAnimation("Run", 8);

        Jump = createAnimation("Jump", 2);

        Idle = createAnimation("Idle", 6);

        Fall = createAnimation("Fall", 2);

        Attack = createAnimation("Attack1", 8);

        TakeDamage = createAnimation("TakeDamage", 4);

        Death = createAnimation("Death", 8);

        define();
        setBounds(0, 0, 150 / Main.PPM, 130 / Main.PPM);
    }

    public void takeDamage() {
        if (currentState != State.DEAD) {
            previousState = State.TAKINGDAMAGE;
            health -= 10;
        }
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState(dt);
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = Jump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = Run.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = Fall.getKeyFrame(stateTimer, true);
                break;
            default:
            case STANDING:
                region = Idle.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = Attack.getKeyFrame(stateTimer, true);
                break;
            case TAKINGDAMAGE:
                region = TakeDamage.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = Death.getKeyFrame(stateTimer);
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

    public boolean canJump() {
        if ((currentState != State.FALLING) && (currentState != State.JUMPING)
                && (currentState != State.ATTACKING) && (currentState != State.DEAD)
                && (currentState != State.TAKINGDAMAGE)) {
            return true;
        }
        return false;
    }

    private State getState(float dt) {
        if (health == 0) {
            return State.DEAD;
        }
        if (previousState == State.TAKINGDAMAGE) {
            if (damageFrame > dt * 18) {
                damageFrame = 0;
                return State.STANDING;
            } else {
                damageFrame = damageFrame + dt;
                return State.TAKINGDAMAGE;
            }
        } else if (previousState == State.ATTACKING) {
            if (attackFrame > dt * 35) {
                attackFrame = 0;
                for (int i = 0; i < attack.getFixtureList().size; i++) {
                    attack.destroyFixture(attack.getFixtureList().get(i));
                }
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

    public void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / Main.PPM, 32 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);
        fdef.filter.categoryBits = Main.WIZARD_BIT;
        fdef.filter.maskBits = Main.DEFAULT_BIT | Main.BRICK_BIT | Main.SPIKE_BIT | Main.WARRIOR_ATTACK_BIT | Main.KING_ATTACK_BIT | Main.SAMURAI_ATTACK_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-10 / Main.PPM, 34 / Main.PPM), new Vector2(10 / Main.PPM, 34 / Main.PPM));
        fdef.shape = head;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape left = new EdgeShape();
        left.set(new Vector2(-10 / Main.PPM, 34 / Main.PPM), new Vector2(-10 / Main.PPM, 0 / Main.PPM));
        fdef.shape = left;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape right = new EdgeShape();
        right.set(new Vector2(10 / Main.PPM, 34 / Main.PPM), new Vector2(10 / Main.PPM, 0 / Main.PPM));
        fdef.shape = right;
        b2body.createFixture(fdef).setUserData(this);
    }
}
