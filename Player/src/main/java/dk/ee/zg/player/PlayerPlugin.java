package dk.ee.zg.player;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IGamePluginService;
import dk.ee.zg.common.weapon.WeaponManager;

public class PlayerPlugin implements IGamePluginService {
    /**
     * instance of player {@link Player}.
     */
    private Player player;

    /**
     * start method for instantiating the player.
     * @param worldEntities - Object of WorldEntities,
     *              contains a map of all entities on map.
     */
    @Override
    public void start(final WorldEntities worldEntities) {
        player = new Player(1, 1, 1, 10, 1, 1,
                1, 1, 1, 1, 1, 1);
        player.setScale(new Vector2(1 / 32f, 1 / 32f));
        player.setPosition(new Vector2(25, 25));
        player.setWeapon(WeaponManager.getInstance().getWeaponSelected());
        player.loadStatsFromWeapon();
        worldEntities.addEntity(player);
    }

    /**
     * stop method for removing the player.
     * @param worldEntities - Object of WorldEntities,
     *              contains a map of all entities on map.
     */
    @Override
    public void stop(final WorldEntities worldEntities) {
        worldEntities.removeEntity(
                worldEntities.getEntities(Player.class).getFirst().getId());
    }


}
