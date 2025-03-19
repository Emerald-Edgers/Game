package dk.ee.zg.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> attackAnimation;
    private float stateTime = 0f;
    private BossState currentState = BossState.IDLE;

    @Override
    public void attack() {
        setState(BossState.ATTACKING);
        System.out.println("set state to attacking");
    }

    private enum BossState {
        IDLE, ATTACKING, MOVING
    }

    public Boss(int attackDamage, int attackSpeed, int moveSpeed, int hitpoints, int defense) {
        super(new Vector2(), 0, new Vector2(1/10f,1/10f), "boss/Idle/Idle-Sheet.png", EntityType.Boss);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
        this.hitpoints = hitpoints;
        this.defense = defense;

       loadAnimations();
    }

    private void loadAnimations() {
        try {

            boolean idleExists = Gdx.files.internal("boss/Idle/Idle-Sheet.png").exists();
            boolean attackExists = Gdx.files.internal("boss/Death/Death-Sheet.png").exists();
            System.out.println("Idle exists: " + idleExists + ", Attack exists: " + attackExists);

            // Load Idle Animation
            Texture idleSpriteSheet = new Texture(Gdx.files.internal("boss/Idle/Idle-Sheet.png"));

            int frameWidth = 32;
            int frameHeight = 32;

            TextureRegion[][] idleFrames = TextureRegion.split(idleSpriteSheet, frameWidth, frameHeight);

            int idleFrameCount = idleFrames[0].length;
            System.out.println("Found " + idleFrameCount + " idle frames");

            int actualIdleFrames = Math.min(idleFrameCount, 4);

            TextureRegion[] idleAnimFrames = new TextureRegion[actualIdleFrames];
            for (int i = 0; i < actualIdleFrames; i++) {
                idleAnimFrames[i] = idleFrames[0][i];
            }
            idleAnimation = new Animation<>(0.15f, idleAnimFrames);

            // Load Attack Animation
            Texture attackSpriteSheet = new Texture(Gdx.files.internal("boss/Death/Death-Sheet.png"));
            int attackFrameWidth = 64;
            int attackFrameHeight = 64;

            TextureRegion[][] attackFrames = TextureRegion.split(attackSpriteSheet, attackFrameWidth, attackFrameHeight);

            int attackFrameCount = attackFrames[0].length;
            System.out.println("Found " + attackFrameCount + " attack frames");

            TextureRegion[] attackAnimFrames = new TextureRegion[attackFrameCount];
            for (int i = 0; i < attackFrameCount; i++) {
                attackAnimFrames[i] = attackFrames[0][i];
            }
            attackAnimation = new Animation<>(0.15f, attackAnimFrames);

            //System.out.println(idleAnimation.getKeyFrame(0f, true));


        } catch (Exception e) {
         System.out.println("Error loading Idle/Idle-Sheet.png"+ e.getMessage());
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame;


        if (currentState == BossState.ATTACKING) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, false);
            System.out.println("attacking");
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
            //System.out.println("idling");
        }

        if (currentFrame != null) {
            batch.draw(
                    currentFrame,
                    getPosition().x,
                    getPosition().y,
                    currentFrame.getRegionWidth() * getScale().x,
                    currentFrame.getRegionHeight() * getScale().y
            );
        } else {
            // Fallback to parent class drawing if animations fail to load
            super.draw(batch);
        }

        if ( currentState == BossState.ATTACKING && attackAnimation.isAnimationFinished(stateTime)) {
            setState(BossState.IDLE);
            System.out.println("attack finished");
        }

    }

    public Rectangle getBounds(float radius) {
        Vector2 position = getPosition();

        float width = 1 * getScale().x;
        float height = 1 * getScale().y;

        return new Rectangle(
                position.x - radius,
                position.y - radius,
                width + (radius * 2),
                height + (radius * 2)
        );
    }

    public void setState(BossState state) {
        this.currentState = state;

        System.out.println("Changed state");

        if (state == BossState.ATTACKING) {
            stateTime = 0;
        }
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

}
