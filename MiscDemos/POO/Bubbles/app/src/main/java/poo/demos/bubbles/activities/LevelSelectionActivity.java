package poo.demos.bubbles.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import poo.demos.bubbles.R;
import poo.demos.bubbles.model.GameState;
import poo.demos.bubbles.model.LevelFactory;
import poo.demos.bubbles.model.strategies.ConstantValue;

/**
 * Class that represents the game's level selection screen.
 */
public class LevelSelectionActivity extends LoggingActivityBase implements View.OnClickListener {

    /** The number of buttons per row of the table layout in portrait mode. */
    private static final int ROW_BUTTON_COUNT_PORTRAIT = 5;
    /** The number of buttons per row of the table layout in landscape mode. */
    private static final int ROW_BUTTON_COUNT_LANDSCAPE = 10;

    /** The number of buttons in each row of the table layout. */
    private int rowButtonCount;
    /** Holds references to the existing buttons. */
    private Button[] buttons;

    /**
     * Method that promotes the navigation to the game activity with the given
     * difficulty level.
     * @param level The initial difficulty level.
     */
    private void navigateToGameActivity(int level) {
        startActivity(GameActivity.makeIntent(this, level));
    }

    /**
     * Initialize the level selection button panel, according to the highest reached
     * level and screen orientation.
     * @param highestLevel The highest level reached so far.
     * @param buttonPanel The root of the button panel.
     */
    private void initializeButtonPanel(int highestLevel, TableLayout buttonPanel) {
        buttons = new Button[LevelFactory.LEVEL_COUNT];
        TableRow row = null;
        for(int count = 0; count < buttons.length; ++count) {
            if(count % rowButtonCount == 0) {
                row = new TableRow(this);
                buttonPanel.addView(row);
            }
            buttons[count] = createButton(count + 1, highestLevel, row);
        }
    }

    /**
     * Updates the button panel, enabling all buttons that correspond to
     * levels below the highest achieved level.
     */
    private void updateButtonPanel() {
        final int achievedLevel = GameState.getInstance().getAchievedLevel();
        for(int index = achievedLevel - 1; index > 0; --index) {
            if(buttons[index].isEnabled())
                return;
            buttons[index].setEnabled(true);
        }
    }

    /**
     * Creates a button and initializes it. The created button is added to the
     * received parent.
     * @param buttonNumber The button face value.
     * @param highestLevel The highest level reached so far.
     * @param parentRow The button's parent tablerow.
     * @return The newly created button.
     */
    private Button createButton(int buttonNumber, int highestLevel, TableRow parentRow) {

        // Create and initialize button
        final Button button = new Button(this);
        button.setText(Integer.toString(buttonNumber));
        button.setEnabled(buttonNumber <= highestLevel);

        final TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.column = (buttonNumber - 1) % rowButtonCount;
        button.setLayoutParams(layoutParams);

        // Register event handler
        button.setOnClickListener(this);

        // Add button to its parent
        parentRow.addView(button);

        return button;
    }

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_selection_activity_main);

        final Configuration configuration = getResources().getConfiguration();
        rowButtonCount = configuration.orientation == Configuration.ORIENTATION_PORTRAIT ?
                ROW_BUTTON_COUNT_PORTRAIT : ROW_BUTTON_COUNT_LANDSCAPE;

        final GameState gameState = GameState.createInstance(
                getPreferences(MODE_PRIVATE), getResources()
        );
        initializeButtonPanel(
                gameState.getAchievedLevel(),
                (TableLayout) findViewById(R.id.buttonPanel)
        );
    }

    /** {@inheritDoc} */
    @Override
    protected void onRestart() {
        super.onRestart();
        updateButtonPanel();
    }

    /**
     * Event handler for level selection buttons.
     * @param sourceButton The event source.
     */
    @Override
    public void onClick(View sourceButton) {
        final int level = Integer.parseInt(((Button)sourceButton).getText().toString());
        navigateToGameActivity(level);
    }
}
