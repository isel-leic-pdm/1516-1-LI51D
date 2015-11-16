package pdm.demos.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import pdm.demos.weatherapp.providers.CardinalPoint;
import pdm.demos.weatherapp.providers.WeatherInfo;
import pdm.demos.weatherapp.utils.LoggingActivity;

/**
 * Activity used to display the current weather information.
 *
 * <p>The weather information to be displayed is received as an extra of the intent used to
 * explicitly navigate to this activity.</p>
 *
 * TODO: Redo request when the current locale changes.
 */
public class WeatherActivity extends LoggingActivity {

    /** Constant used to identify the extra bearing the weather information. */
    private static final String EXTRA_WEATHER_INFO = "WeatherActivity.Extra.WeatherInfo";
    /** Constant used to identify the view state containing the weather information. */
    private static final String VIEW_STATE_WEATHER_INFO = "WeatherActivity.ViewState.WeatherInfo";

    /** The current configuration. */
    private Configuration currentConfiguration;
    /** The weather information currently displayed. */
    private WeatherInfo currentWeather;

    /**
     * Helper method that displays text on the given {@link TextView} instance.
     * @param resId The resource identifier of the {@link TextView} instance.
     * @param text The text to be displayed.
     */
    private void displayText(int resId, String text) {
        ((TextView) findViewById(resId)).setText(text);
    }

    /**
     * Helper method that displays temperature values on the given {@link TextView} instance.
     * @param resId The resource identifier of the {@link TextView} instance.
     * @param temperature The temperature to be displayed.
     */
    private void displayTemperature(int resId, double temperature) {
        displayText(resId, String.format(currentConfiguration.locale, "%1$.0f %2$s",
                        temperature, getResources().getString(R.string.text_temp_units))
        );
    }

    /**
     * Helper method that displays the wind information (i.e. direction and speed)
     * @param weather The weather information.
     */
    private void displayWindInformation(WeatherInfo weather) {
        displayText(R.id.wind, weather.getWindSpeed() + " " +
                ((WeatherApplication) getApplication()).getUnits().windSpeedUnits + " - " +
                CardinalPoint.windDegreesToDirection(weather.getWindDirection())
                        .toString(currentConfiguration.locale));
    }

    /**
     * Displays the current date.
     */
    private void displayCurrentDate() {
        final SimpleDateFormat format = new SimpleDateFormat("MMMM, d");
        displayText(R.id.date, format.format(new Date()));
    }

    /**
     * Helper method that displays the given weather information.
     * @param weather The weather information to be displayed.
     */
    private void displayWeatherInfo(WeatherInfo weather) {
        displayText(R.id.cityName, weather.getCityName());
        displayText(R.id.description, weather.getDescription());
        displayTemperature(R.id.temp, weather.getTemperature());
        displayTemperature(R.id.minTemp, weather.getMinTemperature());
        displayTemperature(R.id.maxTemp, weather.getMaxTemperature());
        displayWindInformation(weather);
        displayText(R.id.humidity, weather.getHumidity() +
                getResources().getString(R.string.text_humidity_units));
        displayCurrentDate();
    }

    /**
     * Method that creates an {@link Intent} to be used to explicitly navigate to this activity.
     *
     * @param sender The context of the intent sender.
     * @param weatherInfo The object containing the weather information to be displayed.
     * @return The newly created explicit intent.
     */
    @NonNull
    public static Intent makeIntent(@NonNull Context sender, @NonNull WeatherInfo weatherInfo) {
        return new Intent(sender, WeatherActivity.class).putExtra(EXTRA_WEATHER_INFO, weatherInfo);
    }

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        currentConfiguration = getResources().getConfiguration();

        currentWeather = (WeatherInfo) (savedInstanceState != null ?
                    savedInstanceState.getParcelable(VIEW_STATE_WEATHER_INFO) :
                    getIntent().getParcelableExtra(EXTRA_WEATHER_INFO));

        displayWeatherInfo(currentWeather);

        // If first time, create retained fragment
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(
                            R.id.weatherImage,
                            DownloadImageFragment.newInstance(currentWeather.getIconURL())
                    )
                    .commit();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(VIEW_STATE_WEATHER_INFO, currentWeather);
    }
}
