package pdm.demos.weatherapp;

import android.os.Bundle;

import pdm.demos.weatherapp.utils.LifecycleLoggingFragment;

/**
 * Class that implements an headless (no UI) fragment whose instances are retained
 * across configuration changes. Hosting activities must implement the interface
 * {@link pdm.demos.weatherapp.providers.WeatherInfoProvider.Callback}
 *
 * TODO
 */
public class WeatherInfoRetainedFragment extends LifecycleLoggingFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the completion listener that forwards callbacks to the hosting Activity.

        // Because this is a retained fragment, this method is only called when the hosting
        // Activity is being created anew. Therefore it is the most convenient spot to initiate
        // the bind protocol

        // TODO:

        // Preserve instance across runtime configuration changes.
        setRetainInstance(true);
    }
}
