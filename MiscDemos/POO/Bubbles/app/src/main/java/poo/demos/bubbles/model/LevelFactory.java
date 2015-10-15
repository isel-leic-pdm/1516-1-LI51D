package poo.demos.bubbles.model;

import android.content.res.Resources;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

import poo.demos.bubbles.R;
import poo.demos.bubbles.model.strategies.ConstantValue;
import poo.demos.bubbles.model.strategies.RadiusGenerator;

/**
 * Class used to fabricate {@link Level} instances, parametrized by the appropriate strategies.
 */
public class LevelFactory {

    /** The number of existing levels. */
    public static final int LEVEL_COUNT = 40;

    /**
     * Class whose instances are used to report configuration errors.
     */
    private static class ConfigException extends Exception {
        public ConfigException() {}
        public ConfigException(String message) { super(message); }
    }

    /**
     * Class whose instances are used to associate generation strategies to level number intervals.
     */
    private static class StrategyInInterval {

        /** The bounds of the level interval to which the strategy is associated. */
        private int lowerBound, upperBound;
        /** The strategy instance to be used in the specified interval. */
        private RadiusGenerator strategy;

        /**
         * Helper method used to initiate an instance.
         * @param clazz The strategy type.
         * @param params The string representation of the parameters to be used to initiate
         *               the strategy.
         * @throws Exception If an error occurred while initiating the strategy.
         */
        private void initStrategy(Class<?> clazz, String[] params) throws Exception {
            final Integer[] values = new Integer[params.length];
            for(int idx = 0; idx < params.length; ++idx)
                values[idx] = Integer.parseInt(params[idx]);

            final Constructor<?> ctor = clazz.getConstructor(Integer[].class);
            strategy = (RadiusGenerator) ctor.newInstance(new Object[] { values });
        }

        /**
         * Initiates an instance with the information extracted from the given configuration file.
         * @param configInfo A {@link Scanner} instance that gives access to the configuration file contents.
         * @throws ConfigException if an error occurred while parsing the configuration file.
         */
        public StrategyInInterval(Scanner configInfo) throws ConfigException {
            final String[] interval = configInfo.next().trim().split("-");
            lowerBound = Integer.parseInt(interval[0]);
            upperBound = Integer.parseInt(interval[1]);
            try {
                initStrategy(
                        Class.forName(configInfo.next()),
                        configInfo.nextLine().trim().split(" ")
                );
            } catch (Exception e) {
                throw new ConfigException(e.getMessage());
            }
        }

        /**
         * Checks if the given level is within this instance's bounds.
         * @param levelNumber The level number.
         * @return {@literal true} if the given level number is within this instance's bounds,
         * {@literal false} otherwise.
         */
        public boolean isWithinBounds(int levelNumber) {
            return levelNumber >= lowerBound && levelNumber < upperBound;
        }

        /**
         * @return The associated radius generation strategy.
         */
        public RadiusGenerator getRadiusGenerationStrategy() {
            return strategy;
        }
    }

    /** The bubbles' default radius. */
    private static final int BUBBLE_DEFAULT_RADIUS = 64;
    /** The default radius generation strategy. */
    private static final RadiusGenerator defaultRadiusGenerator =
            new ConstantValue(BUBBLE_DEFAULT_RADIUS / 2);

    /**
     * Holds the array of configured generation strategies per level intervals, or {@literal null}
     * if an error occurred while initializing.
     */
    private final StrategyInInterval[] strategies;

    /**
     * Method that loads the configured radius generation strategies, returning the
     * @param inputStream The stream from where the configuration will be loaded.
     * @return The array bearing the configured strategies, or {@literal null} if an error occurred.
     */
    private StrategyInInterval[] loadStrategies(InputStream inputStream) {
        final ArrayList<StrategyInInterval> configuredStrategies = new ArrayList<>();
        final Scanner configInfo = new Scanner(inputStream);
        try {
            while(configInfo.hasNext())
                configuredStrategies.add(new StrategyInInterval(configInfo));
        } catch (ConfigException e) {
            return null;
        }
        configInfo.close();
        return configuredStrategies.toArray(new StrategyInInterval[configuredStrategies.size()]);
    }

    /**
     * Gets the radius generation strategy for the given level.
     * @param levelNumber The level number.
     * @return The radius generation strategy instance.
     */
    private RadiusGenerator findStrategyForLevel(int levelNumber) {
        for(StrategyInInterval strategy : strategies)
                if(strategy.isWithinBounds(levelNumber))
                    return strategy.getRadiusGenerationStrategy();

        return defaultRadiusGenerator;
    }

    /**
     * Initiates an instance with the given resources manager.
     */
    public LevelFactory(Resources resources) {
        strategies = loadStrategies(resources.openRawResource(R.raw.radius_config));
    }

    /**
     * Creates a {@link Level} instance for the given level number, parametrized with the
     * appropriate strategies.
     * @param levelNumber The number of the {@link Level} instance to be created.
     * @return The newly created {@link Level} instance.
     */
    public Level createLevel(int levelNumber) {
        levelNumber = Math.min(levelNumber, LEVEL_COUNT);
        return new Level(levelNumber, findStrategyForLevel(levelNumber));
    }
}
