package dk.ee.zg;

public class Player {
    //non-increasing base primary stats
    private final int MaxHP;
    private final int AttackDamage;
    private final int AttackSpeed;
    private final int MoveSpeed;

    //

    public Player(int maxHP, int attackDamage, int attackSpeed, int moveSpeed) {
        this.MaxHP = maxHP;
        this.AttackDamage = attackDamage;
        this.AttackSpeed = attackSpeed;
        this.MoveSpeed = moveSpeed;
    }


    public int getMaxHP() {
        return MaxHP;
    }

    public int getAttackDamage() {
        return AttackDamage;
    }

    public int getAttackSpeed() {
        return AttackSpeed;
    }

    public int getMoveSpeed() {
        return MoveSpeed;
    }


}
