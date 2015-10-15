package confinement.challenges.pdm.confinement101;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity used to illustrate the consequences of thread-confinement.
 *
 * Remember this: All calbacks (unless stated otherwise) are sequentially executed in the process
 * main thread. This means that callback execution is constrained and must return as soon as
 * possible. Long running operations or I/O are therefore forbidden.
 *
 * The goal of this demo is to illustrate how to address the issue, using AsyncTask, if one actually
 * needs to perform a long running operation, while updating the UI with its progress.
 *
 * The demo also illustrates a fundamental flaw when a configuration change occurs (i.e. screen
 * rotation) while the long running operation is in progress. Try it out... ;)
 * The solution to this problem is not presented herein. A demo on that problem will come later.
 *
 *
 * Disclaimer: This is a demo and therefore its purpose is to illustrate the use of mechanisms.
 * It is not about software design. Mindlessly adapting these demos to your solutions is not
 * an appropriate approach to software development.
 */
public class MainActivity extends AppCompatActivity {

    /** The progress value for a completed operation. */
    private static final int MAX_VALUE = 100;
    /** Progress is updated by the following increment value. */
    private static final int STEP = 5;

    /** Key used to identify progress state in the view's state Bundle. */
    private static final String STATE = "State";

    /** The view used to display progress. */
    private TextView msgPercentage;

    /** Holds the latest asyncTask instance. */
    private AsyncTask<Void, Integer, Integer> progress;

    /**
     * Helper method used to update the UI whenever the current progress changes.
     * @param percentage The current progress value.
     */
    private void updateView(int percentage) {
        msgPercentage.setText(Integer.toString(percentage));
    }

    /**
     * Helper method used to simulate a step (i.e. iteration) of the long running operation.
     * Notice that this method is simulating a long operation (i.e. at least 500 ms long)
     */
    private static void doStep() {
        // Disclaimer. This is a demo! The code below is terrible in all aspects!
        try { Thread.sleep(500); }
        catch (InterruptedException ignored) { }
    }

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lets restore UI state, if present
        msgPercentage = (TextView) findViewById(R.id.msg_percentage);
        if(savedInstanceState != null)
            msgPercentage.setText(Integer.toString(savedInstanceState.getInt(STATE)));

        // Setup behaviour of the start button in order to simulate the long running operation
        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                // Is there an operation currently in progress? If not, start one.
                if(progress != null)
                    return;

                // Define the long running operation and corresponding UI updates.
                progress = (new AsyncTask<Void, Integer, Integer>() {
                    /** {@inheritDoc} */
                    @Override
                    protected Integer doInBackground(Void... params) {
                        // Specifies the long running work. The method is executed in an alternative
                        // thread, provided by the AsyncTask implementation.
                        // Notice that there is no shared mutable state (at the application level)
                        // between the executing thread and the main thread.
                        int percentage = 0;
                        while (percentage <= MAX_VALUE) {
                            // We must check if the operation has been cancelled.
                            if(isCancelled())
                                return percentage;
                            // Request progress update. The method can be safely called from this
                            // thread and promotes the forwarding of the request to the main thread.
                            publishProgress(percentage);
                            doStep();
                            percentage += STEP;
                        }
                        return MAX_VALUE;
                    }

                    /** {@inheritDoc} */
                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        // Callback method used to update the UI whenever a progress update is
                        // requested. Executed in the main thread and therefore we can safely update
                        // the UI.
                        Log.v("PROGRESS", "Current progress is " + values[0]);
                        updateView(values[0]);
                    }

                    /** {@inheritDoc} */
                    @Override
                    protected void onCancelled() {
                        // Callback method used to update the UI when the operation is cancelled
                        // It is executed in the main thread and therefore we can safely update
                        // the UI. (in this case, by presenting a Toast)
                        progress = null;
                        Toast.makeText(MainActivity.this, "Cancelled!", Toast.LENGTH_LONG).show();
                    }

                    /** {@inheritDoc} */
                    @Override
                    protected void onPostExecute(Integer integer) {
                        // Callback method used to update the UI when the operation is successfully
                        // completed. It is executed in the main thread and therefore we can safely
                        // update the UI. (in this case, by presenting a Toast)
                        progress = null;
                        Toast.makeText(
                                MainActivity.this, "Done : " + integer, Toast.LENGTH_LONG
                        ).show();

                    }
                });
                // Start long running operation. Once it is completed (either successfully or
                // canceled), the async task instance is discarded (they can only be used once)
                progress.execute();
            }
        });

        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress != null)
                    progress.cancel(true);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save progress state
        outState.putInt(STATE, Integer.parseInt(msgPercentage.getText().toString()));
        super.onSaveInstanceState(outState);
    }
}
