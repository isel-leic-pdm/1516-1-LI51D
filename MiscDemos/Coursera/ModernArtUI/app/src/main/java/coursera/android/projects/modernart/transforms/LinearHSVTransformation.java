package coursera.android.projects.modernart.transforms;

import android.graphics.Color;

/**
 * A simple color transformation that applies a linear variation to the color's
 * selected HSV component. 
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public class LinearHSVTransformation implements ColorTransformation {

	/** 
	 * An array used to store the color's HSV components. It is used as a field 
	 * to prevent an array instantiation each time the transformation is applied.
	 */ 
	private final float[] hsv;
	
	/** The HSV component that will be affected by the transformation. */
	private final HSVComponent transform;

	/**
	 * Instantiates the transformation.
	 * 
	 * @param transform The HSV component to be affected by the transformation. 
	 */
	public LinearHSVTransformation(HSVComponent transform) {
		hsv = new float[HSVComponent.values().length];
		this.transform = transform;
	}
	
	/** {@inheritDoc} */
	@Override
	public int apply(int inputColor, int percentage) {
		Color.colorToHSV(inputColor, hsv);
		hsv[transform.ordinal()] *= ((float) percentage / 100);
		return Color.HSVToColor(hsv);
	}
}
