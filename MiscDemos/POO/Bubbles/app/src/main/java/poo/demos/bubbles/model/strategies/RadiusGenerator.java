package poo.demos.bubbles.model.strategies;

/**
 * Contract to be supported by all radius generators.
 */
public interface RadiusGenerator {

    /**
     * Produces the radius value for the given difficulty level.
     * @param difficultyLevel The difficulty level.
     * @return The generated bubble radius value.
     */
    int computeRadius(int difficultyLevel);
}
