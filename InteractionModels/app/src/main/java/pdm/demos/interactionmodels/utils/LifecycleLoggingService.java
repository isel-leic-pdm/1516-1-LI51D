package pdm.demos.interactionmodels.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.util.Log;


/**
 * Base class used for started services with logging support.
 */
public class LifecycleLoggingService extends Service {

    private final String TAG = "DEMO_APP";

    @Override
    @MainThread
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @MainThread
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "LifecycleLoggingService.onCreate()");
    }

    @Override
    @MainThread
    public void onDestroy() {
        Log.v(TAG, "LifecycleLoggingService.onDestroy()");
        super.onDestroy();
    }

    @Override
    @MainThread
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "LifecycleLoggingService.onStartCommand(...)");
        return super.onStartCommand(intent, flags, startId);
    }
}
