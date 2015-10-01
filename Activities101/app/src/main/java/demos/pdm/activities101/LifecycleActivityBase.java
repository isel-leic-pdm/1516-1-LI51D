package demos.pdm.activities101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Base class for activities on the Activity lifecycle demo.
 */
public abstract class LifecycleActivityBase extends LoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifecycle_activity);

        ((TextView) findViewById(R.id.msg_class)).setText(getClass().getSimpleName());
        ((TextView) findViewById(R.id.msg_hash)).setText(Integer.toString(hashCode()));

        findViewById(R.id.button_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifecycleActivityBase.this, MainActivity.class));
            }
        });

        findViewById(R.id.button_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifecycleActivityBase.this, OtherActivity.class));
            }
        });

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
