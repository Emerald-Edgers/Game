// Enemy.java
package dk.ee.zg;

public class Skeleton {
    protected float health;
    protected float speed;

    public Skeleton(float health, float speed) {
        this.health = health;
        this.speed = speed;
    }

    public void attack() {
        // Implement enemy attack logic here
        System.out.println("Enemy attacks!");
    }

    public float getHealth() {
        return health;
    }

    public void takeDamage(float damage) {
        health -= damage;
    }
}