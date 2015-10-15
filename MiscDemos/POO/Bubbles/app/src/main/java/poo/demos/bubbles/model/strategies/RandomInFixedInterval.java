package poo.demos.bubbles.model.strategies;

import java.util.Random;

/**
 * Class that implements a radius value generation that always produces a
 * random value within the specified fixed interval.
 */
public class RandomInFixedInterval implements RadiusGenerator {

    /** The radius interval lower bound. */
    private final int lowerBound;
    /** The radius interval upper bound. */
    private final int upperBound;

    /** The random value generator. */
    private static Random generator = new Random();

    /**
     * Initiates an instance with the given interval limits.
     * @param lowerBound The radius interval lower bound.
     * @param upperBound The radius interval upper bound.
     */
    public RandomInFixedInterval(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Initiates an instance with the given parameters. This constructor is used to instantiate
     * through reflection, passing in the required configuration parameters.
     * The received array is expected to have on position bearing the radius generation interval.
     * @param params The expected parameters.
     */
    public RandomInFixedInterval(Integer[] params) {
        this(params[0], params[1]);
    }

    /** {@inheritDoc} */
    @Override
    public int computeRadius(int level) {
        final int range = upperBound - lowerBound;
        return generator.nextInt(range) + lowerBound;
    }
}
