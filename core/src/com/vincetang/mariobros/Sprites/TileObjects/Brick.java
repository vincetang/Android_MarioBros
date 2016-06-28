package com.vincetang.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Screens.PlayScreen;

/**
 * Created by Vince on 16-06-27.
 */
public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);

        MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();



    }
}
