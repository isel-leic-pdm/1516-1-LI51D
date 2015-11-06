package pdm.demos.weatherapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pdm.demos.weatherapp.providers.WeatherInfo;


/**
 * TODO:
 */
public class WeatherRefresherService extends IntentService {

    private static final String EXTRA_CITY_NAME = "WeatherRefresherService.Extra.CityName";

    /**
     *
     * @param cityName
     * @return
     */
    public static Intent makeIntent(Context ctx, String cityName) {
        return new Intent(ctx, WeatherRefresherService.class)
                .putExtra(EXTRA_CITY_NAME, cityName);
    }

    /**
     *
     */
    public WeatherRefresherService() {
        super("WeatherRefresherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String cityName = intent.getStringExtra(EXTRA_CITY_NAME);
        final WeatherApplication application = (WeatherApplication) getApplication();
        try {
            final WeatherInfo info = application.getWeatherInfoProvider().
                    getWeatherInfo(cityName, application.getLanguage(), application.getUnits());

            Log.v("INTENT_SERVICE", info.toString());

        } catch (Exception e) {
            Log.v("INTENT_SERVICE", "Ouch!");
        }
    }
}
