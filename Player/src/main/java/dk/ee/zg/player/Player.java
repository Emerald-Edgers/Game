package dk.ee.zg.player;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.weapon.Weapon;


public class Player extends Entity {
    /**
     * weapon typecast to Weapon abstract class from specific implementation.
     * {@link Weapon}
     */
    private Weapon weapon;


    //non-increasing base primary stats
    /**
     * maxHP is the max amount of HP, a player can have (the HP Stat).
     * used to calculate regeneration, and take damage
     */
    private int maxHP;
    /**
     * attackDamage is the amount of base damage, a player inflicts.
     * used to calculate actual damage to inflict upon enemy.
     */
    private int attackDamage;
    /**
     * attackSpeed is the speed at which, a player attacks.
     * used to calculate attack cooldown.
     */
    private int attackSpeed;
    /**
     * moveSpeed is the speed at which, a player moves.
     * used to calculate a players' movement.
     */
    private int moveSpeed;

    //increasing base secondary stats
    /**
     * hp is the current amount of health, a player has.
     * if 0 = player dies.
     */
    private int hp;
    /**
     * critChance is the chance of dealing a crit hit.
     * used to calculate critical hit on enemy.
     */
    private int critChance;
    /**
     * critDamage is the damage inflicted by dealing a crit hit.
     * used to calculate critical hit damage on enemy.
     */
    private int critDamage;
    /**
     * defense is the players' hit weakener.
     * used to calculate damage inflicted on player from enemy.
     */
    private int defense;
    /**
     * lifesteal is the chance of stealing health on hitting enemy.
     * used to calculate chance of stealing life and how much to steal.
     */
    private int lifesteal;
    /**
     * penetration ignores some of the defense of the enemy hit.
     * used to calculate damage inflicted on enemy.
     */
    private int penetration;
    /**
     * range is the length of the players attack.
     * used to calculate weapon attack hitbox length .
     */
    private int range;
    /**
     * evasion is the chance of evading an enemy's attack.
     * used to calculate if player gets hit.
     */
    private int evasion;
    /**
     * healthRegen is the amount of hp a player regenerates pr amount of time.
     * used to calculate player health regeneration per frame.
     */
    private int healthRegen;

    /**
     * constructor for the player, utilizes super class Entity.
     * {@link Entity}
     * @param maxHP {@link Player#maxHP}
     * @param attackDamage {@link Player#attackDamage}
     * @param attackSpeed {@link Player#attackSpeed}
     * @param moveSpeed {@link Player#moveSpeed}
     * @param critChance {@link Player#critChance}
     * @param critDamage {@link Player#critDamage}
     * @param defense {@link Player#defense}
     * @param lifesteal {@link Player#lifesteal}
     * @param penetration {@link Player#penetration}
     * @param range {@link Player#range}
     * @param evasion {@link Player#evasion}
     * @param healthRegen {@link Player#healthRegen}
     */
    public Player(int maxHP, int attackDamage, int attackSpeed, int moveSpeed,
                  int critChance, int critDamage, int defense, int lifesteal, int penetration,
                  int range, int evasion, int healthRegen) {
        super(new Vector2(), 0, new Vector2(50,50), "MainShip.png", EntityType.Player);
        this.maxHP = maxHP;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
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
     * loads primary stats from weapon, if weapon is not null.
     * {@link Weapon}
     */
    public void loadStatsFromWeapon() {
        if (weapon != null) {
            this.maxHP = weapon.getMaxHP();
            this.attackDamage = weapon.getAttackDamage();
            this.attackSpeed = weapon.getAttackSpeed();
            this.moveSpeed = weapon.getMoveSpeed();
            this.hp = this.maxHP;
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(final Weapon weapon) {
        this.weapon = weapon;
    }

    public int getMaxHP() {
        return maxHP;
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


    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        this.hp = hp;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(final int critChance) {
        this.critChance = critChance;
    }

    public int getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(final int critDamage) {
        this.critDamage = critDamage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(final int defense) {
        this.defense = defense;
    }

    public int getLifesteal() {
        return lifesteal;
    }

    public void setLifesteal(final int lifesteal) {
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

    public void setRange(final int range) {
        this.range = range;
    }

    public int getEvasion() {
        return evasion;
    }

    public void setEvasion(final int evasion) {
        this.evasion = evasion;
    }

    public int getHealthRegen() {
        return healthRegen;
    }

    public void setHealthRegen(final int healthRegen) {
        this.healthRegen = healthRegen;
    }


}
