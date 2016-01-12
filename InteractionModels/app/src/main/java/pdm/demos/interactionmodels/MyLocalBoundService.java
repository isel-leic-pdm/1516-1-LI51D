package pdm.demos.interactionmodels;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Implementation of Bound Service which only supports local accesses, that is, accesses from
 * components hosted in the same process.
 */
public class MyLocalBoundService extends Service implements MyLocalBoundServiceContract {

    /**
     * Implementation of Binder for in-proc accesses. The Binder implementation delegates the
     * method call to the actual service implementation.
     */
    private class MyLocalLocalBinder extends Binder implements MyLocalBoundServiceContract {
        @Override
        public void doSomething(int value) {
            MyLocalBoundService.this.doSomething(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public IBinder onBind(Intent intent) {
        return new MyLocalLocalBinder();
    }

    /** {@inheritDoc} */
    @Override
    public void doSomething(int value) {
        Log.v("MyLocalBoundService", "doSomething()");
    }
}
