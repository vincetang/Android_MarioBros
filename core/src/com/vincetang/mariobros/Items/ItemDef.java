package com.vincetang.mariobros.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Vince on 16-06-28.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
