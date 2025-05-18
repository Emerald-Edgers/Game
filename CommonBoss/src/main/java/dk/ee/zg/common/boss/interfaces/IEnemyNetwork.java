package dk.ee.zg.common.boss.interfaces;

import dk.ee.zg.common.boss.data.BossActions;

public interface IEnemyNetwork {
    void buildNetwork();
     BossActions decideAction(boolean meleeReady, boolean aoeReady, boolean rangedReady,
                              double bossHealth, double playerDistance);
}
