package poo.demos.bubbles.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class whose instances represent bubbles' state.
 */
public class Bubble implements Parcelable {

    /**
     * Contract to be supported by types interested in receiving
     * game state changes notifications.
     */
    public interface ChangeListener {
        void onChange(Bubble source);
    }

    /** The bubble's maximum rotation angle (expressed in degrees). */
    public static final float MAX_ROTATION = 360;
    /** The bubble's center coordinates. */
    private float centerX, centerY;
    /** The bubble's radius. */
    private float radius;
    /** The bubble's rotation. */
    private float rotation;
    /** Cached squared radius value. */
    private final float squaredRadius;
    /** The bubble's angular variation. */
    private float dR;
    /** The bubble's rectangular variation. */
    private float dX, dY;

    /** The listener of change events. */
    private ChangeListener listener;

    /**
     * Produces change state events, that is, notifies an eventual interested party.
     */
    private void fireChangeEvent() {
        if(listener != null)
            listener.onChange(this);
    }

    /**
     * Registers the given listener to receive state changes notifications.
     * @param listener The listener object.
     */
    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }


    /** {@inheritDoc} */
    @Override
    public int describeContents() {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(centerX);
        dest.writeFloat(centerY);
        dest.writeFloat(radius);
        dest.writeFloat(rotation);
        dest.writeFloat(dR);
        dest.writeFloat(dX);
        dest.writeFloat(dY);
        // squaredRadius is not stored because it can be re-computed
    }

    /**
     * Initiates an instance with the data stored in the given {@link Parcel}.
     * @param in The {@link Parcel} where the data is stored.
     */
    private Bubble(Parcel in) {
        centerX = in.readFloat();
        centerY = in.readFloat();
        radius = in.readFloat();
        rotation = in.readFloat();
        dR = in.readFloat();
        dX = in.readFloat();
        dY = in.readFloat();
        // squaredRadius was not stored. Re-compute it.
        squaredRadius = radius * radius;
    }

    /**
     * Factory used by the Parcelable framework.
     */
    public static final Parcelable.Creator<Bubble> CREATOR = new Parcelable.Creator<Bubble>() {
        public Bubble createFromParcel(Parcel in) {
            return new Bubble(in);
        }

        public Bubble[] newArray(int size) {
            return new Bubble[size];
        }
    };


    /**
     * Initializes an instance with the given center coordinates
     * and radius.
     * @param centerX The bubble's center X coordinate.
     * @param centerY The bubble's center Y coordinate.
     * @param radius The bubble's radius.
     */
    public Bubble(float centerX, float centerY, float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.squaredRadius = radius * radius;
        this.rotation = 0;
        this.dX = this.dY = this.dR = 0;
    }

    /**
     * Gets the bubble's center X coordinate.
     * @return The bubble's center X coordinate.
     */
    public float getCenterX() {
        return centerX;
    }

    /**
     * Gets the bubble's center Y coordinate.
     * @return The bubble's center Y coordinate.
     */
    public float getCenterY() {
        return centerY;
    }

    /**
     * Sets the bubble's center coordinates.
     * @param x The new center X coordinate.
     * @param y The new center Y coordinate.
     */
    public Bubble setCenter(float x, float y) {
        centerX = x;
        centerY = y;
        fireChangeEvent();
        return this;
    }

    /**
     * Sets the bubble's radius to the given value.
     * @param newRadius the bubble's new radius.
     */
    public Bubble setRadius(float newRadius) {
        radius = newRadius;
        fireChangeEvent();
        return this;
    }

    /**
     * Gets the bubble's radius.
     * @return The bubble's radius.
     */
    public float getRadius() {
        return radius;
    }


    /**
     * Sets the bubble's rotation to the given value (expressed in degrees).
     * @param newRotation The bubbles new rotation in the interval [0..360[.
     */
    public Bubble setRotation(float newRotation) {
        rotation = newRotation % MAX_ROTATION;
        fireChangeEvent();
        return this;
    }

    /**
     * Gets the bubble's current rotation.
     * @return The bubble's current rotation.
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Sets the bubble's cartesian variation on each movement step.
     * @param dX The X coordinate of the variation.
     * @param dY The Y coordinate of the variation.
     * @return The bubble instance, to enable fluent use.
     */
    public Bubble setCenterStep(float dX, float dY) {
        this.dX = dX;
        this.dY = dY;
        return this;
    }

    /**
     * Sets the bubble's rotation variation on each movement step.
     * @param dR The rotation variation, expressed in degrees.
     * @return The bubble instance, to enable fluent use.
     */
    public Bubble setRotationStep(float dR) {
        this.dR = dR;
        return this;
    }

    /**
     * Moves the bubble by the currently specified step variation.
     * @return The bubble instance, to enable fluent use.
     */
    public Bubble doStep() {
        setCenter(getCenterX() + dX, getCenterY() + dY).setRotation(getRotation() + dR);
        fireChangeEvent();
        return this;
    }

    /**
     * Checks if the given point is contained within the bubble.
     * @param x The point's X coordinate.
     * @param x The point's Y coordinate.
     * @return {@value true} if the point is contained in the bubble,
     * {@value false} otherwise.
     */
    public boolean contains(float x, float y) {
        final float distX = x - centerX, distY = y - centerY;
        return distX * distX + distY * distY <= squaredRadius;
    }

    /**
     * Checks if the bubble intersects the given rectangle. The rectangle is defined
     * by its upper-left and bottom-right coordinates.
     * @param left The X coordinate of the rectangle's upper-left corner.
     * @param top The Y coordinate of the rectangle's upper-left corner.
     * @param right The X coordinate of the rectangle's bottom-right corner.
     * @param bottom The Y coordinate of the rectangle's bottom-right corner.
     * @return
     */
    public boolean intersects(float left, float top, float right, float bottom) {
        return (centerX + radius >= left && centerX - radius <= right) &&
                (centerY + radius >= top && centerY - radius <= bottom);
    }
}
