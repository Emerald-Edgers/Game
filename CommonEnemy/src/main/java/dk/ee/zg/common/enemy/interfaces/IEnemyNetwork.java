package dk.ee.zg.common.enemy.interfaces;

import dk.ee.zg.common.enemy.data.BossActions;
import dk.ee.zg.common.enemy.data.Enemy;

public interface IEnemyNetwork {
    void buildNetwork();
     BossActions decideAction(boolean meleeReady, boolean aoeReady, boolean rangedReady,
                              double bossHealth, double playerDistance);
}
