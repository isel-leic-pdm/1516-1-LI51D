package pdm.demos.weatherapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pdm.demos.weatherapp.providers.WeatherInfo;
import pdm.demos.weatherapp.providers.WeatherInfoProvider;
import pdm.demos.weatherapp.providers.openweathermap.OpenWeatherProvider;
import pdm.demos.weatherapp.utils.MyAsyncTask;

/**
 * The application's main activity.
 *
 * A possible design approach is to associate activities (and fragments) to the Controller
 * role in the MVC design pattern. That's the approach we are going to use in this application.
 *
 * TODO: Preserve view state. Cancel background work upon activity destruction. Use headless
 * fragment to preserve asynchronous fetching of weather info across configuration changes
 * (if we are dealing with a screen orientation change)
 */
public class MainActivity extends Activity {

    /** The edit text used to collect the city name entered by the user. */
    private EditText cityTextView;
    /** The buttons used to trigger fetching the weather information. */
    private Button buttonAsync, buttonRetro;

    /**
     * Enables the user interface, thereby allowing for user input.
     */
    private void enableUI() {
        buttonAsync.setEnabled(true);
        buttonRetro.setEnabled(true);
        cityTextView.setText("");
        cityTextView.setEnabled(true);
    }

    /**
     * Disables the user interface, thereby inhibiting user input.
     */
    private void disableUI() {
        buttonAsync.setEnabled(false);
        buttonRetro.setEnabled(false);
        cityTextView.setEnabled(false);
    }

    /**
     * Method that gets the content of the {@link EditText}, validating it.
     * @return The user's input, or null if it was invalid.
     */
    private String getValidatedUserInput() {
        final String cityName = cityTextView.getText().toString();
        return cityName.trim().isEmpty() ? null : cityName;
    }

    /**
     * Navigates to {@link WeatherActivity} passing it the given weather information.
     * @param info The weather information to be displayed.
     */
    private void navigateToWeatherActivity(WeatherInfo info) {
        startActivity(WeatherActivity.makeIntent(this, info));
    }

    /**
     * Gets the weather information.
     */
    private void fetchWeatherInfo(String cityName) {
        final WeatherApplication application = (WeatherApplication) getApplication();
        application.getWeatherInfoProvider()
            .getWeatherInfoAsync(cityName, application.getLanguage(), application.getUnits(),
                    new WeatherInfoProvider.Callback() {
                        @Override
                        public void onResult(@NonNull WeatherInfoProvider.CallResult result) {
                            enableUI();
                            try {
                                navigateToWeatherActivity(result.getResult());
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, R.string.error_msg_couldntget, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }
            );
    }

    /**
     * Merely used for demonstration purposes. Otherwise it is useless.
     */
    private void demoFetchWeatherInfoWithAsyncTask(String cityName) {
        (new MyAsyncTask() {
            private final WeatherApplication application;
            private final String language;
            private final OpenWeatherProvider.UnitSystem units;

            {
                application = (WeatherApplication) getApplication();
                language = application.getLanguage();
                units = application.getUnits();
            }

            // Synchronization not needed here because we are piggy backing it on the
            // underlying thread synchronization... ;) (PC rules!)
            private Exception error;

            @Override
            protected WeatherInfo doInBackground(String cityName) {
                try {
                    return application.getWeatherInfoProvider()
                                .getWeatherInfo(cityName, language, units);
                } catch (Exception e) {
                    error = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecution(WeatherInfo weatherInfo) {
                enableUI();
                navigateToWeatherActivity(weatherInfo);
            }
        }).execute(cityName);
    }

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = (EditText) findViewById(R.id.city);

        (buttonRetro = (Button) findViewById(R.id.button_retro))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View button) {
                        final String city = getValidatedUserInput();
                        if(city != null) {
                            disableUI();
                            fetchWeatherInfo(city);
                        }
                    }
                });

        (buttonAsync = (Button) findViewById(R.id.button_async)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View button) {
                        final String city = getValidatedUserInput();
                        if(city != null) {
                            disableUI();
                            demoFetchWeatherInfoWithAsyncTask(city);
                        }
                    }
                });
    }
}
