package pdm.demos.interactionmodels.openweathermap;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Contract that specifies a programmatic interface for accessing the weather API.
 *
 * <p>This interface is annotated with <a href="http://square.github.io/retrofit/">Retrofit</a>
 * annotations and it uses {@link Call} as a return type. If the remainder of the application's
 * codebase depends directly of this contract, we create a pervasive dependency to this library.
 * When developing software solutions one must consider this consequence carefully. Third-party
 * dependencies may become a liability.</p>
 */
public interface WebAPI {

    /** The base URL of the Weather Service API. */
    String BASE_URL = "http://api.openweathermap.org";

    /** The application's API key. */
    String API_KEY = "7b392e0c3fa214914168139b20ea1b81";

    /**
     * Gets the current weather information for the given city. The returned result is the best
     * match. Corresponds to HTTP requests in the form:
     * GET {@literal http://api.openweathermap.org/data/2.5/weather?q=city_name&appid=api_key}
     *
     *          The application's API key.
     * @param city
     *          The city name. For more accuracy, the received string should be in the format
     *          city_name,country_code. (E.g. Lisbon,pt)
     * @param lang
     *          The language to be used in the query (e.g. pt, en). Supported languages are
     *          identified <a href="http://openweathermap.org/current#multi">here</a>.
     * @param units
     *          The unit system to be used. Supported unit systems are identified
     *          <a href="http://openweathermap.org/current#units">here</a>.
     * @return The information of the current weather at the given city.
     */
    @GET("/data/2.5/weather")
    Call<WeatherInfoDTO> getCurrentWeather(
            @Query("appid") String apiKey,
            @Query("q") String city,
            @Query("lang") String lang,
            @Query("units") String units
    );
}
