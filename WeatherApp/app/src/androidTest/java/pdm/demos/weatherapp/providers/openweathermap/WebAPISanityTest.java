package pdm.demos.weatherapp.providers.openweathermap;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static junit.framework.Assert.*;

/**
 * Class that contains the API sanity tests, namely, it checks if is it up and running and if the
 * API contract has changed.
 *
 * <p>These are not unit tests. Instead, this test suite is designed to assert if the external
 * border is operating as expected.</p>
 */
@RunWith(AndroidJUnit4.class)
public class WebAPISanityTest extends TestCase {

    /** The weather service API proxy */
    private WebAPI serviceAPI;

    @Before
    public void setUp() {
        // Configure retrofit object
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create web api proxy
        serviceAPI = retrofit.create(WebAPI.class);
    }

    @Test
    public void getCurrentWeather_withCorrectArguments_returnsResultOK() throws IOException {

        // Arrange
        final Call<WeatherInfoDTO> callSync = serviceAPI
                .getCurrentWeather(WebAPI.API_KEY, "London,pt", "en", "imperial");

        // Act
        final Response<WeatherInfoDTO> response = callSync.execute();
        final WeatherInfoDTO weatherInfo = response.body();

        // Assert
        // Verify HTTP result code
        final int OK_CODE = 200;
        assertEquals(OK_CODE, response.code());
        assertTrue(response.isSuccess());
        // Verify response body
        assertNotNull(weatherInfo);
        assertEquals(OK_CODE, weatherInfo.getResponseCode());
    }

    @Test
    public void getCurrentWeather_withInvalidApiKey_returnsResultUnauthorized() throws IOException {

        // Arrange
        final Call<WeatherInfoDTO> callSync = serviceAPI
                .getCurrentWeather("", "London,pt", "en", "imperial");

        // Act
        final Response<WeatherInfoDTO> response = callSync.execute();

        // Assert
        // Verify HTTP result code
        final int UNAUTHORIZED_CODE = 401;
        assertEquals(UNAUTHORIZED_CODE, response.code());
        assertFalse(response.isSuccess());
    }
}
