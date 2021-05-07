package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Main;
import com.mygdx.game.Sprites.Fighters.King;
import com.mygdx.game.Sprites.Fighters.Samurai;
import com.mygdx.game.Sprites.Fighters.Warrior;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Main.WARRIOR_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.WARRIOR_BIT) {
                    ((Warrior) fixA.getUserData()).takeDamage();
                } else {
                    ((Warrior) fixB.getUserData()).takeDamage();
                }
            case Main.SAMURAI_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.WARRIOR_BIT) {
                    ((Samurai) fixA.getUserData()).takeDamage();
                } else {
                    ((Samurai) fixB.getUserData()).takeDamage();
                }
            case Main.KING_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.WARRIOR_BIT) {
                    ((King) fixA.getUserData()).takeDamage();
                } else {
                    ((King) fixB.getUserData()).takeDamage();
                }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
