package pdm.demos.interactionmodels;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Activity used for demonstrating interaction models. The Activity instance has the initiative of
 * communicating with the implemented services, sending them the name of the city. The result is
 * then obtained by using Request-Reply and Publish-Subscribe interaction models.
 */
public class MainActivity extends Activity {

    private static final String TAG = "DEMO_APP";

    /**
     * Method used to demonstrate how to trigger fetching of weather information. The fetched
     * information is not actually obtained by this Activity. Instead, the information is dropped
     * somewhere (in this case in the log) by the Service that fetched it.
     * @param cityName The name of the city for which the weather info is to be obtained
     */
    private void triggerFetchThroughIntentService(String cityName) {
        Log.v(TAG, "MainActivity.triggerFetchThroughIntentService() - One Way communication example");
        startService(MyIntentService.ContractHelper.makeOneWayIntent(this, cityName));
    }

    /**
     * Method used to demonstrate how to trigger fetching of weather information while subscribing
     * for notifications in order to receive the fetched information.
     *
     * Implementation note: For demonstration purposes, subscription and unsubscription steps are
     * performed every time a request is sent. In real applications, the subscription life span can
     * be much longer (e.g. subscription made in Activity#onStart() and removed in Activity#onStop())
     * @param cityName The name of the city for which the weather info is to be obtained.
     */
    private void triggerFetchWithSubscriptionThroughIntentService(String cityName) {
        Log.v(TAG, "MainActivity.triggerFetchWithSubscriptionThroughIntentService() - " +
                "Pub / Sub communication example");

        // Subscribe
        MyIntentService.ContractHelper.subscribe(this, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Info has arrived.
                final WeatherInfo info = MyIntentService.ContractHelper
                        .getWeatherInfoFromIntent(intent);
                Log.v(TAG, "In Activity through broadcast. Weather at " + info.getCityName()
                        + " is " + info.getDescription());

                showResult(info);

                // Unsubscribe
                MyIntentService.ContractHelper.unsubscribe(context, this);
            }
        });

        // Trigger fetch
        startService(MyIntentService.ContractHelper.makeOneWayIntent(this, cityName));
    }

    /**
     * Method used to demonstrate how to trigger fetching of weather information. The fetched
     * information is not actually obtained by this Activity. Instead, the information is dropped
     * somewhere (in this case in the log) by the Service that fetched it.
     * @param cityName The name of the city for which the weather info is to be obtained
     */
    private void triggerFetchThroughStartedService(String cityName) {
        Log.v(TAG, "MainActivity.triggerFetchThroughStartedService() - One Way communication example");
        startService(MyStartedService.ContractHelper.makeOneWayIntent(this, cityName));
    }

    /**
     * Method used to demonstrate how to trigger fetching of weather information while subscribing
     * for notifications in order to receive the fetched information.
     *
     * Implementation note: For demonstration purposes, subscription and unsubscription steps are
     * performed every time a request is sent. In real applications, the subscription life span can
     * be much longer (e.g. subscription made in Activity#onStart() and removed in Activity#onStop())
     * @param cityName The name of the city for which the weather info is to be obtained.
     */
    private void triggerFetchWithReplyToThroughStartedService(final String cityName) {
        Log.v(TAG, "MainActivity.triggerFetchWithReplyToThroughStartedService() - " + "" +
                "Request / Reply communication example");

        startService(MyStartedService.ContractHelper.makeRequestReplyIntent(this, cityName,
                        new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                final WeatherInfo info = MyStartedService.ContractHelper
                                        .getWeatherInfoFromMessage(msg);

                                if (info != null) {
                                    Log.v(TAG, "In Activity through Messenger. Weather in "
                                            + info.getCityName() + " is "
                                            + info.getDescription());
                                    showResult(info);
                                    return true;
                                }
                                return false;
                            }
                        })
        );
    }

    /**
     * Helper method used to present a Toast message with the given weather information.
     * @param info The weather information to be shown.
     */
    private void showResult(WeatherInfo info) {
        Toast.makeText(this, info.getCityName() + " : " + info.getDescription(), Toast.LENGTH_LONG)
                .show();
    }

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonIS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerFetchThroughIntentService("London, UK");
            }
        });

        findViewById(R.id.buttonSubIS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerFetchWithSubscriptionThroughIntentService("London, UK");
            }
        });

        findViewById(R.id.buttonSS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerFetchThroughStartedService("Lisbon, PT");
            }
        });

        findViewById(R.id.buttonRRSS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerFetchWithReplyToThroughStartedService("Lisbon, PT");
            }
        });
    }
}
