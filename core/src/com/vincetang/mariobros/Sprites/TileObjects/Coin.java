package com.vincetang.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vincetang.mariobros.Items.ItemDef;
import com.vincetang.mariobros.Items.Mushroom;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Scenes.Hud;
import com.vincetang.mariobros.Screens.PlayScreen;

/**
 * Created by Vince on 16-06-27.
 */
public class Coin extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, Rectangle bounds) {

        super(screen, bounds);

        tileSet = tiledMap.getTileSets().getTileSet("tileset_gutter");

        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {

        if (getCell().getTile().getId() == BLANK_COIN) {
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();

        } else {
            MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            Hud.addScore(200);
            screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x,
                    body.getPosition().y + 16 / MarioBros.PPM),
                    Mushroom.class));
        }
    }


}
