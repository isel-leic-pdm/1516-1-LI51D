package pdm.demos.weatherapp.providers.openweathermap;

/**
 * Class that defines the DTO (Data Transfer Object) that contains weather information data
 * obtained from the OpenWeatherMap Service API.
 *
 * <p>The class does not contain all the fields produced by the API. It merely contains those
 * that are actually used. JSON deserialization is performed according to GSON's conventions and
 * field names are chosen accordingly. Units are those specified by the weather info request. </p>
 *
 * <p>Instances of this class are created through reflection and the IDE (i.e. lint tool)
 * signals its fields as not being used by the application. This verification is performed
 * statically (at build time) and therefore does not account for its use by GSON. For this reason
 * we use the {@link SuppressWarnings} annotations, thereby preventing the false positive. </p>
 *
 * <p>Another design decision is that getter methods return default values, instead of throwing
 * {@link NullPointerException}. This approach ensures that small variations of the API do not
 * provoke application failure. A fail fast solution (throwing exceptions or letting {@literal null}
 * references escape) is inappropriate in this case because we are dealing with an external
 * dependency.</p>
 */
@SuppressWarnings("unused")
public final class WeatherInfoDTO {

    /** The default values, returned whenever a given weather parameter is not available. */
    private static final int DEFAULT = 0;
    private static final String EMPTY_STRING = "";

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

    /**
     * Class whose instances represent weather info entrie, containing the weather descrition
     * and the icon name.
     */
    private static class WeatherEntry {
        /** The current weather description */
        private String description;
        /** The current weather icon name */
        private String icon;
    }

    /** The main weather parameters. */
    private WeatherParameters main;

    /** The wind information. */
    private Wind wind;

    /** The current weather entries, bearing the weather description. */
    private WeatherEntry[] weather;

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
        return name == null ? EMPTY_STRING : name;
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

    /** @return The weather conditions' description. */
    public String getDescription() {
        final String description = weather == null ? EMPTY_STRING : weather[0].description;
        return description == null ? EMPTY_STRING : description;
    }

    /** @return The weather conditions' icon name. */
    public String getWeatherIconName() {
        final String iconName = weather == null ? EMPTY_STRING : weather[0].icon;
        return iconName == null ? EMPTY_STRING : iconName;
    }

    /** @return The URL that refers to the weather icon. */
    public String getWeatherIconURL() {
        final String ICON_PATH = "/img/w/", ICON_EXTENSION = ".png";
        return new StringBuilder(WebAPI.BASE_URL)
                .append(ICON_PATH).append(getWeatherIconName()).append(ICON_EXTENSION)
                .toString();
    }
}
