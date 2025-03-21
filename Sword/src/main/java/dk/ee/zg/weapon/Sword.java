package dk.ee.zg.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.weapon.AttackDirection;
import dk.ee.zg.common.weapon.Weapon;

public class Sword extends Weapon {



    /**
     * constructor for Sword, initializes and sets all Weapon variables.
     * {@link Weapon}
     */
    public Sword() {
        this.spritePath = "sword.png";
        this.rotation = 0;
        this.scale = new Vector2(1, 1);
        Texture img = new Texture(spritePath); // Load texture from file
        this.sprite = new Sprite(img); // Create a sprite from the texture

        //stats
        this.maxHP = 100;
        this.attackDamage = 10;
        this.attackSpeed = 10;
        this.moveSpeed = 10;
    }

    @Override
    public Rectangle attack(Vector2 playerPos, AttackDirection attackDirection) {
        float attackOffsetX = 0;
        float attackOffsetY = 0;
        switch (attackDirection) {
            case LEFT -> attackOffsetX = -2.5f;
            case RIGHT -> attackOffsetX = 0.5f;
            case UP -> attackOffsetY = 0.5f;
            case DOWN -> attackOffsetY = -2.5f;
        }
        Rectangle hitbox = new Rectangle(playerPos.x + attackOffsetX,playerPos.y + attackOffsetY,2,1);


        return hitbox;
    }

}
