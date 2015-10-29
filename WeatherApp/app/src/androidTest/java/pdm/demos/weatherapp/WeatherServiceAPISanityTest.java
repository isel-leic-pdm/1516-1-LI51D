package pdm.demos.weatherapp;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Class that contains the API sanity tests, namely, checks if is it up and running, if the the
 * API contract changed, amongst others.
 *
 * These are not unit tests. Instead, this test suite is designed to assert if the external
 * border is operating as expected.
 */
public class WeatherServiceAPISanityTest extends TestCase {

    public void testGetCurrentWeather_withCorrectArguments_returnsResultOK() throws IOException{

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherServiceAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WeatherServiceAPI service = retrofit.create(WeatherServiceAPI.class);

        final Call<WeatherInfoDTO> callSync = service
                .getCurrentWeather(WeatherServiceAPI.API_KEY, "Lisbon,pt", "en", "imperial");

        final WeatherInfoDTO info = callSync.execute().body();

        Assert.assertNotNull(info);
        Assert.assertEquals(200, info.getResponseCode());
    }
}
