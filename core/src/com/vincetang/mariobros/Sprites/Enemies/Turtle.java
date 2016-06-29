package com.vincetang.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.vincetang.mariobros.MarioBros;
import com.vincetang.mariobros.Screens.PlayScreen;
import com.vincetang.mariobros.Sprites.Mario;

import static com.vincetang.mariobros.Tools.B2WorldCreator.destroyTurtle;

/**
 * Created by Vince on 16-06-29.
 */
public class Turtle extends Enemy {
    public static final int KICK_LEFT_SPEED = -2;
    public static final int KICK_RIGHT_SPEED = 2;


    public enum State { WALKING, STANDING_SHELL, MOVING_SHELL, DEAD }

    public State currentState;
    public State previousState;

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private float deadRotationDegrees;

    private TextureRegion shell;


    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        deadRotationDegrees = 0;

        // Walking Animation Frames
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 24 / MarioBros.PPM);

        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-6, 2).scl(1 / MarioBros.PPM);
        vertices[1] = new Vector2(6, 2).scl(1 / MarioBros.PPM);
        vertices[2] = new Vector2(-6, -2).scl(1 / MarioBros.PPM);
        vertices[3] = new Vector2(6, -2).scl(1 / MarioBros.PPM);
        shape.set(vertices);

        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT|
                MarioBros.OBJECT_BIT |
                MarioBros.MARIO_BIT |
                MarioBros.ENEMY_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Create the Head here
        PolygonShape head = new PolygonShape();

        vertices = new Vector2[4];
        vertices[0] = new Vector2(-6, 8).scl(1 / MarioBros.PPM);
        vertices[1] = new Vector2(6, 8).scl(1 / MarioBros.PPM);
        vertices[2] = new Vector2(-4, 6).scl(1 / MarioBros.PPM);
        vertices[3] = new Vector2(4, 6).scl(1 / MarioBros.PPM);
        head.set(vertices);

        // Create the head that we stomp on
        fdef.shape = head;
        fdef.restitution = 0.7f; // Causes mario to bounce off Goomba's head
        fdef.filter.categoryBits = (MarioBros.ENEMY_HEAD_BIT);

        // set user data lets you have access to this class in our collision handler
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void hitOnHead(Mario mario) {
        mario.immunify(true);
        if (currentState != State.STANDING_SHELL) {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        } else {
            // turlte in STANDING_SHELL state
            kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);
        }

    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if (currentState == State.STANDING_SHELL && stateTime > 10) {
            currentState = State.WALKING;
            velocity.x = 1;
        }

        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - 4/MarioBros.PPM);
        if (currentState == State.DEAD) {
            deadRotationDegrees += 1;
            rotate(deadRotationDegrees);
            if (stateTime > 5 && !destroyed) {
                world.destroyBody(b2body);
                destroyed = true;
                destroyTurtle(this);
            }
        } else {
            b2body.setLinearVelocity(velocity);
        }
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (currentState) {
            case STANDING_SHELL:
            case MOVING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if (velocity.x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        } else if (velocity.x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        // reset time only if state changes
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    public void kick(int speed) {
        velocity.x = speed;
        currentState = State.MOVING_SHELL;
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void onEnemyHit(Enemy enemy) {
        if (enemy instanceof Turtle) {
            if (((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
                killed();
            } else if (currentState == State.MOVING_SHELL && ((Turtle) enemy).currentState == State.WALKING) {
                return;
            } else {
                reverseVelocity(true, false);
            }
        } else if (currentState != State.MOVING_SHELL) {
            reverseVelocity(true, false);
        }
    }

    public void killed() {
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MarioBros.NOTHING_BIT;
        for (Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);

        // pop turtle up in the air
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);

    }
}
