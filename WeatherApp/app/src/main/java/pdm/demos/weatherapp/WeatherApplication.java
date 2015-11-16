package pdm.demos.weatherapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import pdm.demos.weatherapp.providers.WeatherInfoProvider;
import pdm.demos.weatherapp.providers.openweathermap.OpenWeatherProvider;

/**
 * Singleton class (note that we have one instance per application process) that plays the role
 * of Service Locator, decoupling the remaining code base of the concrete service provider types.
 */
public class WeatherApplication extends Application {

    public static final int REFRESHER_SERVICE_CODE = 1;
    /** The instance that provides the service of providing weather information. */
    private volatile WeatherInfoProvider weatherInfoProvider;
    /** The current language. */
    private String language;
    /** The unit system associated to the current locale. */
    private WeatherInfoProvider.UnitSystem units;

    /**
     * Initializes the locale dependent fields (i.e. language and unit system)
     */
    private void initLocaleConfiguration(Configuration config) {
        language = config.locale.getISO3Language();
        units = config.locale.getISO3Country().toUpperCase().equals("USA") ?
                WeatherInfoProvider.UnitSystem.IMPERIAL : WeatherInfoProvider.UnitSystem.METRIC;
    }

    /**
     * @return The existing weather provider instance.
     */
    public WeatherInfoProvider getWeatherInfoProvider() {
        return weatherInfoProvider;
    }

    /**
     * @return The current language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return The unit system associated to the current locale.
     */
    public WeatherInfoProvider.UnitSystem getUnits() {
        return units;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate() {
        super.onCreate();

        initLocaleConfiguration(getResources().getConfiguration());

        // Instantiate the concrete weather provider implementation
        weatherInfoProvider = new OpenWeatherProvider();

        final AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        final PendingIntent pendingIntent = PendingIntent.getService(
                this, REFRESHER_SERVICE_CODE,
                WeatherRefresherService.makeIntent(this, "Washington DC, USA"),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                0, 60*1000, pendingIntent);
    }

    /** {@inheritDoc} */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLocaleConfiguration(newConfig);
    }
}
