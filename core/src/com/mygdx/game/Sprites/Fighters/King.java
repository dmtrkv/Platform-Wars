package com.mygdx.game.Sprites.Fighters;


import com.badlogic.gdx.Gdx;
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

public class King extends Fighter {

    public King(World world) {
        currentState = State.STANDING;
        previousState = State.STANDING;

        this.world = world;
        runningRight = true;

        stateTimer = 0;
        attackFrame = 0;

        health = 100;
        damageFrame = 0;
        initAnimations();
    }

    public void moveRight() {
        if (currentState != State.DEAD)
            b2body.applyLinearImpulse(new Vector2(0.225f, 0), b2body.getWorldCenter(), true);
    }

    public void moveLeft() {
        if (currentState != State.DEAD)
            b2body.applyLinearImpulse(new Vector2(-0.225f, 0), b2body.getWorldCenter(), true);
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
            attackDef.filter.categoryBits = Main.KING_ATTACK_BIT;
            attackDef.filter.maskBits = Main.SAMURAI_BIT | Main.WARRIOR_BIT | Main.WIZARD_BIT | Main.HUNTRESS_BIT;
            attackDef.isSensor = true;

            EdgeShape attackShape = new EdgeShape();attack.setGravityScale(0.25f);
            attack.setGravityScale(0.25f);

            attackShape.set(new Vector2(40 / Main.PPM, 34 / Main.PPM),
                    new Vector2(-40 / Main.PPM, 34 / Main.PPM));

            attackDef.shape = attackShape;
            attack.createFixture(attackDef);
        }
    }

    public Animation<TextureRegion> createAnimation(String animation, int framesNum) {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < framesNum; i++) {
            frames.add(new TextureRegion(new Texture(
                    String.format("Fighters/King/%s.png", animation)),
                    i * 160, 34, 160, 111));
        }

        return new Animation<>(0.1f, frames);
    }

    public void initAnimations() {
        Run = createAnimation("Run", 8);

        Jump = createAnimation("Jump", 2);

        Idle = createAnimation("Idle", 8);

        Fall = createAnimation("Fall", 2);

        Attack = createAnimation("Attack2", 4);

        TakeDamage = createAnimation("TakeDamage", 4);

        Death = createAnimation("Death", 6);

        define();
        setBounds(0, 0, 160 / Main.PPM, 90 / Main.PPM);
    }

    @Override
    public void takeDamage() {
        if (currentState != State.DEAD) {
            previousState = State.TAKINGDAMAGE;
            health -= 10;
        }
    }

    @Override
    protected State getState(float dt) {
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
            if (attackFrame > dt * 18) {
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

    public void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(40 / Main.PPM, 40 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / Main.PPM);
        fdef.filter.categoryBits = Main.KING_BIT;
        fdef.filter.maskBits = Main.DEFAULT_BIT | Main.BRICK_BIT | Main.SPIKE_BIT
                | Main.WARRIOR_ATTACK_BIT | Main.SAMURAI_ATTACK_BIT
                | Main.WIZARD_ATTACK_BIT | Main.HUNTRESS_ATTACK_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-12 / Main.PPM, 34 / Main.PPM), new Vector2(12 / Main.PPM, 34 / Main.PPM));
        fdef.shape = head;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape left = new EdgeShape();
        left.set(new Vector2(-12 / Main.PPM, 34 / Main.PPM), new Vector2(-12 / Main.PPM, 0 / Main.PPM));
        fdef.shape = left;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape right = new EdgeShape();
        right.set(new Vector2(12 / Main.PPM, 34 / Main.PPM), new Vector2(12 / Main.PPM, 0 / Main.PPM));
        fdef.shape = right;
        b2body.createFixture(fdef).setUserData(this);
    }
}

