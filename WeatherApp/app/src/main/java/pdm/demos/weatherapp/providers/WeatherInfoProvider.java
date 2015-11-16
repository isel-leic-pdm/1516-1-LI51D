package pdm.demos.weatherapp.providers;

import android.support.annotation.NonNull;

import java.util.Locale;

import pdm.demos.weatherapp.providers.openweathermap.WeatherInfoDTO;

/**
 * Contract to be supported by all providers of weather information, either real or mocked. The
 * remainder of the code base is therefore decoupled from how this information is obtained.
 *
 * <p>The asynchronous operation signature is not appropriate because it hinders support for
 * cancellation, a fundamental property of production ready code. Perhaps we will deal with this
 * later. =)</p>
 */
public interface WeatherInfoProvider {

    /**
     * Enumeration containing the supported unit systems.
     */
    enum UnitSystem {
        METRIC("m/s", "ºC"), IMPERIAL("knots", "ºF");

        public final String windSpeedUnits;
        public final String temperatureUnits;

        UnitSystem(@NonNull String windSpeed, @NonNull String temperature) {
            windSpeedUnits = windSpeed;
            temperatureUnits = temperature;
        }
    }

    /**
     * Class whose instances are used to host the asynchronous operation result. Instances are
     * immutable and therefore they can be safely shared by multiple threads.
     */
    final class CallResult {
        private final WeatherInfo result;
        private final Exception error;

        /**
         * Prevent instantiation from outside code.
         * @param result The operation's result, if it executed successfully.
         * @param error The operation's error, if one occurred.
         */
        private CallResult(WeatherInfo result, Exception error) {
            this.result = result;
            this.error = error;
        }

        /**
         * Initiates an instance with the given result, thereby signalling successful completion.
         * @param result The operation's result.
         */
        public CallResult(@NonNull WeatherInfo result) {
            this(result, null);
        }

        /**
         * Initiates an instance with the given exception, thereby signalling a flawed completion.
         * @param error The operation's error.
         */
        public CallResult(@NonNull Exception error) {
            this(null, error);
        }

        /**
         * Gets the operation result.
         * @return The weather information.
         * @throws Exception The error that occurred while trying to get the weather information,
         * if one actually existed.
         */
        @NonNull
        public WeatherInfo getResult() throws Exception {
            if(error != null)
                throw error;
            return result;
        }
    }

    /**
     * The contract to be supported to receive completion notifications of asynchronous operations.
     */
    interface Callback {
        /**
         * Called when the corresponding asynchronous operation is completed.
         * @param result The asynchronous call result. The actual result must be obtained by calling
         *               the {@link WeatherInfoProvider.CallResult} instance's.
         *               {@link CallResult#getResult()} method, which produces the result or, if an
         *               error occurs, throws the exception bearing the error information.
         */
        void onResult(@NonNull CallResult result);
    }

    /**
     * Asynchronous operation to get the current weather info for the given city. Completion is
     * signaled by calling the received callback instance.All arguments
     * must be non {@value null} values.
     *
     * @param cityName The city name.
     * @param language The language to be used on the result, encoded in ISO3.
     * @param units The unit system to be used on the result.
     * @param completionCallback The callback to be executed once the operation is completed,
     *                           either successfully or in failure.
     */
    void getWeatherInfoAsync(@NonNull String cityName, @NonNull String language, @NonNull UnitSystem units,
                             @NonNull Callback completionCallback);

    /**
     * Synchronous version of the operation to get the current weather info for the given city.
     * @param cityName The city name.
     * @param language The language to be used on the result, encoded in ISO3.
     * @param units The unit system to be used on the result.
     * @return The resulting weather information.
     * @throws Exception If an error occurred while fetching the weather information.
     */
    WeatherInfo getWeatherInfo(@NonNull String cityName, @NonNull String language, @NonNull UnitSystem units) throws Exception;
}
