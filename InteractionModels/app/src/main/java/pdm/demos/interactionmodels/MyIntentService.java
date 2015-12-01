package pdm.demos.interactionmodels;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.IOException;

import pdm.demos.interactionmodels.openweathermap.DataMapper;
import pdm.demos.interactionmodels.openweathermap.WeatherInfoDTO;
import pdm.demos.interactionmodels.openweathermap.WebAPI;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


/**
 * Implementation of a Started Service using an Intent Service.
 *
 * <p>The implementation fetches the Weather information using the synchronous version of the weather
 * API, whose implementation is provided by Retrofit.</p>
 *
 * <ul>The service supports two messaging interaction models:
 * <li> One way: used to receive the message (i.e. Intent) bearing the city's name; </li>
 * <li> Publish / Subscribe: Used to send back (to any interested parties) the obtained weather
 * information. </li>
 * </ul>
 */
public class MyIntentService extends IntentService {

    public static final String TAG = "DEMO_APP";

    /**
     * Class that contains the set of utility methods used to materialize the messaging contract
     * supported by {@link MyIntentService}.
     */
    public static class ContractHelper {

        /**
         * Constant used to specify the name of the inbound intent's extra that contains
         * the name of the city for which the weather information is to be fetched.
         */
        private static final String EXTRA_CITY_NAME = "MyIntentService.EXTRA_CITY_NAME";

        /**
         * Constant used to specify the Action associated to the intent used to broadcast the
         * obtained weather information to interested parties.
         */
        private static final String ACTION_RESULT_AVAILABLE = "MyIntentService.BROADCAST_RESULT";

        /**
         * Constant used to specify the name of the extra contained in broadcast intents. The extra
         * contains the obtained weather information.
         */
        private static final String EXTRA_RESULT = "MyIntentService.EXTRA_RESULT";

        /**
         * Method that builds an intent that can be used to trigger this service.
         * @param origin The context to be used (e.g. usually the sender's context).
         * @param cityName The name of the city to used in the weather query.
         * @return The {@link Intent} instance that can be sent to the service in order to
         * trigger fetching of weather information.
         */
        public static Intent makeOneWayIntent(Context origin, String cityName) {
            return new Intent(origin, MyIntentService.class)
                    .putExtra(EXTRA_CITY_NAME, cityName);
        }

        /**
         * Subscribes the given receiver as an interested party in receiving broadcast messages
         * bearing the fetched weather information.
         * @param subscriberCtx The subscriber's context.
         * @param receiver The receiver that will handle broadcasts.
         * @return The received receiver, for the callers convenience.
         */
        public static BroadcastReceiver subscribe(Context subscriberCtx, BroadcastReceiver receiver) {
            // Specify what we are interested on
            final IntentFilter subscriptionDescriptor = new IntentFilter(ACTION_RESULT_AVAILABLE);
            subscriptionDescriptor.addCategory(Intent.CATEGORY_DEFAULT);

            // Register subscription
            subscriberCtx.registerReceiver(receiver, subscriptionDescriptor);
            return receiver;
        }

        /**
         * Unsubscribes the given receiver. The receiver will no longer received broadcast intents
         * nearing weather information.
         * @param subscriberCtx The subscriber's context.
         * @param receiver The receiver to be removed from the list of interested parties.
         */
        public static void unsubscribe(Context subscriberCtx, BroadcastReceiver receiver) {
            // Remove subscription
            subscriberCtx.unregisterReceiver(receiver);
        }

        /**
         * Gets the weather information from the given intent instance.
         * @param intent The intent from wich the weather information is to be extracted.
         * @return The weather information, or {@literal null} if none was present.
         */
        public static WeatherInfo getWeatherInfoFromIntent(Intent intent) {
            return (WeatherInfo) intent.getParcelableExtra(EXTRA_RESULT);
        }
    }

    /**
     * Method used to publish the given weather information.
     * @param weatherInfo The weather information to be published.
     */
    private void publishResult(WeatherInfo weatherInfo) {
        sendBroadcast(new Intent(ContractHelper.ACTION_RESULT_AVAILABLE)
                        .addCategory(Intent.CATEGORY_DEFAULT)
                        .putExtra(ContractHelper.EXTRA_RESULT, weatherInfo)
        );
    }

    /** Initiates an instance. */
    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Callback method used to specify the service's background work. The method is executed in
     * an alternative thread (i.e. not in the process main thread), provided by the implementation
     * of {@link IntentService}.
     * @param intent The received intent.
     */
    @Override
    @WorkerThread
    protected void onHandleIntent(Intent intent) {

        Log.v(getClass().getSimpleName(), "MyIntentService.onHandleIntent() started");

        // Configure retrofit object
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create web api proxy
        WebAPI service = retrofit.create(WebAPI.class);
        final Call<WeatherInfoDTO> call = service
                .getCurrentWeather(
                        WebAPI.API_KEY,
                        intent.getStringExtra(ContractHelper.EXTRA_CITY_NAME),
                        "en", "imperial"
                );

        // Fetch weather information. The right way!
        try {
            // Do it synchronously.
            final WeatherInfoDTO result = call.execute().body();
            // Convert from DTO to the application's representation
            final WeatherInfo info = new DataMapper().convertFrom(result);
            Log.v(TAG, "MyIntentService.onHandleIntent() - "  + info.getCityName() +
                    " : " + info.getDescription());
            // Publish result (sending it to any interested parties)
            publishResult(info);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fetch weather information. The wrong way!
        // The service may be destroyed before the completion of the asynchronous operations
        // This leads to the loss of relevance of the hosting process, which may lead to forced
        // termination, thereby precluding the completion of the async work.
        /*
        call.enqueue(new Callback<WeatherInfoDTO>() {
            @Override
            public void onResponse(Response<WeatherInfoDTO> response, Retrofit retrofit) {
                final WeatherInfoDTO result = response.body();
                final WeatherInfo info = new DataMapper().convertFrom(result);
                Log.v(LOG, info.getCityName() + " : " + info.getDescription());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v(LOG, "Callback.onFailure()");
                t.printStackTrace();
            }
        });
        */
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "MyIntentService.onCreate()");
    }

    /** {@inheritDoc} */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "MyIntentService.onDestroy()");
    }
}
