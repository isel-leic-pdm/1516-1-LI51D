package pdm.demos.weatherapp;

import android.app.Application;
import android.content.res.Configuration;

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

    private String language;
    private WeatherInfoProvider.UnitSystem units;

    /**
     * Initializes the locale dependent fields (i.e. language and unit system)
     */
    private void initLocaleConfiguration(Configuration config) {
        language = config.locale.getISO3Language();
        units = config.locale.getISO3Country().toUpperCase().equals("USA") ?
                WeatherInfoProvider.UnitSystem.IMPERIAL : WeatherInfoProvider.UnitSystem.METRIC;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate() {
        super.onCreate();

        initLocaleConfiguration(getResources().getConfiguration());

        // Instantiate the concrete weather provider implementation
        weatherInfoProvider = new OpenWeatherProvider();
    }

    /**
     * @return The existing weather provider instance.
     */
    public WeatherInfoProvider getWeatherInfoProvider() {
        return weatherInfoProvider;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLocaleConfiguration(newConfig);
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return Gets ...
     */
    public WeatherInfoProvider.UnitSystem getUnits() {
        return units;
    }
}
