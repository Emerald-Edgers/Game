package dk.ee.zg.common.weapon;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public abstract class Weapon {

    private final UUID id = UUID.randomUUID();

    protected String sprite_path;

    protected Sprite sprite;
    protected float rotation;

    protected Vector2 scale;

    //non-increasing base primary stats
    protected int MaxHP;
    protected int AttackDamage;
    protected int AttackSpeed;
    protected int MoveSpeed;

    public void attack() {

    }


    public UUID getId() {
        return id;
    }

    public String getSprite_path() {
        return sprite_path;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getRotation() {
        return rotation;
    }


    public Vector2 getScale() {
        return scale;
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
