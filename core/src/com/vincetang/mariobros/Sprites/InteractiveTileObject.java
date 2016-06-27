package com.vincetang.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.vincetang.mariobros.MarioBros;

/**
 * Created by Vince on 16-06-27.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap tiledMap;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, TiledMap tiledMap, Rectangle bounds) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // create coins bodies/fixtures
        bdef.type = BodyDef.BodyType.StaticBody; //3 types of bodies: dynamic, kinematic, static
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2)/ MarioBros.PPM,
                (bounds.getY() + bounds.getHeight() / 2)/ MarioBros.PPM);

        body = world.createBody(bdef);

        // fixture
        shape.setAsBox((bounds.getWidth() / 2)/ MarioBros.PPM, (bounds.getHeight() / 2)/ MarioBros.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }


}
