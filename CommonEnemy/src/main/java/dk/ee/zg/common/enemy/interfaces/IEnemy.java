package dk.ee.zg.common.enemy.interfaces;

public interface IEnemy {
    /**
     * Should extend this method by implementing an attack method.
     * Is purely used within its own implementation,
     * so can be left empty if needed.
     */
    void attack();
}
