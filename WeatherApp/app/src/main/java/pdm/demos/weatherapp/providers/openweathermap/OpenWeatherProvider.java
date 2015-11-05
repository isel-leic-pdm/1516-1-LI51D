package pdm.demos.weatherapp.providers.openweathermap;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import pdm.demos.weatherapp.providers.WeatherInfo;
import pdm.demos.weatherapp.providers.WeatherInfoProvider;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Class that implements a {@link WeatherInfoProvider} backed by the OpenWeatherMap Web API.
 *
 * <p>See <a href="http://openweathermap.org/">OpenWeatherMap</a> documentation for detailed
 * information.</p>
 */
public class OpenWeatherProvider implements WeatherInfoProvider {

    /** The weather service API proxy */
    private final WebAPI serviceAPI;
    /** The data mapper used to convert DTOs. */
    private final DataMapper mapper;

    /**
     * Initiates an instance.
     */
    public OpenWeatherProvider() {

        // Configure retrofit object
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create web api proxy
        serviceAPI = retrofit.create(WebAPI.class);

        // Create the data mapper
        mapper = new DataMapper();
    }

    /** {@inheritDoc} */
    @Override
    public void getWeatherInfoAsync(@NonNull String cityName, @NonNull String language, @NonNull UnitSystem units,
                                    @NonNull final Callback completionCallback) {

        final Call<WeatherInfoDTO> call = serviceAPI
                .getCurrentWeather(WebAPI.API_KEY, cityName, language, units.toString().toLowerCase());

        call.enqueue(new retrofit.Callback<WeatherInfoDTO>() {
            @Override
            public void onResponse(Response<WeatherInfoDTO> response, Retrofit retrofit) {

                final CallResult result = response.isSuccess() ?
                        new CallResult(mapper.convertFrom(response.body())) :
                        new CallResult(new Exception(response.errorBody().toString()));

                completionCallback.onResult(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completionCallback.onResult(new CallResult(new Exception(t)));
            }
        });
    }

    /** {@inheritDoc} */
    public WeatherInfo getWeatherInfo(@NonNull String cityName, @NonNull String language, @NonNull UnitSystem units)
                throws Exception {

        final Call<WeatherInfoDTO> call = serviceAPI
                .getCurrentWeather(WebAPI.API_KEY, cityName, language, units.toString().toLowerCase());

        final Response<WeatherInfoDTO> response = call.execute();
        if(!response.isSuccess())
            throw new Exception(response.errorBody().toString());

        return mapper.convertFrom(response.body());
    }
}
