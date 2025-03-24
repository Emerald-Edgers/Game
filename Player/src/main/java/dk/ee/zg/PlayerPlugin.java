package dk.ee.zg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {
    Player player;

    @Override
    public void start(WorldEntities world) {
        player = new Player(1,1,1,50,1,1
                ,1,1,1,1,1,1);
        //player.setScale(new Vector2(1/32f, 1/32f));
        world.addEntity(player);
        player.setPosition(new Vector2(0,0));

    }

    @Override
    public void stop(WorldEntities world) {
        world.removeEntity(world.getEntities(Player.class).getFirst().getId());
    }

}
