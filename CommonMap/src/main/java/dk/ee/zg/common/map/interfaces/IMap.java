package dk.ee.zg.common.map.interfaces;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.map.data.WorldObstacles;
import java.util.Collection;

public interface IMap {
    /**
     * Loads a map from a given name.
     * @param mapName      The name of the map.
     * @param unitScale    The scale of a singular unit.
     *                     (E.g. 1 / 32 means 32 pixels per unit)
     */
    void loadMap(String mapName, float unitScale);

    /**
     * Render the currently loaded map.
     * This method should be called during the render loop.
     */
    void renderMap();

    /**
     * Get the currently loaded map.
     * @return  the {@link TiledMap} instance representing the loaded map,
     *          or {@code null} if no map is loaded.
     */
    TiledMap getMap();

    /**
     * Get the map renderer.
     * @return  the {@link OrthogonalTiledMapRenderer}
     *          instance for the current map,
     *          or {@code null} if no renderer is available.
     */
    OrthogonalTiledMapRenderer getRenderer();

    /**
     * Get the obstacles as a collection of rectangles from a specific layer.
     * @param layer The layer of which the obstacles is on.
     * @param obstacles The WorldObstacles class that is being used.
     * @return Collection of obstacle rectangles.
     */
    Collection<Rectangle> getObstaclesFromLayer(MapLayer layer,
                                                WorldObstacles obstacles);

}
