package demos.pdm.activities101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Base class for activities on the Activity lifecycle demo. The purpose of this demo is to
 * illustrate the concept of user tasks and back-stack, while using activity navigation with
 * explicit intents.
 *
 * Because the activities of this demo are identical (we merely wish to distinguish them by their
 * concrete type, not their behaviour or look and feel), this class was created to capture their
 * commonalities.
 */
public abstract class LifecycleActivityBase extends LoggingActivity {

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifecycle_activity);

        // Views used to present the instance's class name and hash code... ;)
        ((TextView) findViewById(R.id.msg_class)).setText(getClass().getSimpleName());
        ((TextView) findViewById(R.id.msg_hash)).setText(Integer.toString(hashCode()));

        findViewById(R.id.button_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the main activity (using an explicit intent)
                startActivity(new Intent(LifecycleActivityBase.this, MainActivity.class));
            }
        });

        findViewById(R.id.button_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the other activity (using an explicit intent)
                startActivity(new Intent(LifecycleActivityBase.this, OtherActivity.class));
            }
        });

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Terminate the current activity, thereby revealing the one below it
                finish();
            }
        });
    }
}
