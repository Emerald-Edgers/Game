package dk.ee.zg.basemap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.map.interfaces.IMap;

public class BaseMap implements IMap {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    @Override
    public void loadMap(String mapName, float unitScale) {
        map = new TmxMapLoader().load(mapName);
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void renderMap() {
        renderer.setView(GameData.getInstance().getCamera());
        renderer.render();
    }

    @Override
    public TiledMap getMap() {
        return map;
    }

    @Override
    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }
}
