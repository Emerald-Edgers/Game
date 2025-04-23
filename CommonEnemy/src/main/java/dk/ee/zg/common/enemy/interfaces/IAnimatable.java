package dk.ee.zg.common.enemy.interfaces;

import dk.ee.zg.common.map.data.AnimationState;

public interface IAnimatable {
    void initializeAnimations();
    void setState(AnimationState state);

}
