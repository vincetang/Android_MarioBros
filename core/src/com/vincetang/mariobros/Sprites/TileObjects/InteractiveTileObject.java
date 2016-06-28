package com.vincetang.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Screens.PlayScreen;

/**
 * Created by Vince on 16-06-27.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap tiledMap;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.tiledMap = screen.getMap();
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

        fixture = body.createFixture(fdef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter (short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        // scale back up from MarioBros.PPM and divide by cell size
        return layer.getCell((int) (body.getPosition().x * MarioBros.PPM / 16),
                (int) (body.getPosition().y * MarioBros.PPM / 16) );
    }
}
