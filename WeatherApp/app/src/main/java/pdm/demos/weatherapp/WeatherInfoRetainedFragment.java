package pdm.demos.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import pdm.demos.weatherapp.providers.WeatherInfoProvider;
import pdm.demos.weatherapp.utils.LifecycleLoggingFragment;

/**
 * Class that implements an headless (no UI) fragment whose instances are retained
 * across configuration changes. Hosting activities must implement the interface
 * {@link pdm.demos.weatherapp.providers.WeatherInfoProvider.Callback}
 *
 * TODO
 */
public class WeatherInfoRetainedFragment extends LifecycleLoggingFragment {

    private WeatherInfoProvider.Callback hostActivity;

    /** The fragment initialization parameters */
    private static final String ARG_CITY = "city_name";

    /**
     * Factory method that produces a new instance of this fragment with
     * the given image URI.
     *
     * @param cityName The name of the city for which the weather is to be fetched.
     * @return A new instance of this fragment.
     */
    public static WeatherInfoRetainedFragment newInstance(String cityName) {
        WeatherInfoRetainedFragment fragment = new WeatherInfoRetainedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String cityName = getArguments().getString(ARG_CITY);

        // Because this is a retained fragment, this method is only called when the hosting
        // Activity is being created anew. Therefore it is the most convenient spot to initiate
        // the asynchronous fetch of the weather information
        final WeatherApplication app = (WeatherApplication) getActivity().getApplication();
        final WeatherInfoProvider provider = app.getWeatherInfoProvider();
        provider.getWeatherInfoAsync(cityName, app.getLanguage(), app.getUnits(),
                new WeatherInfoProvider.Callback() {
                    @Override
                    public void onResult(@NonNull WeatherInfoProvider.CallResult result) {
                        try {
                            Log.v("DEBUG", result.getResult().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hostActivity.onResult(result);
                    }
                }
        );

        // Preserve instance across runtime configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        hostActivity = (WeatherInfoProvider.Callback) getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (WeatherInfoProvider.Callback) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO: Cancel ongoing asynchronous operation (if any)
    }
}
