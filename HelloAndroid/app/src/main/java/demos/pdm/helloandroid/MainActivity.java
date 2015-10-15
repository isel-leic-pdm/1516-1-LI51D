package demos.pdm.helloandroid;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Activity used to illustrate the consequences of a managed life-cycle. It also demonstrates
 * how to use locale dependent strings (see res/values/strings.xml and res/values-pt/strings.xml).
 *
 * Disclaimer: This is a demo and therefore its purpose is to illustrate the use of mechanisms.
 * It is not about software design. Mindlessly adapting these demos to your solutions is not
 * an appropriate approach to software development.
 */
public class MainActivity extends LoggingActivity {

    /** Key used to identify the object containing the view state to be stored */
    private final String STATE_KEY = "MainActivity.UIState";

    /**
     * Class used to characterize the view state to be preserved across configuration changes.
     * Because view state may cross process boundaries, the type must adhere to the {@link Parcelable}
     * contract.
     */
    private static class State implements Parcelable {

        /** Immutable field containing the message box's visibility. */
        public final int MSG_BOX_VISIBILITY;

        /**
         * Static field that adheres to the {@link Parcelable} convention and specifies the
         * factory of {@link demos.pdm.helloandroid.MainActivity.State} instances.
         */
        public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator<State>() {

            /**
             * Creates an instance from the data contained within the given {@link Parcel}
             * @param source The parcel containing the instance's state
             * @return The newly created instance.
             */
            @Override
            public State createFromParcel(Parcel source) {
                return new State(source);
            }

            /**
             * Creates an array of State
             * @param size The array size
             * @return The newly created array (containing no instances).
             */
            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };

        /**
         * Private constructor used to initiate an instance from the given {@link Parcel}.
         * @param source The parcel containing the new instance's state.
         */
        private State(Parcel source) {
            this(source.readInt());
        }

        /**
         * Initiates an instance with the given visibility.
         * @param msgBoxVisibility The message's box visibility.
         */
        public State(int msgBoxVisibility) {
            MSG_BOX_VISIBILITY = msgBoxVisibility;
        }

        /** {@inheritDoc} */
        @Override
        public int describeContents() {
            return 0;
        }

        /**
         * Stores the instance's state in the given {@link Parcel} instance.
         * (Used to create the object's external representation)
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(MSG_BOX_VISIBILITY);
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            // Load state from bundle, if one has been passed in
            final State uiState = savedInstanceState.getParcelable(STATE_KEY);
            // Update view accordingly
            findViewById(R.id.msg_box).setVisibility(uiState.MSG_BOX_VISIBILITY);
        }

        findViewById(R.id.button_say).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                findViewById(R.id.msg_box).setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Callback method that gives the opportunity to save view state in the given {@link Bundle}.
     * Notice that {@link Bundle} instances may cross process boundaries, hence the additional
     * requirements to the contained elements (e.g. {@link Parcelable}).
     * @param instanceState The {@link Bundle} instance where the view state is to be stored.
     */
    @Override
    protected void onSaveInstanceState(Bundle instanceState) {
        // Create object representing the view's state
        final State uiState = new State(findViewById(R.id.msg_box).getVisibility());
        // Store it in the bundle
        instanceState.putParcelable(STATE_KEY, uiState);
        // Delegate remaining actions (if any) to the inherited implementation
        super.onSaveInstanceState(instanceState);
    }
}
