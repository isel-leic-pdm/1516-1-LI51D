package pdm.demos.weatherapp.providers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is used for representing weather information within the application.
 *
 * <p>Instances can also be used to transport weather information between components of the
 * application (e.g. Activities, Services). Because those interactions may cross process
 * boundaries, the class supports the {@link android.os.Parcelable} contract and conventions.</p>
 *
 * <p>Instances are designed to be immutable, and are therefore thread-safe.</p>
 */
public final class WeatherInfo implements Parcelable {

    /** The city name */
    private final String name;
    /** The weather description. */
    private final String description;
    /** The weather icon URL. */
    private final String iconURL;
    /** The wind's speed. */
    private final double speed;
    /** The wind's direction */
    private final double windDirection;
    /** The current temperature. */
    private final double currentTemperature;
    /** The maximum temperature. */
    private final double maxTemperature;
    /** The minimum temperature. */
    private final double minTemperature;
    /** The current humidity. */
    private final int humidity;

    /**
     * Factory of {@link WeatherInfo} instances, according to the {@link Parcelable} convention.
     */
    public static final Creator<WeatherInfo> CREATOR = new Creator<WeatherInfo>() {
        @Override
        public WeatherInfo createFromParcel(Parcel in) {
            return new WeatherInfo(in);
        }

        @Override
        public WeatherInfo[] newArray(int size) {
            return new WeatherInfo[size];
        }
    };

    /**
     * Creates an instance from the given {@link Parcel}.
     * @param in The {@link Parcel} containing the instance's data.
     */
    private WeatherInfo(Parcel in) {
        name = in.readString();
        description = in.readString();
        iconURL = in.readString();
        speed = in.readDouble();
        windDirection = in.readDouble();
        currentTemperature = in.readDouble();
        maxTemperature = in.readDouble();
        minTemperature = in.readDouble();
        humidity = in.readInt();
    }

    /**
     * Initiates an instance with the given arguments.
     * @param name The city's name.
     * @param description The weather conditions description.
     * @param iconURL The weather condition's icon URL.
     * @param deg The direction of the wind (in degrees).
     * @param temp The current temperature (in Kelvin).
     * @param tempMin The minimum temperature (in Kelvin).
     * @param tempMax The maximum temperature (in Kelvin).
     * @param humidity The current air humidity (percentage).
     */
    public WeatherInfo(String name,
                       String description,
                       String iconURL,
                       double speed,
                       double deg,
                       double temp,
                       double tempMin,
                       double tempMax,
                       int humidity) {
        this.name = name;
        this.description = description;
        this.iconURL = iconURL;
        this.speed = speed;
        windDirection = deg;
        currentTemperature = temp;
        minTemperature = tempMin;
        maxTemperature = tempMax;
        this.humidity = humidity;
    }

    /**
     * Provides a printable representation of this object.
     */
    @Override
    public String toString() {
        return new StringBuilder("WeatherInfo [name=").append(name)
            .append(", speed=").append(speed)
            .append(", deg=").append(windDirection)
            .append(", weather=").append(description)
            .append(", temp=").append(currentTemperature)
            .append(", tempMin=").append(minTemperature)
            .append(", tempMax=").append(maxTemperature)
            .append(", humidity=").append(humidity).append(']')
            .toString();
    }

    /** @return The city name. */
    public String getCityName() {
        return name;
    }

    /** @return The weather condition's description. */
    public String getDescription() {
        return description;
    }

    /** @return The weather condition's icon URL. */
    public String getIconURL() {
        return iconURL;
    }

    /** @return The current temperature. */
    public double getTemperature() {
        return currentTemperature;
    }

    /** @return The maximum temperature. */
    public double getMaxTemperature() {
        return maxTemperature;
    }

    /** @return The minimum temperature. */
    public double getMinTemperature() {
        return minTemperature;
    }

    /** @return The current humidity (in percentage). */
    public int getHumidity() {
        return humidity;
    }

    /** @return The wind speed. */
    public double getWindSpeed() {
        return speed;
    }

    /** @return The wind direction (in degrees). */
    public double getWindDirection() {
        return windDirection;
    }

    /** {@inheritDoc} */
    @Override
    public int describeContents() {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(iconURL);
        dest.writeDouble(speed);
        dest.writeDouble(windDirection);
        dest.writeDouble(currentTemperature);
        dest.writeDouble(maxTemperature);
        dest.writeDouble(minTemperature);
        dest.writeInt(humidity);
    }
}
