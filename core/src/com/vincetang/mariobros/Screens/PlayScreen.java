package com.vincetang.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Scenes.Hud;

/**
 * Created by Vince on 16-06-26.
 */
public class PlayScreen implements Screen {

    private MarioBros game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader; // loads the tmx file into the game
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer; // renders map to screen


    public PlayScreen(MarioBros game) {
        this.game = game;
        // cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        // create FitViewport to maintain aspect ratio
        gamePort = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, gamecam);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched())
            gamecam.position.x += 100 * dt;
    }

    public void update(float dt) {
        handleInput(dt);

        gamecam.update();
        renderer.setView(gamecam); // only render what our gamecam can see
    }
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
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
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // actually clears the screen

        renderer.render();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined); // what is shown via camera
        hud.stage.draw();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
