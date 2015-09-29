package demos.pdm.activities101;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Base class for activities that perform logging of lifecycle callbacks.
 */
public class LoggingActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG, String.format( "onCreate() : savedInstanceState is%s null",
                savedInstanceState != null ? " not" : ""));
    }

    /** {@inheritDoc} */
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy()");
        super.onDestroy();
    }
}
