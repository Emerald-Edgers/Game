package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.map.data.WorldEntities;

public class SkeletonPlugin implements IEnemyCreator {
    Skeleton skeleton;

    private float cost = 1f;

    public float getEnemyCost() {
        return cost;
    }

    @Override
    public void spawn(float x, float y, WorldEntities world) {
        skeleton = new Skeleton(10,10,10,100,10, cost);
        skeleton.setScale(new Vector2(1/10f, 1/10f));

        world.addEntity(skeleton);
    }
}
