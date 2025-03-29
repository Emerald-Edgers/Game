package dk.ee.zg.pathfinding;

import com.badlogic.gdx.math.Vector2;

/**
 * data class, representing a Node in a*.
 * contains cost
 */
public class Node implements Comparable<Node> {
    /**
     * x position of Node, in relation to the walkable grid.
     */
    private final int x;
    /**
     * y position of Node, in relation to the walkable grid.
     */
    private final int y;
    /**
     * total path cost to this Node.
     * g(n): cost so far to reach n (path cost)
     */
    private float gCost;
    /**
     * heuristic cost of exploring this Node.
     * h(n): estimated cost from n to goal (heuristic)
     */
    private final float hCost;
    /**
     * estimated total cost of exploring this Node.
     * The evaluation function f(n) is the estimated total cost
     * of the path through node n to the goal: f(n) = g(n) + h(n)
     */
    private float fCost;
    /**
     * parent Node of current node,
     * used to establish serach tree.
     */
    private Node parent;

    /**
     * constructor to construct a Node.
     * @param newX {@link Node#x}
     * @param newY {@link Node#y}
     * @param newHCost {@link Node#hCost}
     * @param newParent {@link Node#parent}
     */
    public Node(final int newX, final int newY,
                final float newHCost, final Node newParent, final float newGCost) {
        this.x = newX;
        this.y = newY;
        this.hCost = newHCost;
        this.parent = newParent;
        //float movementCost = (Math.abs(x - this.parent.getX()) + Math.abs(y - this.parent.getY()) == 2) ? (float) Math.sqrt(2) : 1;
        //this.gCost = this.parent.gCost + movementCost;
        this.gCost = newGCost;
        this.fCost = gCost + hCost;
    }


    /**
     * implementation of Compararable compareTo,
     * compares nodes to each other.
     * @param o the object to be compared.
     * @return - the value 0 if this == o;
     *  a value less than 0 if this < o; and a value greater than 0 if this > o
     */
    @Override
    public int compareTo(final Node o) {
        return Float.compare(this.fCost, o.fCost);
    }

    //for use in checking if set contains this node,
    //checking by coordinates instead of normally.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getgCost() {
        return gCost;
    }

    public float gethCost() {
        return hCost;
    }

    public float getfCost() {
        return fCost;
    }

    public Node getParent() {
        return parent;
    }

    public void setgCost(float newGCost) {
        this.gCost = newGCost;
        this.fCost = gCost + hCost;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
