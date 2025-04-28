package dk.ee.zg.logicnetwork;

import dk.ee.zg.common.enemy.data.BossActions;
import dk.ee.zg.common.enemy.interfaces.IEnemyNetwork;

import java.util.Arrays;
import java.util.List;

public class BayesianBossNetwork implements IEnemyNetwork {
    private List<String> possibleDistances;
    private List<String> possibleActions;
    private static BayesianNode meleeOffCooldown;
    private static BayesianNode aoeOffCooldown;
    private static BayesianNode rangeOffCooldown;
    private static BayesianNode distance;
    private static BayesianNode attack;
    private static BayesianNode meleeAttack;
    private static BayesianNode aoeAttack;
    private static BayesianNode rangeAttack;
    private static BayesianNode bossLowHealth;
    private static BayesianNode action;

    @Override
    public void buildNetwork() {
        possibleDistances = Arrays.asList("close", "mid", "far");
        possibleActions = Arrays.asList(
                "flee", "advance", "melee", "aoe", "range");
        prepareNodes();

        setUpAttackCpt(attack);

        setUpSpeceficAttackCases(meleeAttack, 1.0, 0.0, 0.0);
        setUpSpeceficAttackCases(aoeAttack, 0.6, 0.9, 0.0);
        setUpSpeceficAttackCases(rangeAttack, 0.3, 0.5, 0.8);

        setUpActionCpt(action);
    }

    private void prepareNodes() {
        meleeOffCooldown = new BayesianNode(
                "MeleeOffCooldown", Arrays.asList("true", "false"));
        aoeOffCooldown = new BayesianNode(
                "AoeOffCooldown", Arrays.asList("true", "false"));
        rangeOffCooldown = new BayesianNode(
                "RangeOffCooldown", Arrays.asList("true", "false"));
        distance = new BayesianNode(
                "Distance", possibleDistances);
        attack = new BayesianNode(
                "Attack", Arrays.asList("true", "false"));

        meleeAttack = new BayesianNode(
                "MeleeAttack", Arrays.asList("true", "false"));
        aoeAttack = new BayesianNode(
                "AoeAttack", Arrays.asList("true", "false"));
        rangeAttack = new BayesianNode(
                "RangeAttack", Arrays.asList("true", "false"));
        bossLowHealth = new BayesianNode(
                "BossLowHealth", Arrays.asList("true", "false"));

        action = new BayesianNode("Action", possibleActions);

        attack.setParents(Arrays.asList(
                meleeOffCooldown, aoeOffCooldown, rangeOffCooldown, distance));

        meleeAttack.setParents(
                Arrays.asList(meleeOffCooldown, distance, attack));
        aoeAttack.setParents(
                Arrays.asList(aoeOffCooldown, distance, attack));
        rangeAttack.setParents(
                Arrays.asList(rangeOffCooldown, distance, attack));
        action.setParents(Arrays.asList(
                meleeAttack, aoeAttack, rangeAttack, bossLowHealth, distance
        ));
    }

