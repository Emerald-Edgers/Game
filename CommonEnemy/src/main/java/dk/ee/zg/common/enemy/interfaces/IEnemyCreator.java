package dk.ee.zg.common.enemy.interfaces;

import dk.ee.zg.common.map.data.WorldEntities;

public interface IEnemyCreator {
    void spawn(float x, float y, WorldEntities world);
    float getEnemyCost();
}
