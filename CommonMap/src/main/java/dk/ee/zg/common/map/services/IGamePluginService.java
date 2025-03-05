package dk.ee.zg.common.map.services;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dk.ee.zg.common.map.data.World;

public interface IGamePluginService {
    void start(World world);
    void stop(World world);

}
