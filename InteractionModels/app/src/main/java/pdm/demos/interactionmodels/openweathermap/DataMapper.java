package pdm.demos.interactionmodels.openweathermap;

import android.support.annotation.NonNull;

import pdm.demos.interactionmodels.WeatherInfo;

/**
 * Class whose instances are used to map between instances of {@link WeatherInfo} and instances of
 * {@link WeatherInfoDTO}.
 */
public class DataMapper {

    /**
     * Converts the given {@link WeatherInfoDTO} instance to the corresponding {@link WeatherInfo}
     * instance.
     * @param dto The {@link WeatherInfoDTO} instance to be converted.
     * @return The resulting {@link WeatherInfo} instance.
     */
    @NonNull
    public WeatherInfo convertFrom(@NonNull WeatherInfoDTO dto) {
        return new WeatherInfo(
                dto.getCityName(),
                dto.getDescription(),
                dto.getWeatherIconURL(),
                dto.getWindSpeed(),
                dto.getWindDirection(),
                dto.getTemperature(),
                dto.getLowestTemperature(),
                dto.getHighestTemperature(),
                dto.getHumidity()
        );
    }
}
