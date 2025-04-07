package dk.ee.zg.player;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.EventManager;
import dk.ee.zg.common.data.Events;
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
     * level of player,
     * increased upon experience {@link Player#experience} hitting threshold.
     */
    private int level = 0;
    /**
     * experience of player,
     * used for levelling up when value reaches threshold.
     */
    private float experience = 0;

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
     * @param maxLife {@link Player#maxHP}
     * @param atkDamage {@link Player#attackDamage}
     * @param atkSpeed {@link Player#attackSpeed}
     * @param mvSpeed {@link Player#moveSpeed}
     * @param crChance {@link Player#critChance}
     * @param crDamage {@link Player#critDamage}
     * @param def {@link Player#defense}
     * @param hpsteal {@link Player#lifesteal}
     * @param pen {@link Player#penetration}
     * @param r {@link Player#range}
     * @param eva {@link Player#evasion}
     * @param hpRegen {@link Player#healthRegen}
     */
    public Player(final int maxLife, final int atkDamage, final int atkSpeed,
                  final int mvSpeed, final int crChance, final int crDamage,
                  final int def, final int hpsteal, final int pen,
                  final int r, final int eva, final int hpRegen) {
        super(new Vector2(), 0, new Vector2(50, 50),
                "MainShip.png", EntityType.Player);
        this.maxHP = maxLife;
        this.attackDamage = atkDamage;
        this.attackSpeed = atkSpeed;
        this.moveSpeed = mvSpeed;
        this.setHp(maxLife);
        this.critChance = crChance;
        this.critDamage = crDamage;
        this.defense = def;
        this.lifesteal = hpsteal;
        this.penetration = pen;
        this.range = r;
        this.evasion = eva;
        this.healthRegen = hpRegen;
        initEventListeners();
    }

    /**
     * method for setting event listeners to,
     * functions related.
     * {@link dk.ee.zg.common.data.EventManager}
     */
    private void initEventListeners() {
        EventManager.addListener(Events.EnemyKilledEvent.class,
                enemyKilledEvent -> {
            gainExperience(enemyKilledEvent.getExperience());
        });
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
            this.setHp(this.maxHP);
        }
    }

    /**
     * method for accepting experience to be added,
     * to player, might levelup player.
     * @param exp - experience value to add.
     */
    private void gainExperience(final int exp) {
        experience += exp;
        // threshold defined by manipulating level.
        //currently linear x100 of level
        if (experience > level * 100) {
            levelUp();
        }
    }

    /**
     * method to levelup player,
     * and handles additional actions.
     * e.g. triggering player levelup event.
     */
    private void levelUp() {
        level++;
        EventManager.triggerEvent(new Events.PlayerLevelUpEvent());
    }


    public final Weapon getWeapon() {
        return weapon;
    }

    public final void setWeapon(final Weapon w) {
        this.weapon = w;
    }

    public final int getMaxHP() {
        return maxHP;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final int getAttackSpeed() {
        return attackSpeed;
    }

    public final int getMoveSpeed() {
        return moveSpeed;
    }

    public final int getCritChance() {
        return critChance;
    }

    public final void setCritChance(final int i) {
        this.critChance = i;
    }

    public final int getCritDamage() {
        return critDamage;
    }

    public final void setCritDamage(final int i) {
        this.critDamage = i;
    }

    public final int getDefense() {
        return defense;
    }

    public final void setDefense(final int i) {
        this.defense = i;
    }

    public final int getLifesteal() {
        return lifesteal;
    }

    public final void setLifesteal(final int i) {
        this.lifesteal = i;
    }

    public final int getPenetration() {
        return penetration;
    }

    public final void setPenetration(final int i) {
        this.penetration = i;
    }

    public final int getRange() {
        return range;
    }

    public final void setRange(final int i) {
        this.range = i;
    }

    public final int getEvasion() {
        return evasion;
    }

    public final void setEvasion(final int i) {
        this.evasion = i;
    }

    public final int getHealthRegen() {
        return healthRegen;
    }

    public final void setHealthRegen(final int i) {
        this.healthRegen = i;
    }

    public final int getLevel() {
        return level;
    }

    public final void setLevel(final int lvl) {
        this.level = lvl;
    }

    public final float getExperience() {
        return experience;
    }

    public final void setExperience(final float exp) {
        this.experience = exp;
    }
}
