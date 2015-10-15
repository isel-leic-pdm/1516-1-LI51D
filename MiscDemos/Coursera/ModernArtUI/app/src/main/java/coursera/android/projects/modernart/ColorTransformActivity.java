package coursera.android.projects.modernart;

import coursera.android.projects.modernart.transforms.HSVComponent;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The application's color transform selection Activity. It presents to the user the supported 
 * color transformations to be applied to each tile.
 * 
 * TODO: Create a dynamic solution to discover all supported color transformations
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public class ColorTransformActivity extends ListActivity {
	
	private class ColorTransformAdapter extends BaseAdapter {

		private int[] resIds = { R.string.transform_h_name, R.string.transform_s_name, R.string.transform_v_name };
		private HSVComponent[] transforms = { HSVComponent.HUE, HSVComponent.SATURATION, HSVComponent.VALUE };
		
		@Override
		public int getCount() { return transforms.length; }

		@Override
		public Object getItem(int position) {
			return transforms[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				final LayoutInflater inflater = ColorTransformActivity.this.getLayoutInflater();
				convertView = inflater.inflate(R.layout.transform_item, null);
			}
			
			TextView view = (TextView) convertView.findViewById(R.id.transform_name);
			view.setText(resIds[position]);
			view.setTag(transforms[position]);
			return convertView;
		}
		
	}

	public static final String OUTPUT_TRANSFORM_EXTRA = "ColorTransformActivity.Transform"; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setAdapter(new ColorTransformAdapter());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		setResult(
				RESULT_OK, 
				new Intent().putExtra(
						OUTPUT_TRANSFORM_EXTRA, 
						v.findViewById(R.id.transform_name).getTag().toString()
				)
		);
		finish();
	}
}
