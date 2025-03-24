package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.map.data.World;
import dk.ee.zg.common.map.services.IGamePluginService;

public class SkeletonPlugin implements IEnemyCreator {
    Skeleton skeleton;

    @Override
    public void start(World world) {
        skeleton = new Skeleton(10,10,10,100,10,10);
        skeleton.setScale(new Vector2(1/10f, 1/10f));

        world.addEntity(skeleton);

    }

    @Override
    public void spawn() {

    }

    @Override
    public float getEnemyCost() {
        return 0;
    }
}
