package dk.ee.zg.boss.ranged;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;

public class Projectile extends Entity {

    private Vector2 direction;
    private float speed;

    private boolean active = true;


    public Projectile(Vector2 position, Vector2 direction, float speed) {
        super(new Vector2(position),0, new Vector2(1/10f,1/10f), "Fire.png", EntityType.Enemy);


        this.direction = new Vector2(direction);

        if (this.direction.len() == 0) {
            this.direction.set(0,1);
        } else {
            this.direction.nor();
        }

        this.speed = speed;

        //this.sprite.setRotation(this.direction.angle());
        //this.sprite.setPosition(position.x, position.y);
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        Vector2 pos = getPosition();
        pos.add(direction.x * speed * delta, direction.y * speed * delta);
        setPosition(pos);

        float buffer = 50; // buffer to allow some off-screen movement before removal
        if (pos.x < -buffer || pos.x > Gdx.graphics.getWidth() + buffer ||
                pos.y < -buffer || pos.y > Gdx.graphics.getHeight() + buffer) {
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }

}
