package dk.ee.zg.boss.ranged;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.ICollisionEngine;

import java.util.ServiceLoader;

public class Projectile extends Entity {

    private final Vector2 direction;
    private final float speed;

    private final WorldEntities world;
    private final ICollisionEngine collisionEngine;
    private final Entity player;

    private float rotationAngle;


    public Projectile(Vector2 position,
                      Vector2 direction,
                      float speed,
                      WorldEntities world,
                      Entity player) {
        super(new Vector2(position),0, new Vector2(1/32f,1/32f), "Fire.png", EntityType.Projectile);

        this.player = player;

        this.direction = new Vector2(direction);

        if (this.direction.len() == 0) {
            this.direction.set(0,1);
        } else {
            this.direction.nor();
        }

        this.speed = speed;
        this.world = world;

        this.rotationAngle = direction.angleDeg();

        setRotation(rotationAngle);

        this.collisionEngine = getCollisionEngine();
    }

    private ICollisionEngine getCollisionEngine() {
        ServiceLoader<ICollisionEngine> collisionEngineLoader =
                ServiceLoader.load(ICollisionEngine.class);

        return collisionEngineLoader.findFirst().orElse(null);
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
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
            System.out.println("YESYESYES");
        }
    }
}
