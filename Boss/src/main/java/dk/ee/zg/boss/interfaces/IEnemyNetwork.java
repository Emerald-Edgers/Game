package dk.ee.zg.boss.interfaces;

import dk.ee.zg.boss.BossActions;

public interface IEnemyNetwork {
    void buildNetwork();
     BossActions decideAction(boolean meleeReady, boolean aoeReady, boolean rangedReady,
                              double bossHealth, double playerDistance);
}
