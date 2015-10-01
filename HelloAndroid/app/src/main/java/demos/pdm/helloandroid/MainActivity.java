package demos.pdm.helloandroid;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class MainActivity extends LoggingActivity {

    private final String STATE_KEY = "MainActivity.UIState";

    private static class State implements Parcelable {

        public final int MSG_BOX_VISIBILITY;

        public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator<State>() {

            @Override
            public State createFromParcel(Parcel source) {
                return new State(source);
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };

        private State(Parcel source) {
            this(source.readInt());
        }

        public State(int msgBoxVisibility) {
            MSG_BOX_VISIBILITY = msgBoxVisibility;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(MSG_BOX_VISIBILITY);
        }
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            // Load state
            final State uiState = savedInstanceState.getParcelable(STATE_KEY);
            findViewById(R.id.msg_box).setVisibility(uiState.MSG_BOX_VISIBILITY);
        }

        findViewById(R.id.button_say).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.msg_box).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle instanceState) {
        final State uiState = new State(findViewById(R.id.msg_box).getVisibility());
        instanceState.putParcelable(STATE_KEY, uiState);
        super.onSaveInstanceState(instanceState);
    }
}
