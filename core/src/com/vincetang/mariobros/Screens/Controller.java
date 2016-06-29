package com.vincetang.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vincetang.mariobros.MarioBros;

/**
 * Created by Vince on 16-06-29.
 */
public class Controller {
    Viewport viewport;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed;
    OrthographicCamera cam;

    public Controller(MarioBros game)  {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setDebug(false);
        table.left().setPosition(0, viewport.getWorldHeight() / 2 - 50);
        Image dpadImg = new Image(new Texture("Mario GFX/dpad.png"));
        dpadImg.setSize(150, 150);
        dpadImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Touched down on", " x:" + x + " y:" + y);
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Touched up on", " x:" + x + " y:" + y);

            }


        });

        table.add().pad(5,20,0,5);
        table.add(dpadImg).size(dpadImg.getWidth(), dpadImg.getHeight());

        table.add().size(470,1);

        Image buttons = new Image(new Texture("Mario GFX/buttons.png"));
        buttons.setSize(150, 150);
        buttons.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Touched down on button", " x:" + x + " y:" + y +
                        " pointer:" + pointer + " button:" + button + "testing");

                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Touched up on button", " x:" + x + " y:" +
                        y + " pointer:" + pointer + " button:" + button + "testing");


            }


        });

        table.add(buttons).size(buttons.getWidth(), buttons.getHeight()).padRight(20);


        stage.addActor(table);
    }

    public void draw() {
        stage.draw();
    }

    public  void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose(){
        stage.dispose();
    }
}
