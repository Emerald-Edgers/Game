package dk.ee.zg.common.map.services;

import dk.ee.zg.common.map.data.WorldEntities;

public interface IGamePluginService {
    void start(WorldEntities worldEntities);
    void stop(WorldEntities worldEntities);

}
