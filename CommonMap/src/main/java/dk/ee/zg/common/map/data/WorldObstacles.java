package dk.ee.zg.common.map.data;

import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;


public class WorldObstacles {

    private final Map<UUID,Rectangle> obstaclesMap =
            new HashMap<UUID, Rectangle>();
    private final Map<UUID,Rectangle> currentlyVisibleObstaclesMap = new HashMap<UUID,Rectangle>();

    public void addObstacle(Rectangle rect){

    }

    public void removeObstacle(UUID obstacleID){

    }

    public void removeObstacle(Rectangle rect){

    }

    public Rectangle getObstacle(UUID obstacleID){
        return null;
    }


    public List<Rectangle> getObstacles() {
        return null;
    }

    public List<Rectangle> getVisibleObstacles() {
        return currentlyVisibleObstaclesMap.values().stream().toList();
    }

    public void optimizeObstacles(){

    }


}
