package coursera.android.projects.modernart;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import coursera.android.projects.modernart.widgets.ColoredTile;

/**
 * Fragment that displays the modern art painting. If the hosting Activity
 * implements the {@link #PaintingFragment.OnTileClickListener} interface, 
 * the fragment will produce tile click events.  
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public class PaintingFragment extends Fragment {

	/**
	 * Contract to be supported by hosting Activities to enable them to receive
	 * tile click events.
	 */
	public static interface OnTileClickListener {
		public void onTileClick(ColoredTile source);
	}
	
	/** Contains all the painting's tiles. */
	private ArrayList<ColoredTile> tiles;

	/** Used to forward tile clicks to hosting Activity */
	private View.OnClickListener tileClickDispatcher;
	
	/**
	 * Helper method used to register all the paintings tiles. 
	 */
	private void registerPaintingTiles(ViewGroup parent)
	{
		// Register all parent's childs that are painting tiles and to which a color transform
		// can eventually be applied
		for(int childIndex = 0; childIndex < parent.getChildCount(); ++childIndex)
		{
			View child = parent.getChildAt(childIndex);
			if(child instanceof ColoredTile) {
				ColoredTile tile = (ColoredTile) child;
				child.setOnClickListener(tileClickDispatcher);
				tiles.add(tile);
			}
			else
				if(child instanceof ViewGroup)
					// Search the tree starting at the current child
					registerPaintingTiles((ViewGroup) child);
		}
	}
	
	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		// Set up the tile event dispatcher, if the hosting Activity supports the
		// required contract
		tileClickDispatcher = !(activity instanceof OnTileClickListener) ? null : 
			new View.OnClickListener() {
				@Override
				public void onClick(View tile) {
					((OnTileClickListener) activity).onTileClick((ColoredTile) tile);
				}
			};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootPane = (ViewGroup) inflater.inflate(R.layout.painting_fragment, container);
		tiles = new ArrayList<ColoredTile>();
		registerPaintingTiles(rootPane);
		return rootPane;
	}
	
	/**
	 * Applies the given color transformation percentage to all the apinting's tiles.
	 * 
	 * @param percentage The percentage of color transformation to apply. 
	 */
	public void applyTransform(int percentage) {
		for(ColoredTile tile : tiles)
			tile.applyTransform(percentage);
	}
}
