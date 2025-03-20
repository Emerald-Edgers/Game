package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {
    Player player;

    @Override
    public void start(WorldEntities worldEntities) {
        player = new Player(1,1,1,100,1,1
                ,1,1,1,1,1,1);
        player.setScale(new Vector2(1/32f, 1/32f));
        worldEntities.addEntity(player);

    }

    @Override
    public void stop(WorldEntities worldEntities) {
        worldEntities.removeEntity(worldEntities.getEntities(Player.class).getFirst().getId());
    }


}
