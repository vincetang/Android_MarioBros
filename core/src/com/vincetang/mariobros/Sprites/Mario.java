package com.vincetang.mariobros.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Screens.PlayScreen;
import com.vincetang.mariobros.Sprites.Enemies.Enemy;

/**
 * Created by Vince on 16-06-27.
 */
public class Mario extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioJump;
    private TextureRegion marioDead;

    private Animation marioRun;
    private Animation bigMarioRun;
    private Animation growMario;

    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;

    private float stateTimer;


    public Mario(PlayScreen screen) {
        super();
        this.world = screen.getWorld();

        defineMario();

        currentState = State.STANDING;
        previousState = State.STANDING;

        stateTimer = 0;
        runningRight = true;

        // Standing (no animation)
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand);

        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"),
                0, 0, 16, 32);


        // Array of frames to animate for little_mario
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // running animation (frames 1-3)
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        }
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        }
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();

        // growing mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);
        frames.clear();

        // jumping animations
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"),
                80, 0, 16, 32);
        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

    }

    public void update(float dt) {
        if (marioIsBig)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - (6/MarioBros.PPM));
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        if (timeToDefineBigMario)
            defineBigMario();
        if (timeToRedefineMario)
            redefineMario();
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;

        switch (currentState) {
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = (marioIsBig ? bigMarioJump : marioJump);
                break;
            case RUNNING:
                region = (marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) :
                        marioRun.getKeyFrame(stateTimer, true));
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        // if we change states we reset the timer
        stateTimer = (currentState == previousState ? stateTimer + dt : 0);
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (marioIsDead) {
            return State.DEAD;
        } else if (runGrowAnimation) {
            return State.GROWING;
        } else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
                return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
                return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
                return State.RUNNING;
        } else {
                return State.STANDING;
        }
    }


    private void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape(); // A line between two points
        head.set(new Vector2(-2 / MarioBros.PPM, 7 / MarioBros.PPM),
                new Vector2(2 / MarioBros.PPM, 7 / MarioBros.PPM));
        fdef.shape = head;
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.isSensor = true; // sensors used for information and don't collide
        b2body.createFixture(fdef).setUserData(this);

    }

    private void defineBigMario() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // create a second circle so they stack
        shape.setPosition(new Vector2(0, -14/MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape(); // A line between two points
        head.set(new Vector2(-2 / MarioBros.PPM, 7 / MarioBros.PPM),
                new Vector2(2 / MarioBros.PPM, 7 / MarioBros.PPM));
        fdef.shape = head;
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.isSensor = true; // sensors used for information and don't collide
        b2body.createFixture(fdef).setUserData(this);

        timeToDefineBigMario = false;
    }

    private void redefineMario() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape(); // A line between two points
        head.set(new Vector2(-2 / MarioBros.PPM, 7 / MarioBros.PPM),
                new Vector2(2 / MarioBros.PPM, 7 / MarioBros.PPM));
        fdef.shape = head;
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.isSensor = true; // sensors used for information and don't collide
        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineMario = false;
    }

    public void grow() {
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        setBounds(getX(), getY(), getWidth(), getHeight() * 2); // double small mario's height
    }

    public boolean isBig() {
        return marioIsBig;
            }

    public void hit(Enemy enemy) {
        if (marioIsBig) {
            MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            marioIsBig = false;
            timeToRedefineMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight()/2);
        } else {
            MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = MarioBros.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);

        }
    }

    public boolean isDead() {
        return marioIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }
}
