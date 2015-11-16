package pdm.demos.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pdm.demos.weatherapp.utils.LifecycleLoggingFragment;


/**
 * Class for fragments that host an {@link ImageView} instance whose content is downloaded
 * asynchronously.
 *
 * The implementation ensures that the fragment instance is retained across screen orientation
 * reconfigurations, to preserve the asynchronous load progress state.
 */
public class DownloadImageFragment extends LifecycleLoggingFragment {

    /** The fragment initialization parameters */
    private static final String ARG_URI = "image_uri";

    /** The progress bar */
    private ProgressBar progressBar;
    /** The image view */
    private ImageView imageView;
    /** The fragment's root container */
    private ViewGroup fragmentRootContainer;
    /** The image's Uri. */
    private String imageUri;
    /** The AsyncTask instance. */
    private AsyncTask<Void, Void, Bitmap> bitmapAsyncTask;

    /**
     * Factory method that produces a new instance of this fragment with
     * the given image URI.
     *
     * @param imageUri The URI of the image to be presented.
     * @return A new instance of this fragment.
     */
    public static DownloadImageFragment newInstance(String imageUri) {
        DownloadImageFragment fragment = new DownloadImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URI, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            imageUri = getArguments().getString(ARG_URI);

        setRetainInstance(true);
    }

    /** {@inheritDoc} */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // If the instance was not retained or it is the first time its views are being created
        if(fragmentRootContainer == null) {
            // Initialize views
            fragmentRootContainer = (ViewGroup) inflater.inflate(R.layout.image_fragment_layout, null);
            progressBar = (ProgressBar) fragmentRootContainer.findViewById(R.id.progress_bar);
            imageView = (ImageView) fragmentRootContainer.findViewById(R.id.image_view);
        }

        // Start loading bitmap
        asyncDownloadBitmap();

        return fragmentRootContainer;
    }

    /** {@inheritDoc} */
    @Override
    public void onDestroy() {
        // The fragment instance is being destroyed. Lets cancel the background work if appropriate
        if(bitmapAsyncTask != null && bitmapAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            bitmapAsyncTask.cancel(true);
        super.onDestroy();
    }

    /**
     * Method that downloads the bitmap in a background thread (using AsyncTask).
     */
    private void asyncDownloadBitmap() {
        bitmapAsyncTask = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return doDownLoadBitmap();
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                progressBar.setVisibility(View.GONE);
                // Update view's state
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

    /**
     * Method that downloads the image and produces the corresponding {@link android.graphics.Bitmap}
     * instance. Because it performs I/O, the method cannot be executed in the UI thread.
     *
     * @return The {@link android.graphics.Bitmap} instance with the downloaded image, or {@value null}
     * if an error occurred.
     */
    private Bitmap doDownLoadBitmap() {
        final URL url;
        try {
            url = new URL(imageUri.toString());
            HttpURLConnection urlConnection = null;
            try {
                // Simulated delay, for debugging and demonstration purposes
                try { Thread.sleep(2000); }
                catch (InterruptedException ignored) { }

                // Prepare to do actual work, while checking url availability
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream contentStream = new BufferedInputStream(urlConnection.getInputStream());
                // Do actual download work (note that the input stream contents are lazily obtained)
                return BitmapFactory.decodeStream(contentStream);

            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }
        catch (IOException e) {
            return null;
        }
    }
}
