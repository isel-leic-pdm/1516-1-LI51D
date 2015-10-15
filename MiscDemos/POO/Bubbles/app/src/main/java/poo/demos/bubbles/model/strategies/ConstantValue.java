package poo.demos.bubbles.model.strategies;

/**
 * Class that implements a radius value generation that always produces the
 * same value.
 */
public class ConstantValue implements RadiusGenerator {

    /** The radius value to be systematically produced. */
    private final int value;

    /**
     * Initiates an instance with the given constant value.
     * @param value The radius value.
     */
    public ConstantValue(int value) {
        this.value = value;
    }

    /**
     * Initiates an instance with the given parameters. This constructor is used to instantiate
     * through reflection, passing in the required configuration parameters.
     * The received array is expected to have on position bearing the fixed radius value.
     * @param params The expected parameters.
     */
    public ConstantValue(Integer[] params) {
        this(params[0]);
    }

    @Override
    public int computeRadius(int level) {
        return value;
    }
}
