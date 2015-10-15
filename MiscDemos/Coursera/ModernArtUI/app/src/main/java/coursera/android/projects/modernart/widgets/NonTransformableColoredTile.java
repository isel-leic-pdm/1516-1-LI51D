package coursera.android.projects.modernart.widgets;

import coursera.android.projects.modernart.transforms.ColorTransformation;
import android.content.Context;
import android.util.AttributeSet;

/**
 * View that displays a colored tile without color transformation.
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public class NonTransformableColoredTile extends ColoredTile {

	/**
	 * Class that implements the NO OP color transformation.	
	 */
	private static final ColorTransformation NO_TRANSFORMATION = new ColorTransformation() {
		@Override
		public int apply(int inputColor, int percentage) {
			return inputColor;
		}
	};
	
	/**
	 * Initiates an instance with the given application context. This constructor
	 * is useful to simplify code instantiation of the view.
	 * 
	 * @param context The associated application context
	 */
	public NonTransformableColoredTile(Context context) {
		super(context, NO_TRANSFORMATION);
	}

	/**
	 * Initiates an instance with the given application context and base color. 
	 * This constructor is useful to simplify code instantiation of the view.
	 * 
	 * @param context The associated application context.
	 * @param color The view's initial color encoded as an ARGB value.
	 */
	public NonTransformableColoredTile(Context context, int color) {
		super(context, color, NO_TRANSFORMATION);
	}

	/**
	 * Initiates an instance with the given application context and set of 
	 * attributes. This constructor is required to enable instantiation when
	 * inflating from a XML resource file.
	 * 
	 * @param context The associated application context
	 * @param attrs The set of attributes to be applied to the instance 
	 */
	public NonTransformableColoredTile(Context context, AttributeSet attrs) {
		super(context, attrs);
		// If the base attribute has been set through XML, reset it. 
		transform = NO_TRANSFORMATION;
	}
}
