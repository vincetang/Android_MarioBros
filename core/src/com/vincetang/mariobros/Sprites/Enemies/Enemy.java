package com.vincetang.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.vincetang.mariobros.Screens.PlayScreen;
import com.vincetang.mariobros.Sprites.Mario;

/**
 * Created by Vince on 16-06-28.
 */
public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();

        // Make the enemy walk
        velocity = new Vector2(0.5f, 0);

        // Puts the box2d body to sleep (no calculations made in simulation)
        b2body.setActive(false);
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead(Mario mario);
    public abstract void update(float dt);
}
