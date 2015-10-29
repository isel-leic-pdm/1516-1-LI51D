package pdm.demos.weatherapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * The application's main activity.
 *
 * A possible design approach is to associate activities (and fragments) to the Controller
 * role in the MVC design pattern. That's the approach we are going to use in this application.
 */
public class MainActivity extends Activity {

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
