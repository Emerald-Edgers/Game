package dk.ee.zg.boss;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IGamePluginService;

public class BossPlugin implements IGamePluginService {
    /**
     * Instance of the boss {@link Boss}.
     */
    private Boss boss;

    /**
     * start method for instantiating the boss.
     * @param worldEntities - Object of WorldEntities,
     *                      which the boss gets added to
     */
    @Override
    public void start(final WorldEntities worldEntities) {
        boss = new Boss(1, 5, 2, 100, 1);
        boss.setPosition(new Vector2(25, 37.5f));

        worldEntities.addEntity(boss);

    }


    /**
     * Stop method for removing the boss.
     * @param worldEntities - Object of WorldEntities,
     *                 which the boss had been added to
     */
    @Override
    public void stop(final WorldEntities worldEntities) {
        worldEntities.removeEntity(
                worldEntities.getEntities(Boss.class).getFirst().getId());
    }
}

