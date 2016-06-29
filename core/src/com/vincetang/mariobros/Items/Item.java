package com.vincetang.mariobros.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Screens.PlayScreen;

/**
 * Created by Vince on 16-06-28.
 */
public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        defineItem();
        this.toDestroy = false;
        this.destroyed = false;
    }

    public abstract void defineItem();
    public abstract void use();

    public void draw(Batch batch) {
        if (!destroyed)
            super.draw(batch);
    }

    public void update(float dt) {
        if (toDestroy && !destroyed) {
            world.destroyBody(this.body);
            this.destroyed = true;
        }
    }

    public void destroy() {
        this.destroyed = true;
    }

}
