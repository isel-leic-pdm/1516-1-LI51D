package poo.demos.bubbles.model;

import android.util.Log;

import java.util.Random;

import poo.demos.bubbles.model.strategies.RadiusGenerator;

/**
 * Class whose instances represent the game's difficulty level.
 *
 * The game's difficulty level influences:
 * <ul>
 *     <li>The bubble's size range.</li>
 *     <li>The bubble's velocity range.</li>
 * </ul>
 */
public class Level {

    /** The bubbles' smallest center step. */
    private static final int BUBBLE_LOWEST_STEP = 1;
    /** The bubbles' highest center step. */
    private static final int BUBBLE_HIGHEST_STEP = 10;

    /** The level number. */
    private final int levelNumber;
    /** The random value generator. */
    private static final Random generator = new Random();
    /** The strategy used to generate the bubble's radius. */
    private final RadiusGenerator radiusGenerator;

    /**
     * Helper method that computes a randomly generated radius value.
     * The produced value is within the limits specified by the difficulty
     * level.
     * @return The radius value.
     */
    private int computeRadius() {

        return radiusGenerator.computeRadius(levelNumber);

        /*
        // Ugly code! We can do better... =)
        final int levelWeight = 4 * (levelNumber - 1);
        final int upperBound = BUBBLE_HIGHEST_RADIUS - levelWeight;
        final int lowerBound = upperBound - BUBBLE_LOWEST_RADIUS;

        Log.d("LEVEL", "radius [" + lowerBound + " .. " + upperBound + "]");
        Log.d("LEVEL", "radius = " + generator.nextFloat());

        // Have we reached the lowest radius?
        return lowerBound > BUBBLE_LOWEST_RADIUS ?
                generator.nextInt(upperBound - lowerBound) + lowerBound :
                BUBBLE_LOWEST_RADIUS;
        */
    }

    /**
     * Helper method that computes a randomly generated center step coordinate
     * values (i.e. velocity) and sets the given bubble with the generated values.
     * The produced values are within the limits specified by the difficulty
     * level.
     * @param bubble The {@link Bubble} instance to wich the center step will
     *               be attributed.
     * @return The received {@link Bubble} instance to enable fluent use.
     */
    private Bubble computeCenterStep(Bubble bubble) {
        // TODO: For now, we hard code it for debugging purposes
        return bubble.setCenterStep(0, -1);
    }

    /**
     * Initiates an instance with the given level number. Higher values
     * imply higher difficulty.
     * @param levelNumber The level's number in the interval [1..n[.
     * @param radiusGenerator The strategy used to generate bubble radius.
     * @throws IllegalArgumentException if the argument is not in the
     * specified range.
     */
    public Level(int levelNumber, RadiusGenerator radiusGenerator) {
        if(levelNumber <= 0 )
            throw new IllegalArgumentException();
        this.levelNumber = levelNumber;
        this.radiusGenerator = radiusGenerator;
    }

    /**
     * Creates a bubble at the given position. The created bubble has
     * a size and velocity determined by the current difficulty level.
     * @param centerX The bubble's center X coordinate.
     * @param centerY The bubble's center Y coordinate.
     * @return The created bubble.
     */
    public Bubble createBubble(float centerX, float centerY) {
        return computeCenterStep(new Bubble(centerX, centerY, computeRadius()));
    }

    /**
     * @return Gets the level number.
     */
    public int getLevelNumber() {
        return levelNumber;
    }
}
