package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.weapon.Weapon;

import java.util.UUID;

public class Player extends Entity {
    //Essentials
    Weapon weapon;


    //non-increasing base primary stats
    private int MaxHP;
    private int AttackDamage;
    private int AttackSpeed;
    private int MoveSpeed;

    //increasing base secondary stats
    private int hp;
    private int critChance;
    private int critDamage;
    private int defense;
    private int lifesteal;
    private int penetration;
    private int range;
    private int evasion;
    private int healthRegen;

    public Player(int maxHP, int attackDamage, int attackSpeed, int moveSpeed,
                  int critChance, int critDamage, int defense, int lifesteal, int penetration,
                  int range, int evasion, int healthRegen) {
        super(new Vector2(), 0, new Vector2(50,50), "MainShip.png", EntityType.Player);
        this.MaxHP = maxHP;
        this.AttackDamage = attackDamage;
        this.AttackSpeed = attackSpeed;
        this.MoveSpeed = moveSpeed;
        this.hp = maxHP;
        this.critChance = critChance;
        this.critDamage = critDamage;
        this.defense = defense;
        this.lifesteal = lifesteal;
        this.penetration = penetration;
        this.range = range;
        this.evasion = evasion;
        this.healthRegen = healthRegen;

    }

    /**
     * loads primary stats from weapon, if weapon is not null
     * {@link Weapon}
     */
    public void loadStatsFromWeapon(){
        if (weapon != null){
            this.MaxHP = weapon.getMaxHP();
            this.AttackDamage = weapon.getAttackDamage();
            this.AttackSpeed = weapon.getAttackSpeed();
            this.MoveSpeed = weapon.getMoveSpeed();
            this.hp = this.MaxHP;
        }
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


    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public int getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(int critDamage) {
        this.critDamage = critDamage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getLifesteal() {
        return lifesteal;
    }

    public void setLifesteal(int lifesteal) {
        this.lifesteal = lifesteal;
    }

    public int getPenetration() {
        return penetration;
    }

    public void setPenetration(int penetration) {
        this.penetration = penetration;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getEvasion() {
        return evasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public int getHealthRegen() {
        return healthRegen;
    }

    public void setHealthRegen(int healthRegen) {
        this.healthRegen = healthRegen;
    }


}