    private void setUpAttackCpt(BayesianNode attackNode) {
        // Attack Node (MCooldown, AoeCooldown, RCooldown, Distance)
        // True, True, True
        attackNode.addCptEntry("true_true_true_close_true", 1.0);
        attackNode.addCptEntry("true_true_true_close_false", 0.0);

        attackNode.addCptEntry("true_true_true_mid_true", 0.9);
        attackNode.addCptEntry("true_true_true_mid_false", 0.1);

        attackNode.addCptEntry("true_true_true_far_true", 0.8);
        attackNode.addCptEntry("true_true_true_far_false", 0.2);

        // False, True, True
        attackNode.addCptEntry("false_true_true_close_true", 0.9);
        attackNode.addCptEntry("false_true_true_close_false", 0.1);

        attackNode.addCptEntry("false_true_true_mid_true", 0.9);
        attackNode.addCptEntry("false_true_true_mid_false", 0.1);

        attackNode.addCptEntry("false_true_true_far_true", 0.8);
        attackNode.addCptEntry("false_true_true_far_false", 0.2);

        // True, False, True
        attackNode.addCptEntry("true_false_true_close_true", 0.9);
        attackNode.addCptEntry("true_false_true_close_false", 0.1);

        attackNode.addCptEntry("true_false_true_mid_true", 0.8);
        attackNode.addCptEntry("true_false_true_mid_false", 0.2);

        attackNode.addCptEntry("true_false_true_far_true", 0.8);
        attackNode.addCptEntry("true_false_true_far_false", 0.2);

        // True, True, False
        attackNode.addCptEntry("true_true_false_close_true", 0.9);
        attackNode.addCptEntry("true_true_false_close_false", 0.1);

        attackNode.addCptEntry("true_true_false_mid_true", 0.9);
        attackNode.addCptEntry("true_true_false_mid_false", 0.1);

        attackNode.addCptEntry("true_true_false_far_true", 0.0);
        attackNode.addCptEntry("true_true_false_far_false", 1.0);

        // False, False, True
        attackNode.addCptEntry("false_false_true_close_true", 0.8);
        attackNode.addCptEntry("false_false_true_close_false", 0.2);

        attackNode.addCptEntry("false_false_true_mid_true", 0.8);
        attackNode.addCptEntry("false_false_true_mid_false", 0.2);

        attackNode.addCptEntry("false_false_true_far_true", 0.8);
        attackNode.addCptEntry("false_false_true_far_false", 0.2);

        // False, True, False
        attackNode.addCptEntry("false_true_false_close_true", 0.8);
        attackNode.addCptEntry("false_true_false_close_false", 0.2);

        attackNode.addCptEntry("false_true_false_mid_true", 0.8);
        attackNode.addCptEntry("false_true_false_mid_false", 0.2);

        attackNode.addCptEntry("false_true_false_far_true", 0.0);
        attackNode.addCptEntry("false_true_false_far_false", 1.0);

        // True, False, False
        attackNode.addCptEntry("true_false_false_close_true", 0.8);
        attackNode.addCptEntry("true_false_false_close_false", 0.2);

        attackNode.addCptEntry("true_false_false_mid_true", 0.0);
        attackNode.addCptEntry("true_false_false_mid_false", 1.0);

        attackNode.addCptEntry("true_false_false_far_true", 0.0);
        attackNode.addCptEntry("true_false_false_far_false", 1.0);

        // False, False, False
        attackNode.addCptEntry("false_false_false_close_true", 0.0);
        attackNode.addCptEntry("false_false_false_close_false", 1.0);

        attackNode.addCptEntry("false_false_false_mid_true", 0.0);
        attackNode.addCptEntry("false_false_false_mid_false", 1.0);

        attackNode.addCptEntry("false_false_false_far_true", 0.0);
        attackNode.addCptEntry("false_false_false_far_false", 1.0);
    }

    private void setUpSpeceficAttackCases(BayesianNode speceficAttack,
                                          double prob1,
                                          double prob2,
                                          double prob3) {
        // Attack Node (Cooldown, Distance, Attack)
        // True, Distance, True
        speceficAttack.addCptEntry("true_close_true_true", prob1);
        speceficAttack.addCptEntry("true_close_true_false",
                1 - prob1);

        speceficAttack.addCptEntry("true_mid_true_true", prob2);
        speceficAttack.addCptEntry("true_mid_true_false",
                1 - prob2);

        speceficAttack.addCptEntry("true_far_true_true", prob3);
        speceficAttack.addCptEntry("true_far_true_false",
                1 - prob3);

        // True, Distance, False
        speceficAttack.addCptEntry("true_close_false_true", 0.0);
        speceficAttack.addCptEntry("true_close_false_false", 1.0);

        speceficAttack.addCptEntry("true_mid_false_true", 0.0);
        speceficAttack.addCptEntry("true_mid_false_false", 1.0);

        speceficAttack.addCptEntry("true_far_false_true", 0.0);
        speceficAttack.addCptEntry("true_far_false_false", 1.0);

        // False, Distance, True
        speceficAttack.addCptEntry("false_close_true_true", 0.0);
        speceficAttack.addCptEntry("false_close_true_false", 1.0);

        speceficAttack.addCptEntry("false_mid_true_true", 0.0);
        speceficAttack.addCptEntry("false_mid_true_false", 1.0);

        speceficAttack.addCptEntry("false_far_true_true", 0.0);
        speceficAttack.addCptEntry("false_far_true_false", 1.0);

        // False, Distance, False
        speceficAttack.addCptEntry("false_close_false_true", 0.0);
        speceficAttack.addCptEntry("false_close_false_false", 1.0);

        speceficAttack.addCptEntry("false_mid_false_true", 0.0);
        speceficAttack.addCptEntry("false_mid_false_false", 1.0);

        speceficAttack.addCptEntry("false_far_false_true", 0.0);
        speceficAttack.addCptEntry("false_far_false_false", 1.0);
    }

