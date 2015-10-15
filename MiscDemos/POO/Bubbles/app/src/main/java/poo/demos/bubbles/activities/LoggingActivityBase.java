package poo.demos.bubbles.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Base class for activities that support logging of life-cycle callbacks.
 */
public abstract class LoggingActivityBase extends Activity {

    /** The tag used for logging purposes. */
    protected final String TAG = this.getClass().getSimpleName();

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    /** {@inheritDoc} */
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    /** {@inheritDoc} */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }
}
