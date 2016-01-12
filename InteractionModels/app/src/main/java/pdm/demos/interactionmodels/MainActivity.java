package pdm.demos.interactionmodels;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Bundle;
import android.os.RemoteException;
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

        // Food for thought: This implementation is flawed. Can you spot the flaw? How would you
        // address it? Tip: What happens when the screen orientation changes?

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

        // Food for thought: This implementation is also flawed. Can you spot the flaw? How would you
        // address it? Tips: What happens when the screen orientation changes? Should the solution
        // be similar to the one appropriate for the former case (Publish/Subscribe)?

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

        // Lets call the locally hosted bound service
        doLocalCall();
        // Lets call the remotely hosted bound service
        doRemoteCall();
    }

    /**
     * Method used to demonstrate the use of a bound service that is hosted in another process.
     */
    private void doRemoteCall() {

        // Create the intent used to identify the bound service
        final Intent remoteBoundIntent = new Intent(this, MyRemoteBoundService.class);

        // Lets get a reference to it (asynchronously, hence the ServiceConnection object)
        bindService(remoteBoundIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                try {
                    // Let's call it!
                    (MyBoundServiceContract.Stub.asInterface(service)).doSomething(10);
                    // We're done. Free the reference to the service
                    unbindService(this);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // Called upon unbind, just in case we need some cleanup
            }
        }, Context.BIND_AUTO_CREATE);
    }

    /**
     * Method used to demonstrate the use of a bound service that is hosted in the same process.
     */
    private void doLocalCall() {

        // Create the intent used to identify the bound service
        final Intent boundIntent = new Intent(this, MyLocalBoundService.class);

        // Lets get a reference to it (asynchronously, hence the ServiceConnection object)
        bindService(boundIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // Let's call it!
                ((MyLocalBoundServiceContract) service).doSomething(10);
                // We're done. Free the reference to the service
                unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // Called upon unbind, just in case we need some cleanup
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
