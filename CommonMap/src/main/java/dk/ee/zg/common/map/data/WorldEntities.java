package dk.ee.zg.common.map.data;


import dk.ee.zg.common.data.EventManager;
import dk.ee.zg.common.data.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WorldEntities {
    /**
     * HashMap {@link HashMap} casted to Map {@link Map},
     * of all entities {@link Entity} in map,
     * both enemies and player.
     */
    private final Map<UUID, Entity> entityMap = new HashMap<UUID, Entity>();

    /**
     * no args constructor, for basic setup
     * e.g. listener setup
     */
    public WorldEntities() {
        EventManager.addListener(Events.EnemyKilledEvent.class, enemyKilledEvent -> {
            removeEntity(enemyKilledEvent.getUuid());
        });
    }



    /**
     * adds entity {@link Entity} object to entity map.
     * @param entity - entity to add
     */
    public void addEntity(final Entity entity) {
        entityMap.put(entity.getId(), entity);
    }
    /**
     * removes entity {@link Entity} object to entity map.
     * @param entityID - entity to remove by UUID {@link UUID}
     */
    public void removeEntity(final UUID entityID) {
        entityMap.remove(entityID);
    }

    /**
     * gets entity {@link Entity} object to entity map.
     * @param entityID - entity to get by UUID {@link UUID}
     * @return - returns entity found
     */
    public Entity getEntity(final UUID entityID) {
        return entityMap.get(entityID);
    }

    /**
     * gets all entities {@link Entity} objects to list.
     * @return - returns list of all entities
     */
    public List<Entity> getEntities() {
        return entityMap.values().stream().toList();
    }

    /**
     * Gets entites by class.
     * @param entityTypes - class of entities to get
     * @return - returns entities found
     * @param <E> - entity class that extends Entity
     */
    public <E extends Entity> List<Entity> getEntities(
            final Class<E>... entityTypes) {
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

    /**
     * Gets first found entity by class.
     * @param entityClass - class of entity to get
     * @return - returns entity of class found
     * @param <E> - entity class that extends Entity
     */
    public <E extends Entity> Optional<E> getEntityByClass(
            final Class<E>... entityClass) {
        for (Entity e : getEntities()) {
            for (Class<E> entityType : entityClass) {
                if (entityType.equals(e.getClass())) {
                    return (Optional<E>) Optional.of(e);
                }
            }
        }
        return Optional.empty();
    }

}
