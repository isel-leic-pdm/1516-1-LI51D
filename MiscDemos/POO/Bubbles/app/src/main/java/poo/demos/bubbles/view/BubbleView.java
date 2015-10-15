package poo.demos.bubbles.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import poo.demos.bubbles.model.Bubble;

/**
 * Class whose instances visually represent bubbles.
 */
public class BubbleView extends View {

    /** The brush instance used to paint the bubble. */
    private final Paint brush;
    /** The bubble's state. */
    private final Bubble bubble;
    /** The scaled image to be used to draw the bubble. */
    private final Bitmap bubbleImage;

    /**
     * Initializes the view's painting artifacts.
     */
    private void initBrush() {
        brush.setColor(Color.RED);
        brush.setStyle(Paint.Style.FILL);
    }

    /**
     * Initiates an instance with the given context.
     * @param context The view's context (i.e. the owning Activity)
     * @param model The view's model (i.e. the bubble's state)
     */
    public BubbleView(Context context, Bubble model, Bitmap baseImage) {
        super(context);
        this.bubble = model;

        this.bubbleImage = Bitmap.createScaledBitmap(
                baseImage,
                (int) (bubble.getRadius() * 2),
                (int) (bubble.getRadius() * 2),
                false
        );

        brush = new Paint();
        initBrush();

        model.setChangeListener(new Bubble.ChangeListener() {
            @Override
            public void onChange(Bubble source) {
                invalidate();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.rotate(bubble.getRotation(), bubble.getCenterX(), bubble.getCenterY());
        canvas.drawBitmap(
                bubbleImage,
                bubble.getCenterX() - bubble.getRadius(),
                bubble.getCenterY() - bubble.getRadius(),
                brush
        );
        canvas.restore();
    }

    /**
     * Gets the {@link Bubble} instance that holds this
     * view's state.
     * @return The corresponding {@link Bubble} instance.
     */
    public Bubble getBubbleState() {
        return bubble;
    }

    /**
     * Method that checks whether the bubble is outside its view. Notice that if a layout is
     * requested, the method returns {@value false} because there is no way to be certain about
     * the view's current size.
     * @return {@value true} if the bubble is no longer visible, {@value false} otherwise.
     */
    public boolean isOutOfView() {
        return !(isLayoutRequested() || bubble.intersects(0, 0, getWidth(), getHeight()));
    }
}
