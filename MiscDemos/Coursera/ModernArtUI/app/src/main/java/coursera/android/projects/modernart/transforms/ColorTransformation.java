package coursera.android.projects.modernart.transforms;

/**
 * Contract to be supported by all color transformations.
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 */
public interface ColorTransformation {

	/**
	 * Applies a given concrete color transformation, producing the corresponding 
	 * result. The transformation's input and output values are always expressed
	 * in ARGB encoding, to ensure compatibility with Android's view system. 
	 * 
	 * @param inputColor The input color.
	 * @param percentage The percentage value [0..100] of the effect to apply. 
	 * @return The transformation result.
	 */
	public int apply(int inputColor, int percentage);
}
