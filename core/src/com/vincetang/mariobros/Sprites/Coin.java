package com.vincetang.mariobros.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.vincetang.mariobros.MarioBros;

/**
 * Created by Vince on 16-06-27.
 */
public class Coin extends InteractiveTileObject {
    public Coin(World world, TiledMap tiledMap, Rectangle bounds) {
        super(world, tiledMap, bounds);

    }
}
