package dk.ee.zg.boss.ranged;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IAnimatable;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.ICollisionEngine;

import java.util.ServiceLoader;

public class Projectile extends Entity implements IAnimatable {

    /**
     * The vector direction of the projectile.
     */
    private final Vector2 direction;
    /**
     * How fast the projectile moves across the world.
     */
    private final float speed;
    /**
     * Object of all worldEntities, since the projectile gets treated.
     * as an enemy entity
     */
    private final WorldEntities world;
    /**
     * Collision checks for both terrain and players.
     */
    private final ICollisionEngine collisionEngine;
    /**
     * For knowing the player position to determine.
     * both collision and direction
     */
    private final Entity player;
    /**
     * The rotation angle of the projectile.
     * makes sure the projectiles sprite is aimed
     * towards the same direction
     */
    private float rotationAngle;


    /**
     * Constructor for the projectile.
     * utilizes entity super class
     * @param position - The x,y coordinate of where it should spawn
     * @param direction {@link Projectile#direction}
     * @param speed {@link Projectile#speed}
     * @param world {@link Projectile#world}
     * @param player {@link Projectile#player}
     */
    public Projectile(final Vector2 position, final Vector2 direction,
                      final float speed, final WorldEntities world,
                      final Entity player) {
        super(new Vector2(position), 0,
                new Vector2(1 / 32f, 1 / 32f),
                "Fireball.png", EntityType.Projectile);

        this.player = player;

        this.direction = new Vector2(direction);

        if (this.direction.len() == 0) {
            this.direction.set(0, 1);
        } else {
            this.direction.nor();
        }

        this.speed = speed;
        this.world = world;

        this.rotationAngle = direction.angleDeg();

        setRotation(rotationAngle);

        this.collisionEngine = getCollisionEngine();

        setHitbox(new Rectangle(0, 0, 0.5f, 0.5f));
        
        initializeAnimations();
    }

    private ICollisionEngine getCollisionEngine() {
        ServiceLoader<ICollisionEngine> collisionEngineLoader =
                ServiceLoader.load(ICollisionEngine.class);

        return collisionEngineLoader.findFirst().orElse(null);
    }

    /**
     * Updates the position of the projectile.
     */
    @Override
    public void update(float delta) {
        Vector2 pos = getPosition();
        pos.add(direction.x * speed * delta, direction.y * speed * delta);
        setPosition(pos);
        if (collisionEngine != null && player != null) {
            resolveCollision();
        }
    }

    /**
     * Resolves the collision between the fireball, and an entity.
     * Should only target players.
     */
    private void resolveCollision() {
        if (collisionEngine.collidesWithEntity(
                this, player)) {
            //TODO: Implement Hit
        }
    }

    @Override
    public void initializeAnimations() {
        createAnimation("IDLE","Fireball.png",5,1,1f/5f, Animation.PlayMode.LOOP);
    }

    @Override
    public void setState(AnimationState state) {

    }
}
