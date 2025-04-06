package dk.ee.zg.common.enemy.interfaces;

import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.map.data.Entity;

public interface IEnemy {
     /**
     * Should extend this method by implementing an attack method.
     * Is purely used within its own implementation,
     * so can be left empty if needed.
     */
    Rectangle attack(Entity entity);
}
