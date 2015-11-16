package pdm.demos.weatherapp;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.WorkerThread;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pdm.demos.weatherapp.providers.WeatherInfo;


/**
 * TODO:
 */
public class WeatherRefresherService extends IntentService {

    private static final String EXTRA_CITY_NAME = "WeatherRefresherService.Extra.CityName";
    public static final int ID = 123;

    private void showNotification(WeatherInfo info) {

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            final Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(info.getCityName())
                    .setContentText(info.getDescription())
                    .build();

            notificationManager.notify(ID, notification);
        }
    }

    /**
     * Creates the intent used to activate the service.
     * @param cityName
     * @return
     */
    public static Intent makeIntent(Context ctx, String cityName) {
        return new Intent(ctx, WeatherRefresherService.class)
                .putExtra(EXTRA_CITY_NAME, cityName);
    }

    /**
     * Initiates an instance.
     */
    public WeatherRefresherService() {
        super("WeatherRefresherService");
    }

    /** {@inheritDoc} */
    @Override
    protected void onHandleIntent(Intent intent) {
        final String cityName = intent.getStringExtra(EXTRA_CITY_NAME);
        final WeatherApplication application = (WeatherApplication) getApplication();
        try {
            final WeatherInfo info = application.getWeatherInfoProvider().
                    getWeatherInfo(cityName, application.getLanguage(),
                            application.getUnits());

            // Ugly as hell!
            showNotification(info);

        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }
}
