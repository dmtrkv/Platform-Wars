package com.mygdx.game.Sprites.Fighters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

public class Spear extends Sprite {

    private Body b2body;
    private World world;
    private float x;
    private float y;
    private boolean right;
    private Huntress huntress;

    public Spear(boolean right, World world, float x, float y, Huntress huntress) {
        this.world = world;
        setRegion(new Texture("Fighters/Huntress/Spear.png"));
        this.x = x;
        this.y = y;
        this.right = right;
        this.huntress = huntress;
        define();
    }

    private void define() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef attackDef = new FixtureDef();
        attackDef.filter.categoryBits = Main.HUNTRESS_ATTACK_BIT;
        attackDef.filter.maskBits = Main.SAMURAI_BIT | Main.KING_BIT | Main.WIZARD_BIT
                | Main.WARRIOR_BIT | Main.SPIKE_BIT | Main.BRICK_BIT
                | Main.DEFAULT_BIT | Main.DESTROYED_BIT;
        EdgeShape attackShape = new EdgeShape();
        b2body.setGravityScale(0);

        if (right) {
            attackShape.set(new Vector2(80 / Main.PPM, 34 / Main.PPM), new Vector2(10 / Main.PPM, 34 / Main.PPM));
            b2body.setLinearVelocity(0.1f, 0);
        } else {
            attackShape.set(new Vector2(-80 / Main.PPM, 34 / Main.PPM), new Vector2(-10 / Main.PPM, 34 / Main.PPM));
            b2body.setLinearVelocity(-0.1f, 0);
        }

        attackDef.shape = attackShape;
        b2body.createFixture(attackDef).setUserData(huntress);

        setBounds(0, 50, 40 / Main.PPM, 5 / Main.PPM);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void removeFixture() {
        for (int i = 0; i < b2body.getFixtureList().size; i++) {
            Filter filter = new Filter();
            filter.maskBits = Main.DESTROYED_BIT;
            b2body.getFixtureList().get(i).setFilterData(filter);
        }
    }
}
