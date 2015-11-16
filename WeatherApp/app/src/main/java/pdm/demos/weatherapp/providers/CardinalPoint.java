package pdm.demos.weatherapp.providers;

import java.util.Locale;

/**
 * Enumeration that corresponds to the existing cardinal points.
 * Currently only en and pt languages are supported.
 */
public enum CardinalPoint {

    N, NNE, NE, ENE,
    E, ESE, SE, SSE,
    S, SSW, SW, WSW,
    W, WNW, NW, NWN;

    /**
     * Gets the cardinal point string representation according to the given locale.
     * @param locale The localization information.
     * @return The corresponding string representation.
     */
    public String toString(Locale locale) {
        if(locale.getISO3Language().equals("pt"))
            return toString().replace('W', 'O');

        // Defaults to English
        return toString();
    }

    /**
     * Utility method that converts between wind direction expressed in degrees to its corresponding
     * direction expressed in cardinal points.
     * @param wind The wind direction, expressed in degrees.
     * @return The corresponding cardinal point.
     */
    public static CardinalPoint windDegreesToDirection(double wind) {
        final int CIRCLE_ANGLE_IN_DEGREES = 360;
        final double slotSize = CIRCLE_ANGLE_IN_DEGREES / CardinalPoint.values().length;
        return CardinalPoint.values()[(int)(((wind+slotSize/2)% CIRCLE_ANGLE_IN_DEGREES)/slotSize)];
    }
}
