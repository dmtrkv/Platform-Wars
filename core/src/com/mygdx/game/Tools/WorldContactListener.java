package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Main;
import com.mygdx.game.Sprites.Fighters.Fighter;
import com.mygdx.game.Sprites.Fighters.Huntress;
import com.mygdx.game.Sprites.Fighters.King;
import com.mygdx.game.Sprites.Fighters.Samurai;
import com.mygdx.game.Sprites.Fighters.Warrior;
import com.mygdx.game.Sprites.Fighters.Wizard;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Main.HUNTRESS_ATTACK_BIT | Main.WARRIOR_BIT:
            case Main.WARRIOR_BIT | Main.WIZARD_ATTACK_BIT:
            case Main.KING_ATTACK_BIT | Main.WARRIOR_BIT:
            case Main.SAMURAI_ATTACK_BIT | Main.WARRIOR_BIT:
            case Main.WARRIOR_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.WARRIOR_BIT) {
                    ((Warrior) fixA.getUserData()).takeDamage();
                    ((Fighter) fixB.getUserData()).disableAttacks();
                } else {
                    ((Warrior) fixB.getUserData()).takeDamage();
                    ((Fighter) fixA.getUserData()).disableAttacks();
                }
                break;
            case Main.HUNTRESS_ATTACK_BIT | Main.SAMURAI_BIT:
            case Main.SAMURAI_BIT | Main.WIZARD_ATTACK_BIT:
            case Main.KING_ATTACK_BIT | Main.SAMURAI_BIT:
            case Main.WARRIOR_ATTACK_BIT | Main.SAMURAI_BIT:
            case Main.SAMURAI_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.SAMURAI_BIT) {
                    ((Samurai) fixA.getUserData()).takeDamage();
                    ((Fighter) fixB.getUserData()).disableAttacks();
                } else {
                    ((Samurai) fixB.getUserData()).takeDamage();
                    ((Fighter) fixA.getUserData()).disableAttacks();
                }
                break;
            case Main.HUNTRESS_ATTACK_BIT | Main.KING_BIT:
            case Main.KING_BIT | Main.WIZARD_ATTACK_BIT:
            case Main.KING_BIT | Main.SPIKE_BIT:
            case Main.SAMURAI_ATTACK_BIT | Main.KING_BIT:
            case Main.WARRIOR_ATTACK_BIT | Main.KING_BIT:
                if (fixA.getFilterData().categoryBits == Main.KING_BIT) {
                    ((King) fixA.getUserData()).takeDamage();
                    ((Fighter) fixB.getUserData()).disableAttacks();
                } else {
                    ((King) fixB.getUserData()).takeDamage();
                    ((Fighter) fixA.getUserData()).disableAttacks();
                }
                break;
            case Main.WIZARD_BIT | Main.HUNTRESS_ATTACK_BIT:
            case Main.WIZARD_BIT | Main.SPIKE_BIT:
            case Main.WIZARD_BIT | Main.SAMURAI_ATTACK_BIT:
            case Main.WIZARD_BIT | Main.WARRIOR_ATTACK_BIT:
            case Main.WIZARD_BIT | Main.KING_ATTACK_BIT:
                if (fixA.getFilterData().categoryBits == Main.WIZARD_BIT) {
                    ((Wizard) fixA.getUserData()).takeDamage();
                    ((Fighter) fixB.getUserData()).disableAttacks();
                } else {
                    ((Wizard) fixB.getUserData()).takeDamage();
                    ((Fighter) fixA.getUserData()).disableAttacks();
                }
                break;
            case Main.SAMURAI_ATTACK_BIT | Main.HUNTRESS_BIT:
            case Main.WARRIOR_ATTACK_BIT | Main.HUNTRESS_BIT:
            case Main.WIZARD_ATTACK_BIT | Main.HUNTRESS_BIT:
            case Main.KING_ATTACK_BIT | Main.HUNTRESS_BIT:
            case Main.SPIKE_BIT | Main.HUNTRESS_BIT:
                if (fixA.getFilterData().categoryBits == Main.HUNTRESS_BIT) {
                    ((Huntress) fixA.getUserData()).takeDamage();
                    ((Fighter) fixB.getUserData()).disableAttacks();
                } else {
                    ((Huntress) fixB.getUserData()).takeDamage();
                    ((Fighter) fixA.getUserData()).disableAttacks();
                }
                break;
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
