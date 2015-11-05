package pdm.demos.weatherapp.utils;

import android.os.AsyncTask;
import android.os.HandlerThread;

import android.os.Handler;
import android.os.Looper;

import pdm.demos.weatherapp.providers.WeatherInfo;

/**
 * Unrealistic (due to its flawed simplicity) implementation of {@link AsyncTask}.
 *
 * <p>Its sole purpose is to illustrate the use of the HaMer framework, where thread orchestration
 * is done through message exchange, in contrast to the concurrency programming model based on
 * synchronization and shared mutable state.</p>
 *
 * <p>The implementation is not generic, it does not support cancellation, and it uses unbounded
 * thread creation, just to name some of the existing flaws.</p>
 */
public abstract class MyAsyncTask {

    /**
     * Same semantics as its real counterpart: it should be overridden to specify the work to be
     * executed in an alternative thread.
     * @param input The work's input.
     * @return The result.
     */
    protected abstract WeatherInfo doInBackground(final String input);

    /**
     * Same semantics as its real counterpart: called on the thread which originally triggered the
     * background work execution (i.e. the one that called {@link #execute(String)}, which is
     * usually the process main thread) to signal.
     * @param output The background work result.
     */
    protected void onPostExecution(WeatherInfo output) { }

    /**
     * Triggers the execution of the specified background work.
     * @param input The work's input.
     */
    public void execute(final String input) {

        // Unbound thread creation: usually a bad idea.
        final HandlerThread worker = new HandlerThread("Worker");
        worker.start();

        // Send an object bearing the computation to be performed to the newly created thread
        // (i.e. argument of post)
        new Handler(worker.getLooper()).post(new Runnable() {
            // This field is initialized by the same thread that instantiates its hosting object,
            // that is, the thread calling execute
            final Handler callerHandler = new Handler();
            @Override
            public void run() {
                // Executed on the "Worker" thread
                final WeatherInfo info = doInBackground(input);
                // Instantiate an object bearing the computation (and required state) to be
                // performed in the original thread (e.g. the main thread)
                callerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // This will run on the original thread
                        onPostExecution(info);
                    }
                });
            }
        });
    }
}
