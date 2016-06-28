package com.vincetang.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Screens.PlayScreen;
import com.vincetang.mariobros.Sprites.Brick;
import com.vincetang.mariobros.Sprites.Coin;
import com.vincetang.mariobros.Sprites.Goomba;

/**
 * Created by Vince on 16-06-27.
 */
public class B2WorldCreator {

    private Array<Goomba> goombas;

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap tiledMap = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // create ground bodies/fixtures
        for (MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //3 types of bodies: dynamic, kinematic, static
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/ MarioBros.PPM,
                    (rect.getY() + rect.getHeight() / 2)/ MarioBros.PPM);

            body = world.createBody(bdef);

            // fixture
            shape.setAsBox((rect.getWidth() / 2)/ MarioBros.PPM, (rect.getHeight() / 2)/ MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // create pipes bodies/fixtures
        for (MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody; //3 types of bodies: dynamic, kinematic, static
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/ MarioBros.PPM,
                    (rect.getY() + rect.getHeight() / 2)/ MarioBros.PPM);

            body = world.createBody(bdef);

            // fixture
            shape.setAsBox((rect.getWidth() / 2)/ MarioBros.PPM, (rect.getHeight() / 2)/ MarioBros.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fdef);
        }

        // create bricks bodies/fixtures
        for (MapObject object : tiledMap.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);
        }

        // create Goombas
        for (MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rect);
        }

        // Create all goombas
        goombas = new Array<Goomba>();
        for (MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            goombas.add(new Goomba(screen, rect.getX()/MarioBros.PPM, rect.getY()/MarioBros.PPM));
        }

    }
}
