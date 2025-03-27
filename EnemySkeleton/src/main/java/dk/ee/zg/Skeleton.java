// Enemy.java
package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;

public class Skeleton extends Entity {

    private int attackDamage;
    private int attackSpeed;
    private int moveSpeed;
    private int hitpoints;
    private int defense;
    private float cost;

    public Skeleton(int attackDamage, int attackSpeed, int moveSpeed, int hitpoints, int defense, float cost,
                    Vector2 spawnPoint) {
        super(spawnPoint, 0, new Vector2(1/25f,1/25f), "Skeleton.png", EntityType.Enemy);

    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

}