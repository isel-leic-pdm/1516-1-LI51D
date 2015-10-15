package coursera.android.projects.modernart.transforms;

/** 
 * Enumeration for the HSV color encoding components.
 * 
 * @author Paulo Pereira "https://www.linkedin.com/in/palbp"
 **/ 
public enum HSVComponent { 
	HUE(350.0f), SATURATION(1.0f), VALUE(1.0f);
	
	/** The component's maximum value. */
	private final float maxValue;
	
	/** Initiates an instance with the given maximum value. */
	private HSVComponent(float maxValue) {
		this.maxValue = maxValue;
	}
	
	/**
	 * Gets the component's maximum value. Note that all values minimum value is 0.
	 * @return The HSV component's maximum value.
	 */
	public float getMaxValue() {
		return maxValue;
	}
}