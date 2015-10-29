package pdm.demos.weatherapp;

/**
 * This class is a Plain Old Java Object (POJO) used for data
 * transport within the WeatherOps app.  This POJO implements the
 * Parcelable interface to enable IPC between the WeatherActivity and
 * the WeatherServiceSync and WeatherServiceAsync. It represents the
 * response Json obtained from the Open Weather Map API, e.g., a call
 * to http://api.openweathermap.org/data/2.5/weather?q=Nashville,TN
 * might return the following Json data:
 * 
 * { "coord":{ "lon":-86.78, "lat":36.17 }, "sys":{ "message":0.0138,
 * "country":"United States of America", "sunrise":1431427373,
 * "sunset":1431477841 }, "weather":[ { "id":802, "main":"Clouds",
 * "description":"scattered clouds", "icon":"03d" } ],
 * "base":"stations", "main":{ "temp":289.847, "temp_min":289.847,
 * "temp_max":289.847, "pressure":1010.71, "sea_level":1035.76,
 * "grnd_level":1010.71, "humidity":76 }, "wind":{ "speed":2.42,
 * "deg":310.002 }, "clouds":{ "all":36 }, "dt":1431435983,
 * "id":4644585, "name":"Nashville", "cod":200 }
 *
 * The meaning of these Json fields is documented at 
 * http://openweathermap.org/weather-data#current.
 *
 * Parcelable defines an interface for marshaling/de-marshaling
 * https://en.wikipedia.org/wiki/Marshalling_(computer_science)
 * to/from a format that Android uses to allow data transport between
 * processes on a device.  Discussion of the details of Parcelable is
 * outside the scope of this assignment, but you can read more at
 * https://developer.android.com/reference/android/os/Parcelable.html.
 */
public class WeatherInfo {
    /*
     * These data members are the local variables that will store the
     * WeatherData's state
     */
    private final String mName;
    private final String mDescription;
    private final String mIconName;
    private final double mSpeed;
    private final double mDeg;
    private final double mTemp;
    private final double mTempMax;
    private final double mTempMin;
    private final long mHumidity;

    /**
     * Initiates an instance with the given arguments.
     * @param name The citr's name.
     * @param description The weather conditions description.
     * @param iconName The weather conditions icon name.
     * @param deg The direction of the wind (in degrees).
     * @param temp The current temperature (in Kelvin).
     * @param tempMin The minimum temperature (in Kelvin).
     * @param tempMax The maximum temperature (in Kelvin).
     * @param humidity The current air humidity (percentage).
     */
    public WeatherInfo(String name,
                       String description,
                       String iconName,
                       double speed,
                       double deg,
                       double temp,
                       double tempMin,
                       double tempMax,
                       long humidity) {
        mName = name;
        mDescription = description;
        mIconName = iconName;
        mSpeed = speed;
        mDeg = deg;
        mTemp = temp;
        mTempMin = tempMin;
        mTempMax = tempMax;
        mHumidity = humidity;
    }

    /**
     * Provides a printable representation of this object.
     */
    @Override
    public String toString() {
        return "WeatherData [name=" + mName
            + ", speed=" + mSpeed
            + ", deg=" + mDeg
            + ", weather=" + mDescription
            + ", temp=" + mTemp
            + ", tempMin=" + mTempMin
            + ", tempMax=" + mTempMax
            + ", humidity=" + mHumidity + "]";
    }

    /**
     * @return The city name.
     */
    public String getCityName() {
        return mName;
    }

    /**
     * @return The weather condition's description.
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @return The weather condition's icon name.
     */
    public String getIconName() {
        return mIconName;
    }

    /**
     * @return The current temperature (in Kelvin).
     */
    public double getTemperature() {
        return mTemp;
    }

    /**
     * @return The maximum temperature  (in Kelvin).
     */
    public double getMaxTemperature() {
        return mTempMax;
    }

    /**
     * @return The minimum temperature (in Kelvin).
     */
    public double getMinTemperature() {
        return mTempMin;
    }

    /**
     * @return The current humidity (in percentage).
     */
    public long getHumidity() {
        return mHumidity;
    }

    /**
     * @return The wind speed.
     */
    public double getWindSpeed() {
        return mSpeed;
    }

    /**
     * @return The wind direction (in degrees).
     */
    public double getWindDirection() {
        return mDeg;
    }
}
