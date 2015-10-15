package poo.demos.bubbles.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import poo.demos.bubbles.R;
import poo.demos.bubbles.model.GameState;

/**
 * View responsible for displaying the game state.
 */
public class GameStateView extends LinearLayout {

    /** The label used to display the current score. */
    private final TextView score;
    /** The label used to display the current level. */
    private final TextView level;

    /** The associated {@link GameState} instance. */
    private GameState model;

    /**
     * Updates the view's contents in order to visually reflect the state changes.
     */
    private void update() {
        score.setText(Integer.toString(model.getCurrentScore()));
        level.setText(Integer.toString(model.getCurrentLevel().getLevelNumber()));
    }

    /**
     * Initiates an instance.
     */
    public GameStateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        inflater.inflate(R.layout.view_game_state, this, true);

        score = (TextView) findViewById(R.id.scoreView);
        level = (TextView) findViewById(R.id.levelView);

    }

    /**
     * Sets the view's associated game state
     * @param model The {@link GameState} instance.
     */
    public void setGameState(GameState model) {
        this.model = model;
        model.setChangeListener(new GameState.ChangeListener() {
            @Override
            public void onChange(GameState source) {
                update();
            }
        });
        update();
    }
}
