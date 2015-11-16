package pdm.demos.weatherapp.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.util.Log;


/**
 * Base class used for started services with logging support.
 */
public class LoggingService extends Service {

    private final String TAG = getClass().getSimpleName();

    @Override
    @MainThread
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @MainThread
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "LoggingService.onCreate()");
    }

    @Override
    @MainThread
    public void onDestroy() {
        Log.v(TAG, "LoggingService.onDestroy()");
        super.onDestroy();
    }

    @Override
    @MainThread
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "LoggingService.onStartCommand(...)");
        return super.onStartCommand(intent, flags, startId);
    }
}
