package dk.ee.zg.boss;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.Player;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.World;
import dk.ee.zg.common.map.services.IGamePluginService;

import java.util.List;

public class BossPlugin implements IGamePluginService {
    private static final float ATTACK_INTERVAL = 3.0f;
    private static final float VIEWPORT_WIDTH = 32;
    private static final float VIEWPORT_HEIGHT = 20;
    private static java.util.UUID bossID;
    private static float attackTimer = 0;

    @Override
    public void start(World world) {
        Boss boss= new Boss(1,1,1,100,1);
        boss.setPosition(new Vector2(0,0));
        world.addEntity(boss);
        bossID = boss.getId();

        System.out.println("Boss plugin started, ID: " + bossID);


        //System.out.println(boss != null ? "yes" : "no");

        //System.out.println("Boss plugin started");

        System.out.println("Boss position: " + boss.getPosition());


    }

    @Override
    public void stop(World world) {
        List<Entity> bosses = world.getEntities(Boss.class);
        if (!bosses.isEmpty()) {
            world.removeEntity(bosses.getFirst().getId());
        }
    }

    @Override
    public void update(float delta, World world) {
        List<Entity> bosses = world.getEntities(Boss.class);
        List<Entity> players = world.getEntities(Player.class);

        if (!bosses.isEmpty() && !players.isEmpty()) {
            Boss boss = (Boss) bosses.getFirst();

            Rectangle detect = boss.getBounds(5f);

            Player player = (Player) players.getFirst();
            //System.out.println("Boss found");

            Vector2 playerPosition = player.getPosition();

            //System.out.println("player position" + playerPosition);

            System.out.println("Boss position: " + boss.getPosition().x + " " + boss.getPosition().y);

            for (Entity e : players) {
                Vector2 position = e.getPosition();

                System.out.println("Player pos:" + position.x + " " + position.y);

                //System.out.println("Hel");

                if (detect.contains(position.x, position.y)) {
                    System.out.println("player detected");
                }
            }

            attackTimer += delta;
            //System.out.println("Attack timer: " + attackTimer + " / " + ATTACK_INTERVAL);

            if (attackTimer > ATTACK_INTERVAL) {
                //System.out.println("Boss plugin attacking");
                boss.attack();
                attackTimer = 0;
            }
        } else {
            System.out.println("no boss found");
        }

    }
}

