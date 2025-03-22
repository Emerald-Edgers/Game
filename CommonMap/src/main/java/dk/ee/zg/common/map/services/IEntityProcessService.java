package dk.ee.zg.common.map.services;

import dk.ee.zg.common.map.data.WorldEntities;

public interface IEntityProcessService {
    /**
     * main entrance to all entity processors.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     */
    void process(WorldEntities worldEntities);
}
