package dk.ee.zg.boss;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IGamePluginService;

public class BossPlugin implements IGamePluginService {
    Boss boss;

    @Override
    public void start(WorldEntities worldEntities) {
        boss = new Boss(1,5,10,100,1);
        worldEntities.addEntity(boss);

        boss.setPosition(new Vector2(5,5));
        System.out.println("boss spawned");
        System.out.println("Boss spawned at: " + boss.getPosition());
    }

    @Override
    public void stop(WorldEntities worldEntities) {
        worldEntities.removeEntity(worldEntities.getEntities(Boss.class).getFirst().getId());
    }
}

