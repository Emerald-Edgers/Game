package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.map.data.WorldEntities;

public class SkeletonPlugin implements IEnemyCreator {
    Skeleton skeleton;

    private float cost = 10f;

    public float getEnemyCost() {
        return cost;
    }

    @Override
    public void spawn(float x, float y, WorldEntities world) {
        skeleton = new Skeleton(10,10,10,100,10, cost, new Vector2(x, y));

        world.addEntity(skeleton);
    }
}
