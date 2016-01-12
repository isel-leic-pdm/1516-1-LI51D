package pdm.demos.interactionmodels;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * Implementation of Bound Service that supports both local and remote accesses, that is, accesses
 * from components regardless of their hosting process.
 */
public class MyRemoteBoundService extends Service implements MyBoundServiceContract {

    /**
     * Implementation of Binder for out-of-proc accesses. The Binder implementation delegates the
     * method call to the actual service implementation.
     */
    private final IBinder stub = new MyBoundServiceContract.Stub() {
        @Override
        public void doSomething(int value) throws RemoteException {
            MyRemoteBoundService.this.doSomething(value);
        }
    };

    /** {@inheritDoc} */
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    /** {@inheritDoc} */
    @Override
    public IBinder asBinder() {
        return stub;
    }

    /** {@inheritDoc} */
    @Override
    public void doSomething(int value) throws RemoteException {
        Log.v("MyRemoteBoundService", "doSomething()");
    }
}
