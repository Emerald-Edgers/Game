package dk.ee.zg.pathfinding;


import java.util.Objects;

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
     * @param newGCost {@link Node#gCost}
     */
    public Node(final int newX, final int newY,
                final float newHCost,
                final Node newParent, final float newGCost) {
        this.x = newX;
        this.y = newY;
        this.hCost = newHCost;
        this.parent = newParent;
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
    /**
     * for use in checking if set contains this node,
     * checking by coordinates instead of normally.
     * @param o - other object to compare to
     * @return - returns true if XY equals or object equals,
     * returns false if not an instance or XY not equals.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    /**
     * basic impl of hashcode with Objects.hash.
     * {@link Objects#hash(Object...)}
     * @return - returns hashed int
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    /**
     * getter for gcost.
     * @return - gcost
     */
    public final float getgCost() {
        return gCost;
    }
    /**
     * getter for hcost.
     * @return - hcost
     */
    public final float gethCost() {
        return hCost;
    }
    /**
     * getter for fcost.
     * @return - fcost
     */
    public final float getfCost() {
        return fCost;
    }

    public final Node getParent() {
        return parent;
    }

    /**
     * sets g cost of node.
     * @param newGCost - new cost of g
     */
    public void setgCost(final float newGCost) {
        this.gCost = newGCost;
        this.fCost = gCost + hCost;
    }

    /**
     * sets parent of node.
     * @param newParent - new parent of node
     */
    public void setParent(final Node newParent) {
        this.parent = newParent;
    }
}
