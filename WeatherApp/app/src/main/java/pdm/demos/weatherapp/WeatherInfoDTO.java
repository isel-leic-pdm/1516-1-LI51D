package pdm.demos.weatherapp;

/**
 * Class that defines the DTO (Data Transfer Object) that contains weather information data
 * obtained from the Weather Service API.
 *
 * <p>The class does not contain all the fields produced by the API. It merely contains those
 * that are actually used. Deserialization is performed according to GSON's conventions and
 * field names are chosen accordingly.</p>
 *
 * Design notes: Suppress warnings; returning default values instead of null pointer exceptions
 * Units are those specified by the weather info request
 */
@SuppressWarnings("unused")
public final class WeatherInfoDTO {

    /** The default value, returned whenever a given weather parameter is not available. */
    private static final int DEFAULT = 0;

    /**
     * Class whose instances contain the weather parameters.
     */
    private static class WeatherParameters {
        /** The current temperature */
        private double temp;
        /** The minimum temperature */
        private double temp_min;
        /** The maximum temperature */
        private double temp_max;
        /** The current atmospheric pressure. */
        private double pressure;
        /** The current air humidity. */
        private int humidity;
    }

    /**
     * Class whose instances contain wind information.
     */
    private static class Wind {
        /** The current wind speed */
        private double speed;
        /** The wind direction (in degrees) */
        private double deg;
    }

    /** The main weather parameters. */
    private WeatherParameters main;

    /** The wind information. */
    private Wind wind;

    /** The HTTP response code. */
    private int cod;

    /** The city name. */
    private String name;

    /** @return The current temperature. */
    public double getTemperature() {
        return main == null ? DEFAULT : main.temp;
    }

    /** @return The current temperature. */
    public double getLowestTemperature() {
        return main == null ? DEFAULT : main.temp_min;
    }

    /** @return The current temperature. */
    public double getHighestTemperature() {
        return main == null ? DEFAULT : main.temp_max;
    }

    /** @return The current air humidity. */
    public int getHumidity() {
        return main == null ? DEFAULT : main.humidity;
    }

    /** @return The atmospheric pressure. */
    public double getPressure() {
        return main == null ? DEFAULT : main.pressure;
    }

    /** @return The city name. */
    public String getCityName() {
        return name == null ? "" : name;
    }

    /** @return The HTTP response code. */
    public int getResponseCode() {
        return cod;
    }

    /** @return The wind speed. */
    public double getWindSpeed() {
        return wind == null ? DEFAULT : wind.speed;
    }

    /** @return The wind speed. */
    public double getWindDirection() {
        return wind == null ? DEFAULT : wind.deg;
    }
}
