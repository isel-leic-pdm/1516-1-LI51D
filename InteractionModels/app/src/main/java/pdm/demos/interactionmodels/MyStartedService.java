package pdm.demos.interactionmodels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import pdm.demos.interactionmodels.openweathermap.DataMapper;
import pdm.demos.interactionmodels.openweathermap.WeatherInfoDTO;
import pdm.demos.interactionmodels.openweathermap.WebAPI;
import pdm.demos.interactionmodels.utils.LifecycleLoggingService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Implementation of a Started Service.
 *
 * <p>The implementation fetches the Weather information using the asynchronous version of
 * the weather API, whose implementation is provided by Retrofit.</p>
 *
 * <ul>The service supports two messaging interaction models:
 * <li> One way: used to receive the message (i.e. Intent) bearing the city's name; </li>
 * <li> Request / Reply: Used to send back (to the requesting party) the obtained weather
 * information. </li>
 * </ul>
 */
public class MyStartedService extends LifecycleLoggingService {

    public static final String TAG = "DEMO_APP";

    /**
     * Class that contains the set of utility methods used to materialize the messaging contract
     * supported by {@link MyStartedService}.
     */
    public static class ContractHelper {
        /**
         * Constant used to specify the name of the inbound intent's extra that contains
         * the name of the city for which the weather information is to be fetched.
         */
        private static final String EXTRA_CITY_NAME = "MyStartedService.EXTRA_CITY_NAME";

        /**
         * Constant used to specify the name of the inbound intent's extra that contains
         * the {@link Messenger} instance that is to be used to send back the reply message.
         */
        private static final String EXTRA_REPLY_TO = "MyStartedService.EXTRA_REPLY_TO";

        /**
         * Constant used to specify the name of the extra contained in the reply message. The extra
         * contains the obtained weather information.
         */
        private static final String EXTRA_RESULT = "MyStartedService.EXTRA_RESULT";

        /**
         * Method that builds an intent that can be used to trigger this service.
         * @param origin The context to be used (e.g. usually the sender's context).
         * @param cityName The name of the city to used in the weather query.
         * @return The {@link Intent} instance that can be sent to the service in order to
         * trigger fetching of weather information.
         */
        public static Intent makeOneWayIntent(Context origin, String cityName) {
            return new Intent(origin, MyStartedService.class)
                    .putExtra(EXTRA_CITY_NAME, cityName);
        }

        /**
         * Method that builds an intent that can be used to trigger this service.
         * @param origin The context to be used (e.g. usually the sender's context).
         * @param cityName The name of the city to used in the weather query.
         * @return The {@link Intent} instance that can be sent to the service in order to
         * trigger fetching of weather information.
         */
        public static Intent makeRequestReplyIntent(Context origin, String cityName,
                                                    Handler.Callback callback) {
            final Messenger replyTo = new Messenger(new Handler(callback));
            return makeOneWayIntent(origin, cityName).putExtra(EXTRA_REPLY_TO, replyTo);
        }

        /**
         * Gets the weather information from the given message instance.
         * @param replyMsg The message from which the weather information is to be extracted.
         * @return The weather information, or {@literal null} if none was present.
         */
        public static WeatherInfo getWeatherInfoFromMessage(Message replyMsg) {
            final Bundle data = replyMsg.getData();
            data.setClassLoader(WeatherInfo.class.getClassLoader());
            return (WeatherInfo) data.getParcelable(EXTRA_RESULT);
        }
    }

    /**
     * Method that sends the given weather information through the received {@link Messenger}
     * instance.
     * @param result The weather information to be sent.
     * @param replyTo The {@link Messenger} instance to be used.
     */
    private void sendReply(WeatherInfo result, Messenger replyTo) {
        Log.v(TAG, "sendReply()");
        final Message msg = Message.obtain();
        msg.getData().putParcelable(ContractHelper.EXTRA_RESULT, result);

        try {
            replyTo.send(msg);
        } catch (RemoteException e) {
            // Should be dealt with in production code, otherwise it would be broken.
            // Because this is a demo, we aim at simplicity.
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /** {@inheritDoc} */
    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);

        // Configure retrofit object
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create web api proxy
        WebAPI service = retrofit.create(WebAPI.class);
        final Call<WeatherInfoDTO> call = service
                .getCurrentWeather(WebAPI.API_KEY,
                        intent.getStringExtra(ContractHelper.EXTRA_CITY_NAME),
                        "en", "metric"
                );

        // Fetch the information asynchronously
        call.enqueue(new Callback<WeatherInfoDTO>() {
            @Override
            public void onResponse(Response<WeatherInfoDTO> response, Retrofit retrofit) {
                Log.v(TAG, "onResult()");
                // Get weather information
                final WeatherInfoDTO result = response.body();
                // Check if a reply-to messenger exists
                final Messenger replyTo = intent.getParcelableExtra(ContractHelper.EXTRA_REPLY_TO);
                if (replyTo != null) {
                    // If so, convert DTO and send the result back
                    sendReply(new DataMapper().convertFrom(result), replyTo);
                }
                Log.v(TAG, result.getCityName() + " : " + result.getDescription());
                stopSelf(startId);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v(TAG, "Callback.onFailure()");
                t.printStackTrace();
                stopSelf(startId);
            }
        });

        // No need to redeliver intent. If fault tolerance would be an issue, we should return
        // Service.START_REDELIVER_INTENT
        return START_NOT_STICKY;
    }
}
