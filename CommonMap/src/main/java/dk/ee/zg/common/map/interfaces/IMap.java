package dk.ee.zg.common.map.interfaces;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public interface IMap {
    void loadMap(String mapName, float unitScale);
    void renderMap();

    TiledMap getMap();
    OrthogonalTiledMapRenderer getRenderer();
}
