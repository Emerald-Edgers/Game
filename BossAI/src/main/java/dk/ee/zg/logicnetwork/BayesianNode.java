package dk.ee.zg.logicnetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BayesianNode {
    /**
     * Name of the node.
     */
    private String name;

    /**
     * The possible states a node may be in.
     */
    private List<String> states;

    /**
     * A list of all parent nodes.
     */
    private List<BayesianNode> parents;

    /**
     * The conditional probability table.
     */
    private Map<String, Double> cpt;

    /**
     * The current state of the node.
     */
    private String currentState;

    /**
     * Constructor for a new node in the network.
     * @param newName   The name of the node.
     * @param newStates The possible states of the node.
     */
    public BayesianNode(final String newName, final List<String> newStates) {
        this.name = newName;
        this.states = newStates;
        this.currentState = this.states.get(0);
    }

    /**
     * Queries the node for the most likely state.
     * @return A string representing the most likely state.
     */
    public String query() {
        if (parents == null || parents.isEmpty()) {
            return getHighestProbabilityState();
        } else {
            return inferStateFromParents();
        }
    }

    private String getHighestProbabilityState() {
        String bestState = states.get(0);
        double bestProb = 0.0;

        for (String state : states) {
            String key = state;
            if (cpt.containsKey(key) && cpt.get(key) > bestProb) {
                bestProb = cpt.get(key);
                bestState = state;
            }
        }

        return bestState;
    }

    private String inferStateFromParents() {
        StringBuilder keyBuilder = new StringBuilder();
        for (BayesianNode parent : parents) {
            keyBuilder.append(parent.getCurrentState()).append("_");
        }

        String bestState = states.get(0);
        double bestProb = 0.0;

        // Check each possible state with the parent configuration
        for (String state : states) {
            String key = keyBuilder.toString() + state;
            if (cpt.containsKey(key) && cpt.get(key) > bestProb) {
                bestProb = cpt.get(key);
                bestState = state;
            }
        }

        return bestState;
    }

    /**
     * Get the current state of this node.
     * @return A string representing the current state of the node.
     */
    public String getCurrentState() {
        return currentState;
    }

    /**
     * Gets the name of this node.
     * @return A string representing the name of the node.
     */
    public String getName() {
        return name;
    }

    /**
     * Set evidence for this node (observed state).
     * @param state A string representing the observed state of the node.
     */
    public void setEvidence(final String state) {
        if (states.contains(state)) {
            this.currentState = state;
        }
    }

    /**
     * Set the list of parent nodes.
     * @param newParents The list of parent nodes.
     */
    public void setParents(final List<BayesianNode> newParents) {
        this.parents = newParents;
    }

    /**
     * Set the conditional probability table.
     * The key format should be:
     * - For root nodes: "state"
     * - For child nodes: "parentState1_parentState2_..._state"
     * @param newCpt The conditional probability table.
     */
    public void setCpt(final Map<String, Double> newCpt) {
        this.cpt = newCpt;
    }

    /**
     * Helper method to add a single entry to the CPT.
     * @param key        The key of the entry.
     *                  Should be a string representation of the state.
     * @param probability The probability of the entry.
     */
    public void addCptEntry(final String key, final Double probability) {
        if (this.cpt == null) {
            this.cpt = new HashMap<>();
        }
        this.cpt.put(key, probability);
    }
}
