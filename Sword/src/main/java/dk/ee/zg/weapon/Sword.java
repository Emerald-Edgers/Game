package dk.ee.zg.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.weapon.Weapon;

public class Sword extends Weapon {

    public Sword(){
        this.sprite_path = "sword.png";
        this.rotation = 0;
        this.scale = new Vector2(1,1);
        Texture img = new Texture(sprite_path); // Load texture from file
        this.sprite = new Sprite(img); // Create a sprite from the texture

        //stats
        this.MaxHP = 100;
        this.AttackDamage = 10;
        this.AttackSpeed = 10;
        this.MoveSpeed = 10;
    }

    @Override
    public void attack(){

    }

}
