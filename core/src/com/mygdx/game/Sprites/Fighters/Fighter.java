package com.mygdx.game.Sprites.Fighters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

public abstract class Fighter extends Sprite {
    public World world;
    public Body b2body;
    public Body attack;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, ATTACKING, TAKINGDAMAGE, DEAD}

    public Wizard.State currentState;
    public Wizard.State previousState;
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
    protected int x;
    protected int y;

    protected abstract void takeDamage();

    protected TextureRegion getFrame(float dt) {
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
                region = Attack.getKeyFrame(stateTimer);
                break;
            case TAKINGDAMAGE:
                region = TakeDamage.getKeyFrame(stateTimer);
                break;
            case DEAD:
                region = Death.getKeyFrame(stateTimer);
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

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        currentState = getState(dt);
    }

    protected State getState(float dt) {
        if (health <= 0) {
            return State.DEAD;
        }
        if (currentState == State.TAKINGDAMAGE) {
            if (TakeDamage.isAnimationFinished(stateTimer)) {

            } else {
                return State.TAKINGDAMAGE;
            }
        }
        if (currentState == State.ATTACKING) {
            if (Attack.isAnimationFinished(stateTimer)) {
                for (int i = 0; i < attack.getFixtureList().size; i++) {
                    attack.destroyFixture(attack.getFixtureList().get(i));
                }
            } else {
                return State.ATTACKING;
            }
        }
        if (b2body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public boolean canJump() {
        if ((currentState != State.FALLING) && (currentState != State.JUMPING)
                && (currentState != State.ATTACKING) && (currentState != State.DEAD)
                && (currentState != State.TAKINGDAMAGE)) {
            return true;
        }
        return false;
    }

    public void disableAttacks() {
        Filter filter = new Filter();
        filter.categoryBits = Main.DESTROYED_BIT;

        for (int i = 0; i < attack.getFixtureList().size; i++) {
            attack.getFixtureList().get(i).setFilterData(filter);
        }
    }
}
