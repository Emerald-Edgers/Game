package dk.ee.zg.pathfinding;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldObstacles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class AIPathFinding implements IPathFinder {
    /**
     * Instance of the singleton class {@link GameData}.
     */
    private GameData gameData = GameData.getInstance();

    /**
     * boolean 2d array, of walkable tiles.
     */
    private static boolean[][] walkableGrid;

    /**
     * the tiled map used {@link TiledMap}.
     */
    private static TiledMap map;

    /**
     * impl process method for processing pathfinding,
     * and converting to actual coordinates.
     * @param origin - origin for pathfinding from, and moving
     * @param target - target of pathfinding e.g. player
     * @return - returns list of vector coordinates to define path.
     */
    @Override
    public List<Vector2> process(final Entity origin, final Entity target) {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer)
                map.getLayers().get("Collision");
        float tileWidth = tileLayer.getTileWidth() * gameData.getUNIT_SCALE();
        float tileHeight = tileLayer.getTileHeight() * gameData.getUNIT_SCALE();
        //since get position returns center pos of entity,
        // it is only divided by half of tilewidth and height.
        int startX = (int) (origin.getPosition().x / (tileWidth));
        int startY = (int) (origin.getPosition().y / (tileHeight));
        int goalX = (int) (target.getPosition().x / (tileWidth));
        int goalY = (int) (target.getPosition().y / (tileHeight));
        List<Vector2> pathInPixelPos = new ArrayList<>();
        for (Node node : findPath(startX, startY, goalX, goalY)) {
            //node position multiplied with half of tile size,
            // to get center of tile in pixel pos.
            Vector2 tempPos = new Vector2(node.getX() * (tileWidth),
                    node.getY() * (tileHeight));
            pathInPixelPos.add(tempPos);
        }
        return pathInPixelPos;
    }

    /**
     * impl method for loading map to walkable grid.
     * @param obstacles - instance of WorldObstacles,
     *                  contains all collidable obstacles.
     * @param tiledMap - TiledMap containing all tilemap data.
     */
    @Override
    public void load(final WorldObstacles obstacles, final TiledMap tiledMap) {
        this.map = tiledMap;
        TiledMapTileLayer tileLayer = (TiledMapTileLayer)
                tiledMap.getLayers().get("Collision");

        int mapWidth = tileLayer.getWidth();
        int mapHeight = tileLayer.getHeight();
        walkableGrid = new boolean[mapWidth][mapHeight];
        //initialize walkable grid with size of the camera.
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                walkableGrid[x][y] = true;

            }
        }

        float tileWidth = tileLayer.getTileWidth() * gameData.getUNIT_SCALE();
        float tileHeight = tileLayer.getTileHeight() * gameData.getUNIT_SCALE();
        // Mark cells where obstacle is as non-walkable
        for (Rectangle obstacle : obstacles.getObstacles()) {

            int x = (int) (obstacle.x / tileWidth);
            int y = (int) (obstacle.y / tileHeight);

            if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) {
                walkableGrid[x][y] = false;
            }
        }
        //testing
        /*
        walkableGrid[53][30] = false;
        walkableGrid[54][29] = false;
        walkableGrid[53][29] = false;
        walkableGrid[54][30] = false;
        walkableGrid[55][29] = false;
        walkableGrid[54][31] = false;
        walkableGrid[56][29] = false;
        walkableGrid[55][31] = false;
        walkableGrid[53][31] = false;
        walkableGrid[54][32] = false;
        List<Node> nodesPath = findPath(15, 15, 55, 30);
        System.out.println("printing path:");
        for (Node node : nodesPath) {
            System.out.println(
                    "taking step: XY[" + node.getX() + ":"
                            + node.getY() + "]"
                            + " gcost: " + node.getgCost() + " hcost: "
                            + node.gethCost() + " fcost: " + node.getfCost());
        }
        */
    }

    /**
     * method for finding path with a* algorithm.
     * @param startX - start x position in walkable grid
     * @param startY - start y position in walkable grid
     * @param goalX - goal x position in walkable grid
     * @param goalY - goal y position in walkable grid
     * @return - returns list of Nodes to define path on grid.
     */
    private List<Node> findPath(final int startX, final int startY,
                          final int goalX, final int goalY) {
        PriorityQueue<Node> open = new PriorityQueue<>();
        Set<Node> closed = new HashSet<>();

        Node startNode = new Node(startX, startY,
                heuristic(startX, startY, goalX, goalY), null, 0);
        open.add(startNode);
        while (!open.isEmpty()) {
            Node currentNode = open.poll();
            //if node is goal
            if (currentNode.getX() == goalX && currentNode.getY() == goalY) {
                return constructPath(currentNode);
            }
            closed.add(currentNode);

            for (Node succesorNode : getSuccesors(currentNode, goalX, goalY)) {
                //if closed contains successor node, skip it
                if (closed.contains(succesorNode)) {
                    continue;
                }

                float movementCost = (succesorNode.getX() != currentNode.getX()
                        && succesorNode.getY() != currentNode.getY())
                        ? (float) Math.sqrt(2) : 1;
                float tentativeGCost = currentNode.getgCost() + movementCost;

                if (!open.contains(succesorNode)
                        || tentativeGCost < succesorNode.getgCost()) {
                    // New node, add to open list
                    succesorNode.setgCost(tentativeGCost);
                    succesorNode.setParent(currentNode);
                    open.add(succesorNode);
                }

            }


        }
        //failure
        return Collections.emptyList();
    }

    /**
     * method used for constructing the final path,
     * from parent tree of current node.
     * @param endNode - currently open node
     * @return - returns the total path,
     * from start to end as list of nodes.
     */
    private List<Node> constructPath(final Node endNode) {
        Node currentNode = endNode;
        List<Node> path = new ArrayList<>();
        while (currentNode != null) {
            path.addFirst(currentNode); // Add at the beginning to reverse order
            currentNode = currentNode.getParent();
        }
        return path;
    }


    /**
     * method to get and generate succesor nodes.
     * @param node - node to get succesors of
     * @param goalX - goal x position
     * @param goalY - goal y position
     * @return - returns list of succesor nodes.
     */
    private List<Node> getSuccesors(
            final Node node, final int goalX, final int goalY) {
        List<Node> succesors = new ArrayList<>();
        //directions to check  : Up, Right, Down, Left, + Diagonals
        int[][] directions = {{0, 1}, {1, 0}, {0, -1},
                {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            int x = node.getX() + dir[0];
            int y = node.getY() + dir[1];
            //if outside x
            if (x < 0 || x > walkableGrid.length - 1) {
                continue;
            }
            //if outside y
            if (y < 0 || y > walkableGrid[x].length - 1) {
                continue;
            }
            if (walkableGrid[x][y]) {
                float movementCost =
                        (dir[0] == 0 || dir[1] == 0) ? 1 : (float) Math.sqrt(2);
                float newGCost = node.getgCost() + movementCost;
                succesors.add(new Node(
                        x, y, heuristic(x, y, goalX, goalY), node, newGCost));
            }
        }
        return succesors;
    }


    /**
     * heuristic function used for a* algorithm.
     * since entity has 8 way directional movement:
     * we use Diagonal distance for our heuristic:
     * <a href="https://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#S7">...</a>
     * @param startX - start x position in walkable grid
     * @param startY - start y position in walkable grid
     * @param endX - end x position in walkable grid
     * @param endY - end y position in walkable grid
     * @return -
     */
    private float heuristic(final int startX, final int startY,
                            final int endX, final int endY) {
        float dx = Math.abs(startX - endX);
        float dy = Math.abs(startY - endY);
        // formula :
        // return D * (dx + dy) + (D2 - 2 * D) * min(dx, dy)
        // When D = 1 and D2 = 1, this is called the Chebyshev distance.
        // When D = 1 and D2 = sqrt(2), this is called the octile distance.
        return (float) (1 * (dx + dy)
                + (Math.sqrt(2) - 2 * 1) * Math.min(dx, dy));
    }



}
