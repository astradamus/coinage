package world.blueprint;

import utils.Array2D;
import utils.Dimension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A builder class used to fabricate Array2D's of any type extending BlueprintFeature, containing
 * the elements in featureSet distributed according to their getWeight() value relative to the other
 * features in the set. Does not actually distribute the features, only sets itself up as a base and
 * exposes methods that enable easily distributing these features (so that the method of
 * distribution can vary).
 */
public class Blueprint<E extends BlueprintFeature> {

    private final Set<E> featureSet;
    private final E mostCommonFeature;

    private final Array2D<E> featureLayout;

    private final Map<E, Integer> featureCountGoals;
    private final Map<E, Integer> featureCounts;


    /**
     * Construct a blueprint of the given size. Use the given feature set to determine the count goals
     * for each feature, and start the blueprint full of the most common of these features.
     *
     * @param strictness A fractional value from 0.0 to 1.0. Each count goal is multiplied by this
     *                   value, so that, as the value approaches zero, we get lower goals and thus
     *                   more "flexible" outcomes--in other words, because all of our goals are lower,
     *                   a certain percentage (1.0-strictness) of features are unaccounted for and can
     *                   be anything. How this flexibility manifests depends on what method is being
     *                   used to develop the blueprint.
     */
    Blueprint(Dimension dimension, Set<E> featureSet, double strictness) {

        // Determine number of squares in the area.
        final int squares = dimension.getArea();

        // Get total of all feature weights (for divisor) and the most common feature.
        int totalWeight = 0;
        E mostCommonFeature = null;

        for (E feature : featureSet) {
            totalWeight += feature.getWeight();
            if (mostCommonFeature == null || feature.getWeight() > mostCommonFeature.getWeight()) {
                mostCommonFeature = feature;
            }
        }

        // Store the most common feature.
        this.mostCommonFeature = mostCommonFeature;

        // Determine what the final count of each feature should be, adjusted for strictness.
        final Map<E, Integer> featureCountGoals = new HashMap<>();
        featureCounts = new HashMap<>();
        for (E feature : featureSet) {
            final int goal = (int) (squares * feature.getWeight() / (double) totalWeight * strictness);
            featureCountGoals.put(feature, goal);
            featureCounts.put(feature, 0);
        }

        // The map will be filled with the most common feature, so add to that feature's count.
        featureCounts.put(mostCommonFeature, squares);

        // Make a base map filled with the heaviest feature index.
        this.featureSet = featureSet;
        this.featureLayout = new Array2D<>(dimension, mostCommonFeature);
        this.featureCountGoals = Collections.unmodifiableMap(featureCountGoals);
    }


    /**
     * Ensures this blueprint only works with features contained in its featureSet.
     */
    private void containsCheck(E feature) {
        if (!featureSet.contains(feature)) {
            throw new IllegalArgumentException("Feature not in blueprint's set: " + feature.toString());
        }
    }


    /**
     * Set a location on the blueprint to a feature, incrementing that feature's count while
     * decrementing the count of the feature that was replaced.
     */
    void putFeature(E placedFeature, int adjX, int adjY) {
        containsCheck(placedFeature);

        // Place the feature, storing the feature that it replaced.
        final E replacedFeature = featureLayout.put(placedFeature, adjX, adjY);

        // Increment the placed feature's count.
        final int advancedGoal = featureCounts.get(placedFeature) + 1;
        featureCounts.put(placedFeature, advancedGoal);

        // Decrement the replaced feature's count.
        final int reversedGoal = featureCounts.get(replacedFeature) - 1;
        featureCounts.put(replacedFeature, reversedGoal);
    }


    Set<E> getFeatureSet() {
        return featureSet;
    }


    E getMostCommonFeature() {
        return mostCommonFeature;
    }


    Map<E, Integer> getFeatureCountGoals() {
        return featureCountGoals;
    }


    Map<E, Integer> getFeatureCounts() {
        return featureCounts;
    }


    boolean goalIsSatisfied(E feature) {
        containsCheck(feature);
        return getFeatureCounts().get(feature) >= getFeatureCountGoals().get(feature);
    }


    boolean allGoalsSatisfied() {
        return featureSet.stream().allMatch(this::goalIsSatisfied);
    }


    /**
     * Returns (a copy of) the Array2D described by this blueprint.
     */
    public Array2D<E> build() {
        return new Array2D<>(featureLayout);
    }


    public E get(int x, int y) {
        return featureLayout.get(x, y);
    }


    public int getFeatureCount(E feature) {
        containsCheck(feature);
        return featureCountGoals.get(feature);
    }
}