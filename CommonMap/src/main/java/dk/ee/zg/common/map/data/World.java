package dk.ee.zg.common.map.data;

import java.util.*;

public class World {

    private final Map<UUID,Entity> entityMap = new HashMap<UUID,Entity>();

    /**
     * adds entity to entity map
     * @param entity
     */
    public void addEntity(Entity entity){
        entityMap.put(entity.getId(), entity);
    }


    public Map<UUID, Entity> getEntityMap() {
        return entityMap;
    }


    public void removeEntity(UUID entityID){
        entityMap.remove(entityID);
    }

    public Entity getEntity(UUID entityID){
        return entityMap.get(entityID);
    }


    /**
     * gets values of entityMap
     * @return - values of entityMap
     */
    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    /**
     * Gets entites by class
     * @param entityTypes - class of entities to get
     * @return - returns entities found
     * @param <E> - entity class that extends Entity
     */
    public <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> r = new ArrayList<Entity>();
        for (Entity e : getEntities()) {
            for (Class<E> entityType : entityTypes) {
                if (entityType.equals(e.getClass())) {
                    r.add(e);
                }
            }
        }
        return r;
    }

}
