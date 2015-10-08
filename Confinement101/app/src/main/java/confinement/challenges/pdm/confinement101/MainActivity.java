package confinement.challenges.pdm.confinement101;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int STEP = 5, MAX_VALUE = 100;
    private TextView msgPercentage;

    private void updateView(int percentage) {
        msgPercentage.setText(Integer.toString(percentage));
    }

    private void doWork() {
        int percentage = 0;
        while(percentage <= MAX_VALUE) {
            updateView(percentage);
            doStep();
            percentage += STEP;
        }
    }

    private void doStep() {
        try {
            Thread.sleep(2000);
        }
        catch(InterruptedException ignored) { }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgPercentage = (TextView) findViewById(R.id.msg_percentage);

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                doWork();
            }
        });
    }
}
