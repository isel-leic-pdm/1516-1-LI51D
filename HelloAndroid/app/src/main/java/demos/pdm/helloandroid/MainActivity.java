package demos.pdm.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int counterValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView msgBox = (TextView) findViewById(R.id.msg_box);

        findViewById(R.id.dec_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                msgBox.setText(String.valueOf(--counterValue));
            }
        });

        findViewById(R.id.inc_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                msgBox.setText(String.valueOf(++counterValue));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            TextView msgBox = (TextView) findViewById(R.id.msg_box);
            msgBox.setText("UAU");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
