package coursera.android.projects.modernart.widgets;

import coursera.android.projects.modernart.transforms.ColorTransformation;
import coursera.android.projects.modernart.transforms.HSVComponent;
import coursera.android.projects.modernart.transforms.LinearHSVTransformation;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * View that displays a colored tile to which a color transformation can
 * be applied. The transformation is always applied to the view's base 
 * color, that is, its color set upon instantiation or through the method
 * #setBaseColor.
 * 
 * The implementation makes use of the Strategy design pattern. 
 *  
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public class ColoredTile extends View {

	/** The tile's color XML attribute name. */
	private static final String COLOR_ATTRIBUTE = "background";
	/** The tile's color XML attribute namespace. */
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
	/** The color that is used if no other is specified. */
	private static final int DEFAULT_COLOR = Color.BLACK;
	/** The view's default color transformation. */
	private static final ColorTransformation DEFAULT_TRANSFORMATION = 
			new LinearHSVTransformation(HSVComponent.SATURATION);
	
	/** The brush used to draw the view. */
	protected Paint brush;
	/** The color transformation to be used.  */
	protected ColorTransformation transform;
	/** The view's base color. */ 
	protected int baseColor;
	/** The current percentage of color transformation applied to the view. */
	protected int currentTransformPercentage;

	/**
	 * Helper method that parses the given string and returns the corresponding
	 * color value. If the received string is invalid, the method returns the 
	 * default color's ARGB value.
	 * 
	 * @param colorString The string bearing the color encoding.
	 * @return The color's ARGB value.
	 */
	private static int parseColorString(String colorString) {
		int color = DEFAULT_COLOR;
		if(colorString != null)
		{
			try { color = Color.parseColor(colorString); }
			catch(IllegalArgumentException iae) { }
		}
		return color;
	}
	
	/**
	 * Initiates the current instance with the given color and transformation.
	 * @param color The color's ARGB value.
	 * @param transformation The color transformation to be applied.
	 */
	private void init(int color, ColorTransformation transformation) {
		// No need to apply the color transformation because, initially, its
		// value is 0
		currentTransformPercentage = 0;
		transform = transformation;
		baseColor = color;
		brush = new Paint();
		brush.setStyle(Paint.Style.FILL_AND_STROKE);
		brush.setColor(color);
	}
	
	/**
	 * Initiates an instance with the given application context. The resulting instance's
	 * color and color transformation are set to their default values. This constructor is 
	 * provided as a convenience constructor to simplify instantiation in Java code of 
	 * the view.
	 * 
	 * @param context The associated application context
	 */
	public ColoredTile(Context context) {
		this(context, DEFAULT_COLOR, DEFAULT_TRANSFORMATION);
	}
	
	/**
	 * Initiates an instance with the given application context and color transformation. 
	 * The resulting instance's color is set to its default value. This constructor is 
	 * provided as a convenience constructor to simplify instantiation in Java code of 
	 * the view.
	 * 
	 * @param context The associated application context
	 * @param transform The color transformation to be applied.
	 */
	public ColoredTile(Context context, ColorTransformation transform) {
		this(context, DEFAULT_COLOR, transform);
	}
	
	/**
	 * Initiates an instance with the given application context and color. The resulting 
	 * instance's color transformation is set to its default value. This constructor is 
	 * provided as a convenience constructor to simplify instantiation in Java code of 
	 * the view.
	 * 
	 * @param context The associated application context.
	 * @param color The view's color encoded as an ARGB value.
	 */
	public ColoredTile(Context context, int color) {
		this(context, color, DEFAULT_TRANSFORMATION);
	}
	
	/**
	 * Initiates an instance with the given application context, color and color
	 * transformation. This constructor is provided as a convenience constructor 
	 * to simplify instantiation in Java code of the view.
	 * 
	 * @param context The associated application context.
	 * @param color The view's color encoded as an ARGB value.
	 * @param transform The color transformation to be applied.
	 */
	public ColoredTile(Context context, int color, ColorTransformation transform) {
		super(context);
		init(color, transform);
	}

	/**
	 * Initiates an instance with the given application context and set of 
	 * attributes. This constructor is required to enable instantiation when
	 * inflating from a XML resource file.
	 * 
	 * @param context The associated application context.
	 * @param attrs The set of attributes to be applied to the instance.
	 */
	public ColoredTile(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(
			parseColorString(attrs.getAttributeValue(NAMESPACE, COLOR_ATTRIBUTE)),
			DEFAULT_TRANSFORMATION
		);
	}

	/** {@inheritDoc} */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(0, 0, getWidth(), getHeight(), brush);
	}
	
	private void internalApplyTransform() {
		brush.setColor(transform.apply(baseColor, currentTransformPercentage));
		invalidate();
	}

	/**
	 * Gets the view's current color. If a color transformation has been applied, 
	 * the resulting color is produced.
	 * @return The current color encoded as an ARGB value
	 */
	public int getColor() { 
		return brush.getColor();
	}
	
	/**
	 * Sets the view's current base color, that is, the color to which subsequent 
	 * transformations will be applied. This method also changes the view's current
	 * color.
	 * @param baseColor The view's new base color, encoded in ARGB. 
	 */
	public void setBaseColor(int baseColor) {
		this.baseColor = baseColor;
		internalApplyTransform();
	}
	
	/**
	 * Gets the view's current base color, that is, the color to which transformations
	 * are applied.
	 * @return The view's current base color, encoded in ARGB.
	 */
	public int getBaseColor() {
		return baseColor;
	}
	
	/**
	 * Sets the color transformation to be used.
	 * 
	 * @param transform The color transformation strategy instance, or {@literal null}
	 * to disable transformation
	 */
	public void setTransformation(ColorTransformation transform) {
		this.transform = transform;
		internalApplyTransform();
	}
	
	/**
	 * Applies the view's color transformation (if one exists). The transformation is
	 * always applied to the view's base color.
	 * 
	 * @param percentage The percentage [0..100] value to be used in the color 
	 * transformation, ranging from {@literal 0} that means that the transformation is 
	 * not applied, to {@literal 100}  that means that a full trabsformation is applied.
	 */
	public void applyTransform(int percentage) {
		currentTransformPercentage = percentage;
		internalApplyTransform();
	}
}
