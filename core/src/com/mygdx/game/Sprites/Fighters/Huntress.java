package com.mygdx.game.Sprites.Fighters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;

public class Huntress extends Fighter {

    public Huntress(World world) {
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

    private void initAnimations() {
        Run = createAnimation("Run", 8);

        Jump = createAnimation("Jump", 2);

        Idle = createAnimation("Idle", 8);

        Fall = createAnimation("Fall", 2);

        Attack = createAnimation("Attack3", 8);

        TakeDamage = createAnimation("TakeDamage", 3);

        Death = createAnimation("Death", 8);

        define();
        setBounds(0, 0, 150 / Main.PPM, 150 / Main.PPM);
    }

    private void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(20 / Main.PPM, 32 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9 / Main.PPM);
        fdef.filter.categoryBits = Main.HUNTRESS_BIT;
        fdef.filter.maskBits = Main.DEFAULT_BIT | Main.BRICK_BIT | Main.SPIKE_BIT | Main.SAMURAI_ATTACK_BIT | Main.KING_ATTACK_BIT | Main.WIZARD_ATTACK_BIT | Main.WARRIOR_ATTACK_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-9 / Main.PPM, 30 / Main.PPM), new Vector2(9 / Main.PPM, 30 / Main.PPM));
        fdef.shape = head;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape left = new EdgeShape();
        left.set(new Vector2(-9 / Main.PPM, 30 / Main.PPM), new Vector2(-9 / Main.PPM, 0 / Main.PPM));
        fdef.shape = left;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape right = new EdgeShape();
        right.set(new Vector2(9 / Main.PPM, 30 / Main.PPM), new Vector2(9 / Main.PPM, 0 / Main.PPM));
        fdef.shape = right;
        b2body.createFixture(fdef).setUserData(this);
    }

    public Animation<TextureRegion> createAnimation(String animation, int framesNum) {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < framesNum; i++) {
            frames.add(new TextureRegion(new Texture(
                    String.format("Fighters/Huntress/%s.png", animation)),
                    i * 150, 10, 150, 150));
        }

        return new Animation<TextureRegion>(0.1f, frames);
    }

    @Override
    public void takeDamage() {
        if (currentState != State.DEAD) {
            previousState = State.TAKINGDAMAGE;
            health -= 10;
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
            attackDef.filter.categoryBits = Main.HUNTRESS_ATTACK_BIT;
            attackDef.filter.maskBits = Main.SAMURAI_BIT | Main.KING_BIT | Main.WIZARD_BIT | Main.WARRIOR_BIT;
            attackDef.isSensor = true;

            EdgeShape attackShape = new EdgeShape();
            attack.setGravityScale(0.25f);


            if (runningRight) {
                attackShape.set(new Vector2(80 / Main.PPM, 34 / Main.PPM), new Vector2(10 / Main.PPM, 34 / Main.PPM));

            } else {
                attackShape.set(new Vector2(-80 / Main.PPM, 34 / Main.PPM), new Vector2(-10 / Main.PPM, 34 / Main.PPM));
            }

            attackDef.shape = attackShape;
            attack.createFixture(attackDef);
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

    public void moveRight() {
        if (currentState != State.DEAD && currentState != State.ATTACKING) {
            b2body.applyLinearImpulse(new Vector2(0.225f, 0), b2body.getWorldCenter(), true);
        }
    }

    public void moveLeft() {
        if (currentState != State.DEAD && currentState != State.ATTACKING) {
            b2body.applyLinearImpulse(new Vector2(-0.225f, 0), b2body.getWorldCenter(), true);
        }
    }

    public void jump() {
        if (currentState != State.DEAD)
            if (canJump()) {
                b2body.applyLinearImpulse(new Vector2(0, 3.8f), b2body.getWorldCenter(), true);
            }
    }
}
