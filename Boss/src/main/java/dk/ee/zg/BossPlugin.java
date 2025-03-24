package dk.ee.zg;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IGamePluginService;

import java.util.List;

public class BossPlugin implements IGamePluginService {
    Boss boss;

    @Override
    public void start(WorldEntities worldEntities) {
        boss = new Boss(1,1,1,100,1);
        boss.setScale(new Vector2(1,1));
        worldEntities.addEntity(boss);

        boss.setPosition(new Vector2(0,0));
    }

    @Override
    public void stop(WorldEntities worldEntities) {
        worldEntities.removeEntity(worldEntities.getEntities(Boss.class).getFirst().getId());
    }
}