    private void setUpActionCpt(BayesianNode actionNode) {
        String key;
        // Action Node (mA, aoeA, rA, BossLowHealth, Distance)
        key = "true_true_true_true";
        addCloseActionEntry(key, actionNode, 0.1, 0.0, 0.2, 0.3, 0.4);
        addMidActionEntry(key, actionNode, 0.1, 0.0, 0.0, 0.5, 0.4);
        addFarActionEntry(key, actionNode, 0.0, 0.0, 0.0, 0.0, 1.0);

        key = "true_true_true_false";
        addCloseActionEntry(key, actionNode, 0.0, 0.1, 0.5, 0.3, 0.1);
        addMidActionEntry(key, actionNode, 0.0, 0.1, 0.0, 0.6, 0.3);
        addFarActionEntry(key, actionNode, 0.0, 0.1, 0.0, 0.0, 0.9);

        key = "true_true_false_true";
        addCloseActionEntry(key, actionNode, 0.1, 0.0, 0.2, 0.7, 0.0);
        addMidActionEntry(key, actionNode, 0.1, 0.0, 0.2, 0.7, 0.0);
        addFarActionEntry(key, actionNode, 0.1, 0.9, 0.0, 0.0, 0.0);

        key = "true_false_true_true";
        addCloseActionEntry(key, actionNode, 0.1, 0.0, 0.3, 0.0, 0.6);
        addMidActionEntry(key, actionNode, 0.6, 0.0, 0.0, 0.0, 0.4);
        addFarActionEntry(key, actionNode, 0.0, 0.1, 0.0, 0.0, 0.9);

        key = "false_true_true_true";
        addCloseActionEntry(key, actionNode, 0.5, 0.0, 0.0, 0.3, 0.2);
        addMidActionEntry(key, actionNode, 0.3, 0.0, 0.0, 0.5, 0.2);
        addFarActionEntry(key, actionNode, 0.1, 0.0, 0.0, 0.0, 0.9);

        key = "true_true_false_false";
        addCloseActionEntry(key, actionNode, 0.0, 0.2, 0.5, 0.3, 0.0);
        addMidActionEntry(key, actionNode, 0.0, 0.3, 0.0, 0.7, 0.0);
        addFarActionEntry(key, actionNode, 0.0, 1.0, 0.0, 0.0, 0.0);

        key = "true_false_true_false";
        addCloseActionEntry(key, actionNode, 0.0, 0.1, 0.7, 0.0, 0.2);
        addMidActionEntry(key, actionNode, 0.0, 0.4, 0.0, 0.0, 0.6);
        addFarActionEntry(key, actionNode, 0.0, 0.6, 0.0, 0.0, 0.4);

        key = "false_true_true_false";
        addCloseActionEntry(key, actionNode, 0.3, 0.0, 0.0, 0.7, 0.0);
        addMidActionEntry(key, actionNode, 0.1, 0.0, 0.0, 0.5, 0.4);
        addFarActionEntry(key, actionNode, 0.0, 0.2, 0.0, 0.0, 0.8);

        key = "true_false_false_true";
        addCloseActionEntry(key, actionNode, 0.4, 0.0, 0.6, 0.0, 0.0);
        addMidActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);
        addFarActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);

        key = "false_true_false_true";
        addCloseActionEntry(key, actionNode, 0.4, 0.0, 0.0, 0.6, 0.0);
        addMidActionEntry(key, actionNode, 0.2, 0.0, 0.0, 0.8, 0.0);
        addFarActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);

        key = "false_false_true_true";
        addCloseActionEntry(key, actionNode, 0.0, 0.0, 0.0, 0.0, 1.0);
        addMidActionEntry(key, actionNode, 0.0, 0.0, 0.0, 0.0, 1.0);
        addFarActionEntry(key, actionNode, 0.0, 0.0, 0.0, 0.0, 1.0);

        key = "true_false_false_false";
        addCloseActionEntry(key, actionNode, 0.0, 0.0, 1.0, 0.0, 0.0);
        addMidActionEntry(key, actionNode, 0.0, 1.0, 0.0, 0.0, 0.0);
        addFarActionEntry(key, actionNode, 0.0, 1.0, 0.0, 0.0, 0.0);

        key = "false_true_false_false";
        addCloseActionEntry(key, actionNode, 0.3, 0.0, 0.0, 0.7, 0.0);
        addMidActionEntry(key, actionNode, 0.0, 0.0, 0.0, 1.0, 0.0);
        addFarActionEntry(key, actionNode, 0.0, 1.0, 0.0, 0.0, 0.0);

        key = "false_false_true_false";
        addCloseActionEntry(key, actionNode, 0.0, 0.0, 0.0, 0.0, 1.0);
        addMidActionEntry(key, actionNode, 0.0, 0.0, 0.0, 0.0, 1.0);
        addFarActionEntry(key, actionNode, 0.0, 0.0, 0.0, 0.0, 1.0);

        key = "false_false_false_true";
        addCloseActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);
        addMidActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);
        addFarActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);

        key = "false_false_false_false";
        addCloseActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);
        addMidActionEntry(key, actionNode, 1.0, 0.0, 0.0, 0.0, 0.0);
        addFarActionEntry(key, actionNode, 0.0, 1.0, 0.0, 0.0, 0.0);
    }

    private void addCloseActionEntry(final String keyNoDistance,
                                   final BayesianNode actionNode,
                                   final double probFlee,
                                   final double probAdvance,
                                   final double probMelee,
                                   final double probAoe,
                                   final double probRange) {
        String closeKey = keyNoDistance + "_" + "close";
        actionNode.addCptEntry(closeKey + "_flee", probFlee);
        actionNode.addCptEntry(closeKey + "_advance", probAdvance);
        actionNode.addCptEntry(closeKey + "_melee", probMelee);
        actionNode.addCptEntry(closeKey + "_aoe", probAoe);
        actionNode.addCptEntry(closeKey + "_range", probRange);
    }

    private void addMidActionEntry(final String keyNoDistance,
                                   final BayesianNode actionNode,
                                   final double probFlee,
                                   final double probAdvance,
                                   final double probMelee,
                                   final double probAoe,
                                   final double probRange) {
        String midKey = keyNoDistance + "_" + "mid";
        actionNode.addCptEntry(midKey + "_flee", probFlee);
        actionNode.addCptEntry(midKey + "_advance", probAdvance);
        actionNode.addCptEntry(midKey + "_melee", probMelee);
        actionNode.addCptEntry(midKey + "_aoe", probAoe);
        actionNode.addCptEntry(midKey + "_range", probRange);
    }

    private void addFarActionEntry(final String keyNoDistance,
                                   final BayesianNode actionNode,
                                   final double probFlee,
                                   final double probAdvance,
                                   final double probMelee,
                                   final double probAoe,
                                   final double probRange) {
        String farKey = keyNoDistance + "_" + "far";
        actionNode.addCptEntry(farKey + "_flee", probFlee);
        actionNode.addCptEntry(farKey + "_advance", probAdvance);
        actionNode.addCptEntry(farKey + "_melee", probMelee);
        actionNode.addCptEntry(farKey + "_aoe", probAoe);
        actionNode.addCptEntry(farKey + "_range", probRange);
    }

    @Override
    public BossActions decideAction(boolean meleeReady, boolean aoeReady,
                                    boolean rangedReady, double bossHealth,
                                    double playerDistance) {
        meleeOffCooldown.setEvidence(meleeReady ? "true" : "false");
        aoeOffCooldown.setEvidence(aoeReady ? "true" : "false");
        rangeOffCooldown.setEvidence(rangedReady ? "true" : "false");

        String distanceCategory;
        if (playerDistance < 4) {
            distanceCategory = "close";
        } else if (playerDistance < 6) {
            distanceCategory = "mid";
        } else {
            distanceCategory = "far";
        }
        distance.setEvidence(distanceCategory);

        bossLowHealth.setEvidence(bossHealth < 30.0 ? "true" : "false");


        String attackState = attack.query();
        attack.setEvidence(attackState);

        String meleeAttackState = meleeAttack.query();
        meleeAttack.setEvidence(meleeAttackState);

        String aoeAttackState = aoeAttack.query();
        aoeAttack.setEvidence(aoeAttackState);

        String rangeAttackState = rangeAttack.query();
        rangeAttack.setEvidence(rangeAttackState);

        String bestAction = action.query();
        return getAction(bestAction);
    }

    private BossActions getAction(String actionName) {
        BossActions bossAction = null;
        switch (actionName) {
            case "advance":
                bossAction = BossActions.ADVANCE;
                break;
            case "melee":
                bossAction = BossActions.MELEE;
                break;
            case "aoe":
                bossAction = BossActions.AOE;
                break;
            case "range":
                bossAction = BossActions.RANGE;
                break;
            default:
                bossAction = BossActions.FLEE;
        }
        return bossAction;
    }
}
