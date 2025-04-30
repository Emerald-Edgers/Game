package dk.ee.zg.enemy.dragon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemy;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.player.Player;

import java.util.Optional;
import java.util.ServiceLoader;

public class DragonControlSystem implements IEntityProcessService, IEnemy {
    /**
     * The main player which the dragon should chase.
     */
    private Entity player;


    /**
     * The main singular attack for the dragon enemy.
     *
     * @param entity - the Dragon enemy that uses the attack
     * @return - returns aoe rectangle around the entity
     */
    @Override
    public Rectangle attack(final Entity entity) {
        Vector2 dragonPos = entity.getPosition();

        float width = 8;
        float height = 8;

        return new Rectangle(
                dragonPos.x - width / 2,
                dragonPos.y - height / 2,
                width,
                height
        );
    }

    /**
     * Process that runs once per frame, will attempt to locate the player.
     * If no player is found the loop breaks.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     */
    @Override
    public void process(final WorldEntities worldEntities, final WorldObstacles worldObstacles) {
        if (player == null) {
            Optional<Entity> tempPlayer = worldEntities.getEntities(
                    Player.class).stream().findFirst();
            if (tempPlayer.isPresent()) {
                player = tempPlayer.get();
            } else {
                return;
            }
        }

        for (Entity eDragon : worldEntities.getEntities(Dragon.class)) {
            Dragon dragon = (Dragon) eDragon;
            Optional<IPathFinder> pathFinder =
                    ServiceLoader.load(IPathFinder.class).findFirst();

            if (pathFinder.isPresent()) {
                dragon.moveWithPathFinding(pathFinder.get(), player);
            } else {
                move(dragon);
            }
        }
    }

    /**
     * Moves the dragon based on the player's position.
     * @param dragon {@link Dragon}
     */
    private void move(final Dragon dragon) {

        Vector2 dragonPosition = dragon.getPosition();
        Vector2 playerPosition = player.getPosition();

        Vector2 dir = new Vector2(
                playerPosition.x - dragonPosition.x,
                playerPosition.y - dragonPosition.y);
        dir.nor();
        float speed = dragon.getMoveSpeed();

        Vector2 newPosition = new Vector2(
                dragonPosition.x + dir.x * speed * Gdx.graphics.getDeltaTime(),
                dragonPosition.y + dir.y * speed * Gdx.graphics.getDeltaTime()
        );

        dragon.setPosition(newPosition);
    }
}
