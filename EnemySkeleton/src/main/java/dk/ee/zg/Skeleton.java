// Enemy.java
package dk.ee.zg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;

public class Skeleton extends Entity {

    private int attackDamage;
    private int attackSpeed;
    private int moveSpeed;
    private int hitpoints;
    private int defense;
    private int cost;

    public Skeleton(int attackDamage, int attackSpeed, int moveSpeed, int hitpoints, int defense, int cost) {
        super(new Vector2(5,5), 0, new Vector2(1/10f,1/10f), "boss/Boss2.png", EntityType.Enemy);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}