package dk.ee.zg.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.weapon.AttackDirection;
import dk.ee.zg.common.weapon.Weapon;
import dk.ee.zg.common.enemy.interfaces.IAnimatable;

import java.util.HashMap;
import java.util.Map;


public class Player extends Entity implements IAnimatable {
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

    private AnimationState currentState = AnimationState.IDLE;

    private Map<AnimationState, String> stateToAnimateMap;
    private Map<String,String> directionAnimationMap;

    private AttackDirection facingDirection = AttackDirection.RIGHT;

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
                "Animations/Carry_Idle/Carry_Idle_Down-Sheet.png", EntityType.Player,true);
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

        initializeAnimations();
        setState(AnimationState.IDLE, this.facingDirection);

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


    @Override
    public void initializeAnimations() {
        createAnimation("IDLE_DOWN","Animations/Carry_Idle/Carry_Idle_Down-Sheet.png",4,1,0.1f, Animation.PlayMode.LOOP);
        createAnimation("IDLE_SIDE","Animations/Carry_Idle/Carry_Idle_Side-Sheet.png",4,1,0.1f, Animation.PlayMode.LOOP);
        //createAnimation("IDLE_UP","Animations/Carry_Idle/Carry_Idle_Up-Sheet.png",4,1,0.1f, Animation.PlayMode.LOOP);

        createAnimation("ATTACK_SIDE","Animations/Pierce_Base/Pierce_Side-Sheet.png",8,1,0.1f,Animation.PlayMode.NORMAL);
        createAnimation("ATTACK_UP","Animations/Pierce_Base/Pierce_Top-Sheet.png",8,1,0.1f,Animation.PlayMode.NORMAL);
        createAnimation("ATTACK_DOWN","Animations/Pierce_Base/Pierce_Down-Sheet.png",8,1,0.1f,Animation.PlayMode.NORMAL);

        createAnimation("RUN_DOWN","Animations/Carry_Run/Carry_Run_Down-Sheet.png",6,1,0.1f, Animation.PlayMode.LOOP);
        createAnimation("RUN_UP","Animations/Carry_Run/Carry_Run_Up-Sheet.png",6,1,0.1f,Animation.PlayMode.LOOP);
        createAnimation("RUN_SIDE","Animations/Carry_Run/Carry_Run_Side-Sheet.png",6,1,0.1f,Animation.PlayMode.LOOP);

        stateToAnimateMap = new HashMap<>();

        stateToAnimateMap.put(AnimationState.IDLE,"IDLE_DOWN");
        stateToAnimateMap.put(AnimationState.RUN,"RUN_DOWN");
        stateToAnimateMap.put(AnimationState.ATTACK,"ATTACK_SIDE");

        directionAnimationMap = new HashMap<>();

        directionAnimationMap.put("IDLE_" + AttackDirection.UP.name(), "IDLE_DOWN");
        directionAnimationMap.put("IDLE_" + AttackDirection.DOWN.name(), "IDLE_DOWN");
        directionAnimationMap.put("IDLE_" + AttackDirection.LEFT.name(), "IDLE_SIDE");
        directionAnimationMap.put("IDLE_" + AttackDirection.RIGHT.name(), "IDLE_SIDE");

        directionAnimationMap.put("RUN_" + AttackDirection.UP.name(), "RUN_UP");
        directionAnimationMap.put("RUN_" + AttackDirection.DOWN.name(), "RUN_DOWN");
        directionAnimationMap.put("RUN_" + AttackDirection.RIGHT.name(), "RUN_SIDE");
        directionAnimationMap.put("RUN_" + AttackDirection.LEFT.name(), "RUN_SIDE");

        directionAnimationMap.put("ATTACK_" + AttackDirection.UP.name(), "ATTACK_UP");
        directionAnimationMap.put("ATTACK_" + AttackDirection.DOWN.name(), "ATTACK_DOWN");
        directionAnimationMap.put("ATTACK_" + AttackDirection.RIGHT.name(), "ATTACK_SIDE");
        directionAnimationMap.put("ATTACK_" + AttackDirection.LEFT.name(), "ATTACK_SIDE");



    }

    public void setState(AnimationState state, AttackDirection direction) {
        if (state != currentState || direction != facingDirection) {
            currentState = state;
            facingDirection = direction;

            String directionKey = state.name() + "_" + direction.name();
            String animationName = directionAnimationMap.get(directionKey);

            if (animationName == null) {
                animationName = stateToAnimateMap.get(state);
            }

            if (animationName != null) {
                setCurrentAnimation(animationName);

                if (direction == AttackDirection.LEFT && (animationName.contains("_SIDE"))) {
                    getSprite().setFlip(true, false);
                } else {
                    getSprite().setFlip(false, false);
                }
            }
        }
    }

    @Override
    public void setState(AnimationState state) {
        setState(state,facingDirection);

        /*if (state != currentState) {
            currentState = state;
            String animationName = stateToAnimateMap.get(state);
            if (animationName != null) {
                setCurrentAnimation(animationName);
            }
        }

         */
    }

    public AnimationState getCurrentState() {
        return currentState;
    }

    public AttackDirection getFacingDirection() {
        return facingDirection;
    }
}
