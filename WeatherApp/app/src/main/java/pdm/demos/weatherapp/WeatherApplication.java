package pdm.demos.weatherapp;

import android.app.Application;

import pdm.demos.weatherapp.providers.WeatherInfoProvider;
import pdm.demos.weatherapp.providers.openweathermap.OpenWeatherProvider;

/**
 * Singleton class (note that we have one instance per application process) that plays the role
 * of Service Locator, decoupling the remaining code base of the concrete service provider types.
 */
public class WeatherApplication extends Application {

    /**
     * The instance that provides the service of providing weather information.
     */
    private volatile WeatherInfoProvider weatherInfoProvider;

    /** {@inheritDoc} */
    @Override
    public void onCreate() {
        super.onCreate();

        // Instantiate the concrete weather provider implementation
        weatherInfoProvider = new OpenWeatherProvider();
    }

    /**
     * @return The existing weather provider instance.
     */
    public WeatherInfoProvider getWeatherInfoProvider() {
        return weatherInfoProvider;
    }
}
