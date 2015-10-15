package poo.demos.bubbles.model;

import android.content.SharedPreferences;
import android.content.res.Resources;

/**
 * Class used to encapsulate access to the game's state.
 * Because it should only exist one instance of this class, the
 * Singleton pattern is used.
 */
public class GameState {

    /**
     * Contract to be supported by types interested in receiving
     * game state changes notifications
     */
    public interface ChangeListener {
        void onChange(GameState source);
    }

    /** The singleton instance. */
    private static GameState instance = null;

    /** The key associated to the maximum achieved level durable state. */
    private static final String ACHIEVED_LEVEL_KEY = "achieved_level";

    /** The number of the maximum level achieved so far. */
    private int achievedLevel;
    /** The current level. */
    private Level currentLevel;
    /** The level factory object. */
    private LevelFactory levelFactory;
    /** The current score. */
    private int currentScore;
    /** The durable data repository. */
    private SharedPreferences durableState;

    /** The listener of change events. */
    private ChangeListener listener;

    /**
     * Produces change state events, that is, notifies an eventual interested party.
     */
    private void fireChangeEvent() {
        if(listener != null)
            listener.onChange(this);
    }

    /**
     * Registers the given listener to receive state changes notifications.
     * @param listener The listener object.
     */
    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private GameState(SharedPreferences durableState, Resources resources) {
        levelFactory = new LevelFactory(resources);
        currentLevel = levelFactory.createLevel(1);
        this.durableState = durableState;
        achievedLevel = durableState.getInt(ACHIEVED_LEVEL_KEY, currentLevel.getLevelNumber());
    }

    /**
     * @return Creates the singleton instance with the given durable state and resource manager
     * @throws IllegalArgumentException if {@literal durableState} is {@value null}.
     */
    public static GameState createInstance(SharedPreferences durableState, Resources resources) {
        if(durableState == null)
            throw new IllegalArgumentException();

        return instance = new GameState(durableState, resources);
    }

    /**
     * @return Gets the singleton instance.
     * @throws IllegalStateException if the instance has not yet been created.
     */
    public static GameState getInstance() {
        if(instance == null)
            throw new IllegalStateException();

        return instance;
    }

    /**
     * @return Gets the maximum level achieved so far.
     */
    public int getAchievedLevel() {
        return achievedLevel;
    }

    /**
     * @return Gets the game's current level.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets the game's current level, returning the corresponding {@link Level} instance.
     * The received level number cannot be higher than the highest achieved so far.
     * @param levelNumber The new level number.
     * @return The {@link Level} instance corresponding to the new level;
     * @throws IllegalArgumentException if the argument is not a positive value.
     * @throws IllegalStateException if the argument is higher then the maximum
     * level reached so far.
     */
    public Level setCurrentLevel(int levelNumber) {
        // Is it a legal level number?
        if(levelNumber <= 0)
            throw new IllegalArgumentException();
        // Is it a level number currently playable?
        if(levelNumber > achievedLevel)
            throw new IllegalStateException();

        currentLevel = levelFactory.createLevel(levelNumber);
        fireChangeEvent();
        return currentLevel;
    }

    /**
     * Advances the game's state to the next level, returning the corresponding {@link Level}
     * instance and updating the highest level achieved so far, if that's the case.
     * @return The next {@link Level} instance.
     */
    public Level nextLevel() {
        // Set the game state to the new level
        currentLevel = levelFactory.createLevel(currentLevel.getLevelNumber() + 1);
        // Is it the maximum level achieved so far?
        achievedLevel = Math.max(achievedLevel, currentLevel.getLevelNumber());
        final SharedPreferences.Editor durableStateEditor = durableState.edit();
        durableStateEditor.putInt(ACHIEVED_LEVEL_KEY, achievedLevel);
        durableStateEditor.apply();

        fireChangeEvent();
        return currentLevel;
    }

    /**
     * @return The current score.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Adds the given points to the current score.
     * @param points The points to be added.
     */
    public void addToCurrentScore(int points) {
        currentScore += points;
        fireChangeEvent();
    }
}
