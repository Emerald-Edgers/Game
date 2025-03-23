package dk.ee.zg.common.map.services;

import dk.ee.zg.common.map.data.WorldEntities;

public interface IGamePluginService {
    /**
     * main entrance to all game plugins.
     * supposed to be for starting the plugin.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     */
    void start(WorldEntities worldEntities);
    /**
     * main entrance to all game plugins.
     * supposed to be for stopping the plugin.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     */
    void stop(WorldEntities worldEntities);

}
