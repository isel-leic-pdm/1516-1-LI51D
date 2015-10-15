package coursera.android.projects.modernart;


import coursera.android.projects.modernart.R;
import coursera.android.projects.modernart.transforms.HSVComponent;
import coursera.android.projects.modernart.transforms.LinearHSVTransformation;
import coursera.android.projects.modernart.widgets.ColoredTile;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * The application's main Activity. It shows the modern art painting and promotes navigation
 * to the tile's configuration Activity.
 * 
 * TODO: Handle save and restore instance state to deal with orientation changes 
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public class MainActivity extends Activity implements PaintingFragment.OnTileClickListener {

	private static final String authorLinkedInURL = "https://www.linkedin.com/in/palbp";
	private static final String momaURL = "http://www.moma.org/m";
	
	private static final int TILE_SETTINGS_REQUEST_CODE = 0;
	
	private ColoredTile lastClickedTile;
	private SeekBar transformBar;

	/**
	 * Helper method that translates progress bar progress values to color transformation 
	 * percentages.
	 */
	private int computeTransformPercentage(int progress) {
		final int MAX_VALUE = 100;
		return MAX_VALUE - progress;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final PaintingFragment painting = (PaintingFragment) getFragmentManager().findFragmentById(R.id.painting_fragment);
		transformBar = (SeekBar) findViewById(R.id.seek_bar);
		transformBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				painting.applyTransform(computeTransformPercentage(progress));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	@SuppressLint("InflateParams") 
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_more) {
			new AlertDialog.Builder(this)
				.setCancelable(true)
				.setView(getLayoutInflater().inflate(R.layout.more_dialog_view, null))
				.setPositiveButton(R.string.more_dialog_ok_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(momaURL)));
					}
				})
				.setNegativeButton(R.string.more_dialog_cancel_button, null)
				.show();
			return true;
		}
		if (id == R.id.action_author) {
	        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorLinkedInURL)));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTileClick(ColoredTile source) {
		lastClickedTile = source;
		startActivityForResult(
				new Intent(this, TileSettingsActivity.class)
					.putExtra(TileSettingsActivity.INPUT_COLOR_EXTRA, source.getColor()),
				TILE_SETTINGS_REQUEST_CODE
		);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TILE_SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
			final int newBaseColor = data.getIntExtra(
					TileSettingsActivity.OUTPUT_COLOR_EXTRA, 
					lastClickedTile.getColor()
			);
			
			final String transformation = data.getStringExtra(TileSettingsActivity.OUTPUT_TRANSFORM_EXTRA);
			if(transformation != null)
			{
				HSVComponent component = HSVComponent.valueOf(transformation);
				lastClickedTile.setTransformation(new LinearHSVTransformation(component));
			}
			
			// Preventing an awkward visual effect by delaying the visual manifestation 
			// of the transformation
			final int NO_TRANSFORMATION = 0;
			final int DELAY = 1500;
			lastClickedTile.setBaseColor(newBaseColor);
			lastClickedTile.applyTransform(computeTransformPercentage(NO_TRANSFORMATION));
			lastClickedTile.postDelayed(
					new Runnable() {
						final ColoredTile delayedEffectTarget = lastClickedTile;
						@Override 
						public void run() { 
							delayedEffectTarget.applyTransform(computeTransformPercentage(transformBar.getProgress())); 
						}
					}, DELAY
			);
		}
	}
}
