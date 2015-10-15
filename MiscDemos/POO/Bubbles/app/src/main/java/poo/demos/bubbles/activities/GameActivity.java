package poo.demos.bubbles.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import poo.demos.bubbles.R;
import poo.demos.bubbles.model.Bubble;
import poo.demos.bubbles.model.GameState;
import poo.demos.bubbles.view.BubbleView;
import poo.demos.bubbles.view.GameStateView;


/**
 * Activity that defines the application's game screen.
 */
public class GameActivity extends LoggingActivityBase implements View.OnTouchListener {

    /** The number of existing levels. */
    public static final int LEVEL_COUNT = 30;

    /** The application's frame rate, in milli-seconds. */
    private static final int FRAME_RATE = 10;

    /** The intent's extra key associated to the difficulty level. */
    private static final String LEVEL_SELECTION_KEY = "LEVEL_KEY";
    /** The default starting level number. */
    private static final int DEFAULT_START_LEVEL = 1;
    /** The key used to store and retrieve instance state. */
    private static final String BUBBLES_STATE_KEY = "GameActivity.BubblesState";
    /** The key used to store and retrieve the current level state. */
    private static final String CURRENT_LEVEL_STATE_KEY = "GameActivity.CurrentLevelState";

    /** The pane containing the bubbles. */
    private ViewGroup rootPane;
    /** Holds the bubble's unscaled bitmap. */
    private Bitmap unscaledBubbleImage;

    /** The game's current state. */
    private GameState gameState;
    /** The game's running state. True indicates that the game is paused.*/
    private boolean isPaused;
    /** The object that executes animation related work. */
    final Runnable stepWork = new Runnable() {
        @Override
        public void run() {
            if(isPaused) // next work item will not be scheduled
                return;
            doStep();
            rootPane.postDelayed(this, FRAME_RATE);
        }
    };

    /**
     * Factory method that produces the intent to be used to activate the current
     * Activity.
     * @param sender The sender activity.
     * @param level The selected level.
     * @return The requested {@link Intent}.
     */
    public static Intent makeIntent(Activity sender, int level) {
        return new Intent(sender, GameActivity.class)
                .putExtra(LEVEL_SELECTION_KEY, level);
    }

    /**
     * Gets the starting level number, contained within the received intent.
     * @return The game's starting level.
     */
    private int getStartingLevel() {
        final int startingLevel = getIntent().getIntExtra(LEVEL_SELECTION_KEY, DEFAULT_START_LEVEL);
        Log.d(TAG, "Starting level is " + startingLevel);
        return startingLevel;
    }

    /**
     * Method that performs level transition.
     */
    private void startNextLevel() {
        gameState.nextLevel();
        Log.d(TAG, "Moved to level " + gameState.getCurrentLevel().getLevelNumber());
    }

    /**
     * Method that performs the computation required in each animation step.
     */
    private void doStep() {
        doStepBubbles();
    }

    /**
     * Moves all existing bubbles by their specified distance delta and
     * rotation delta.
     */
    private void doStepBubbles() {
        final ArrayList<BubbleView> toRemove = new ArrayList<>();
        // Find out which bubbles should be removed
        for(int idx = 0; idx < rootPane.getChildCount(); ++idx) {
            BubbleView bubble = (BubbleView) rootPane.getChildAt(idx);
            if(bubble.isOutOfView()) {
                toRemove.add(bubble);
                continue;
            }
            bubble.getBubbleState().doStep();
        }

        // Removing views
        for(BubbleView bubble : toRemove)
            rootPane.removeView(bubble);
    }

    /**
     * Creates a bubble on the given coordinates, adding it to the
     * root pane.
     * @param x The new bubble's X coordinate.
     * @param y The new bubble's Y coordinate.
     */
    private void createBubble(float x, float y) {
        // TODO: Just for testing. Remove it later.
        // Half the times, we start a new level. Just for testing.
        if( Math.random() >= 0.5 )
            startNextLevel();

        rootPane.addView(
                new BubbleView(
                        this,
                        gameState.getCurrentLevel().createBubble(x, y),
                        unscaledBubbleImage
                )
        );
    }

    /**
     * Destroys the given bubble, removing it from the root pane.
     * @param collidingBubble The bubble to be destroyed.
     */
    private void destroyBubble(BubbleView collidingBubble) {
        rootPane.removeView(collidingBubble);
    }

    /**
     * Checks if any bubble contains the given coordinates (i.e. colision
     * detection).
     * @param x The X value of the coordinate to be checked.
     * @param y The Y value of the coordinate to be checked.
     * @return The bubble view instance found at the given position,
     * or {@literal null} if none was found.
     */
    private BubbleView detectCollision(float x, float y) {
        for(int idx = rootPane.getChildCount() - 1; idx >= 0; --idx) {
            BubbleView bubbleView = (BubbleView) rootPane.getChildAt(idx);
            if(bubbleView.getBubbleState().contains(x, y))
                return bubbleView;
        }

        return null;
    }

    /**
     * Saves the current game state (i.e. bubbles' state) in the given bundle.
     * @param outState The {@link Bundle} instance where the state is to be stored.
     */
    private void saveGameState(Bundle outState) {
        // Save current level
        outState.putInt(CURRENT_LEVEL_STATE_KEY, gameState.getCurrentLevel().getLevelNumber());
        // Load bubbles' state
        final ArrayList<Bubble> state = new ArrayList<>();
        for(int idx = 0; idx < rootPane.getChildCount(); ++idx) {
            Bubble bubble = ((BubbleView) rootPane.getChildAt(idx)).getBubbleState();
            state.add(bubble);
        }
        outState.putParcelableArrayList(BUBBLES_STATE_KEY, state);
    }

    /**
     * Loads the current game state (i.e. bubbles' state) from the given bundle.
     */
    private void loadGameState(Bundle inState) {
        // Load current level
        gameState.setCurrentLevel(inState.getInt(CURRENT_LEVEL_STATE_KEY));
        // Load bubbles' state
        final ArrayList<Bubble> bubbles = inState.getParcelableArrayList(BUBBLES_STATE_KEY);
        for(Bubble bubble : bubbles)
            rootPane.addView(new BubbleView(this, bubble, unscaledBubbleImage));

        // TODO: re-compute positions
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameState = GameState.getInstance();
        gameState.setCurrentLevel(getStartingLevel());

        rootPane = (ViewGroup) findViewById(R.id.root_pane);
        final GameStateView gameStateView = (GameStateView) findViewById(R.id.gameStateView);
        gameStateView.setGameState(gameState);

        rootPane.setOnTouchListener(this);

        // Load basic bubble Bitmap
        unscaledBubbleImage = BitmapFactory.decodeResource(
                getResources(), R.drawable.b64
        );

        isPaused = true;

        if(savedInstanceState != null) {
            // Load bubbles' state and create bubble views accordingly
            loadGameState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveGameState(outState);
    }

    @Override
    public boolean onTouch(View source, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            BubbleView collidingBubble = detectCollision(event.getX(), event.getY());
            if (collidingBubble == null)
                // TODO: For debugging purposes. Remove it later.
                createBubble(event.getX(), event.getY());
            else
                destroyBubble(collidingBubble);
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        rootPane.postDelayed(stepWork, FRAME_RATE);
    }
}
