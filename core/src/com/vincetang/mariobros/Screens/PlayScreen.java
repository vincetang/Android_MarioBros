package com.vincetang.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Scenes.Hud;
import com.vincetang.mariobros.Sprites.Goomba;
import com.vincetang.mariobros.Sprites.Mario;
import com.vincetang.mariobros.Tools.B2WorldCreator;
import com.vincetang.mariobros.Tools.WorldContactListener;

/**
 * Created by Vince on 16-06-26.
 */
public class PlayScreen implements Screen {

    TextureAtlas atlas;

    private MarioBros game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private Mario player;
    private Goomba goomba;

    private TmxMapLoader mapLoader; // loads the tmx file into the game
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer; // renders map to screen

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Music music;

    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        // cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        // create FitViewport to maintain aspect ratio
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM,
                MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / MarioBros.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        goomba = new Goomba(this, .64f, .32f);

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f),
                    player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public TiledMap getMap() {
        return tiledMap;
    }

    public World getWorld() {
        return world;
    }


    public void update(float dt) {
        handleInput(dt);

        // Take 1 step in the physics simulation (60 times per second);
        world.step(1/60f, 6, 2);

        // attach our gamecam to our players.x coordinate
        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();
        player.update(dt);
        goomba.update(dt);

        renderer.setView(gamecam); // only render what our gamecam can see
    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }
    /**
     * Called when this screen becomes the current screen for a Game
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // separate our update logic from render
        update(delta);

        // Clear game screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // actually clears the screen

        // render our game map
        renderer.render();

        // render our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        // Drawing the player
        game.batch.setProjectionMatrix(gamecam.combined); // only what the game can see
        game.batch.begin();
        player.draw(game.batch);
        goomba.draw(game.batch);
        game.batch.end();

        // Set our batch to now draw what the hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined); // what is shown via camera
        hud.stage.draw();

        hud.update(delta);
    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a Game
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
