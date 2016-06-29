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
import com.vincetang.mariobros.Sprites.Enemies.Enemy;
import com.vincetang.mariobros.Sprites.Enemies.Turtle;
import com.vincetang.mariobros.Sprites.TileObjects.Brick;
import com.vincetang.mariobros.Sprites.TileObjects.Coin;
import com.vincetang.mariobros.Sprites.Enemies.Goomba;

/**
 * Created by Vince on 16-06-27.
 */
public class B2WorldCreator {

    public static Array<Goomba> goombas;
    public static Array<Turtle> turtles;
    public static Array<Enemy> enemies;

    public Array<Turtle> getTurtles() { return turtles; }

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

            new Brick(screen, object);
        }

        // create Goombas
        for (MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, object);
        }

        // Create all goombas
        goombas = new Array<Goomba>();
        for (MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            goombas.add(new Goomba(screen, rect.getX()/MarioBros.PPM, rect.getY()/MarioBros.PPM));
        }

        // Create all goombas
        turtles = new Array<Turtle>();
        for (MapObject object : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            turtles.add(new Turtle(screen, rect.getX()/MarioBros.PPM, rect.getY()/MarioBros.PPM));
        }

    }

    public Array<Enemy> getEnemies() {
        enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }

    public static void destroyTurtle(Turtle turtle) {
        enemies.removeValue(turtle, true);
        turtles.removeValue(turtle, true);
    }

    public static void destroyGoomba(Goomba goomba) {
        enemies.removeValue(goomba, true);
        goombas.removeValue(goomba, true);
    }
}
