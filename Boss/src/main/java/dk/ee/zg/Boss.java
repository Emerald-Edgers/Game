package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemy;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;


public class Boss extends Entity implements IEnemy {

    private final int attackDamage;
    private final int attackSpeed;
    private final int moveSpeed;
    private final int hitpoints;
    private final int defense;

    public Boss(int attackDamage, int attackSpeed, int moveSpeed, int hitpoints, int defense) {
        super(new Vector2(0,0), 0, new Vector2(1/10f,1/10f), "Boss2.png", EntityType.Enemy);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
        this.hitpoints = hitpoints;
        this.defense = defense;
    }

    public int getAttackDamage() {
        return attackDamage;
    }
    public int getAttackSpeed() {
        return attackSpeed;
    }
    public int getMoveSpeed() {
        return moveSpeed;
    }
    public int getHitpoints() {
        return hitpoints;
    }
    public int getDefense() {
        return defense;
    }

    @Override
    public void attack() {

    }
}
