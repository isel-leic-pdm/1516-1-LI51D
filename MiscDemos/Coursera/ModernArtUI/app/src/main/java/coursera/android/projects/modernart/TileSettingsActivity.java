package coursera.android.projects.modernart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import coursera.android.projects.modernart.transforms.HSVComponent;
import coursera.android.projects.modernart.widgets.ColorPickerView;

/**
 * The application's tile settings Activity. It presents to the user the supported 
 * tile configuration settings, namely, color and transformation.
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public class TileSettingsActivity extends Activity 
				implements SeekBar.OnSeekBarChangeListener, ColorPickerView.OnColorChangedListener {

	public static final String INPUT_COLOR_EXTRA = "TileSettingsActivity.InputColor"; 
	public static final String OUTPUT_COLOR_EXTRA = "TileSettingsActivity.OutputColor"; 
	public static final String OUTPUT_TRANSFORM_EXTRA = "TileSettingsActivity.Transformation"; 
	
	private static final int TILE_COLOR_TRANSFORM_REQUEST_CODE = 0;

	private ColorPickerView colorPicker;
	private SeekBar saturationBar;
	private SeekBar valueBar;
	private float[] hsv;
	private String colorTransformation;
	
	private void updateColorSettingsDisplay(int color) {
		Color.colorToHSV(color, hsv);
		saturationBar.setProgress((int)(hsv[HSVComponent.SATURATION.ordinal()]*100));
		valueBar.setProgress((int)(hsv[HSVComponent.VALUE.ordinal()]*100));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		hsv = new float[HSVComponent.values().length];
		int inputColor = getIntent().getIntExtra(INPUT_COLOR_EXTRA, 0);
		
		colorPicker = (ColorPickerView) findViewById(R.id.color_picker);
		colorPicker.setOnColorChangedListener(this);
		colorPicker.setColor(inputColor);
		
		saturationBar = (SeekBar) findViewById(R.id.saturation_seek_bar);
		valueBar = (SeekBar) findViewById(R.id.value_seek_bar);

		updateColorSettingsDisplay(inputColor);

		// Set up behavior
		saturationBar.setOnSeekBarChangeListener(this);
		valueBar.setOnSeekBarChangeListener(this);
		
		findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { finish(); }
		});
		
		findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				Intent resultData = new Intent().putExtra(OUTPUT_COLOR_EXTRA, colorPicker.getColor());
				if(colorTransformation != null)
					resultData.putExtra(OUTPUT_TRANSFORM_EXTRA, colorTransformation);
				setResult(RESULT_OK, resultData);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tile_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_transform) {
			startActivityForResult(new Intent(this, ColorTransformActivity.class), TILE_COLOR_TRANSFORM_REQUEST_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Color.colorToHSV(colorPicker.getColor(), hsv);
		hsv[HSVComponent.SATURATION.ordinal()] = (float) (saturationBar.getProgress() / 100.0);
		hsv[HSVComponent.VALUE.ordinal()] = (float) (valueBar.getProgress() / 100.0);
		colorPicker.setColor(Color.HSVToColor(hsv));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TILE_COLOR_TRANSFORM_REQUEST_CODE && resultCode == RESULT_OK) {
			colorTransformation = data.getStringExtra(ColorTransformActivity.OUTPUT_TRANSFORM_EXTRA);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) { }

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) { }

	@Override
	public void colorChanged(int color) {
		updateColorSettingsDisplay(color);
	}
}
