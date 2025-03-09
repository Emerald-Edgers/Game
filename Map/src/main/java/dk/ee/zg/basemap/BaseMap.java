package dk.ee.zg.basemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.map.interfaces.IMap;

public class BaseMap implements IMap {
    /**
     * The currently loaded {@link TiledMap}.
     */
    private TiledMap map;

    /**
     * The {@link OrthogonalTiledMapRenderer} used to draw the map.
     */
    private OrthogonalTiledMapRenderer renderer;

    /**
     * Sets this instance's {@link TiledMap} to the map found.
     * Creates a new {@link OrthogonalTiledMapRenderer}
     * for map with given unitscale.
     * @param mapName      The name of the map.
     *                     Provided as a filename with extension.
     * @param unitScale    The scale of a singular unit.
     *                     (E.g. 1 / 32 means 32 pixels per unit)
     */
    @Override
    public void loadMap(final String mapName, final float unitScale) {
        map = new TmxMapLoader().load(mapName);
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    /**
     * Sets the view of this instance's {@link OrthogonalTiledMapRenderer}
     * to the camera retrieved from {@link GameData}.
     * Calls {@code OrthogonalTiledMapRenderer.render()}
     */
    @Override
    public void renderMap() {
        renderer.setView(GameData.getInstance().getCamera());
        renderer.render();
    }

    /**
     * Retrieves this instance's map.
     * @return the {@link TiledMap} for the current instance.
     */
    @Override
    public TiledMap getMap() {
        return map;
    }

    /**
     * Retrieves this instance's renderer.
     * @return  the {@link OrthogonalTiledMapRenderer}
     * for the current instance.
     */
    @Override
    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }
}
