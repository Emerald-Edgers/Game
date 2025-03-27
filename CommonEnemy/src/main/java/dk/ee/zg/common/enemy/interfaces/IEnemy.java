package dk.ee.zg.common.enemy.interfaces;

import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.map.data.Entity;

public interface IEnemy {
    Rectangle attack(Entity entity);
}
