package coursera.android.projects.modernart.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Adapted from Android SDK samples, API level 17. Cleaned it up a bit but I didn't 
 * change the coding style, which I dislike (for example, poor naming). 
 */
@SuppressLint("ClickableViewAccessibility") 
public class ColorPickerView extends View {
	
	public static interface OnColorChangedListener {

		void colorChanged(int color);
	}
	
	private static final String COLOR_ATTRIBUTE = "color";
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
	private static final int DEFAULT_COLOR = Color.BLACK;

	private static final int[] mColors = new int[] {
		0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
		0xFFFFFF00, 0xFFFF0000
	};
	
	private static int parseColorString(String colorString) {
		int color = DEFAULT_COLOR;
		if(colorString != null)
		{
			try { color = Color.parseColor(colorString); }
			catch(IllegalArgumentException iae) { }
		}
		return color;
	}

	private Paint mPaint;
	private Paint mCenterPaint;
	private RectF mCircleBounds;
	private OnColorChangedListener mListener;
	
    private int CENTER_X;
    private int CENTER_Y;
    private int CENTER_RADIUS;

	private void init(int color) {
        Shader s = new SweepGradient(0, 0, mColors, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(s);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(32);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(color);
        mCenterPaint.setStrokeWidth(5);
        mCircleBounds = new RectF();
    }
    
    public ColorPickerView(Context c, int color) {
        super(c);
        init(color);
    }
    
	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(parseColorString(attrs.getAttributeValue(NAMESPACE, COLOR_ATTRIBUTE)));
	}

    private boolean mTrackingCenter;
    private boolean mHighlightCenter;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(CENTER_X, CENTER_X);
        canvas.drawOval(mCircleBounds, mPaint);
        canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);

        if (mTrackingCenter) {
            int c = mCenterPaint.getColor();
            mCenterPaint.setStyle(Paint.Style.STROKE);

            if (mHighlightCenter) {
                mCenterPaint.setAlpha(0xFF);
            } else {
                mCenterPaint.setAlpha(0x80);
            }
            canvas.drawCircle(0, 0,
                              CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
                              mCenterPaint);

            mCenterPaint.setStyle(Paint.Style.FILL);
            mCenterPaint.setColor(c);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Compute required square size
		int measuredSize = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
		setMeasuredDimension(measuredSize, measuredSize);
		
		CENTER_X = CENTER_Y = measuredSize / 2;
		CENTER_RADIUS = CENTER_X / 3;
        final float radius = CENTER_X - mPaint.getStrokeWidth()*0.5f;
		mCircleBounds.set(-radius, -radius, radius, radius); 
    }

    
    private int ave(int s, int d, float p) {
        return s + java.lang.Math.round(p * (d - s));
    }

    private int interpColor(int colors[], float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }

        float p = unit * (colors.length - 1);
        int i = (int)p;
        p -= i;

        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i+1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - CENTER_X;
        float y = event.getY() - CENTER_Y;
        boolean inCenter = java.lang.Math.sqrt(x*x + y*y) <= CENTER_RADIUS;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTrackingCenter = inCenter;
                if (inCenter) {
                    mHighlightCenter = true;
                    invalidate();
                    break;
                }
            case MotionEvent.ACTION_MOVE:
                if (mTrackingCenter) {
                    if (mHighlightCenter != inCenter) {
                        mHighlightCenter = inCenter;
                        invalidate();
                    }
                } else {
                    float angle = (float)java.lang.Math.atan2(y, x);
                    // need to turn angle [-PI ... PI] into unit [0....1]
                    float unit = (float) (angle/(2*Math.PI));
                    if (unit < 0) {
                        unit += 1;
                    }
                    mCenterPaint.setColor(interpColor(mColors, unit));
                    if(mListener != null)
                    	mListener.colorChanged(mCenterPaint.getColor());
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTrackingCenter) {
                    mTrackingCenter = false;    // so we draw w/o halo
                    invalidate();
                }
                break;
        }
        return true;
    }

    public void setColor(int argbColor) {
    	mCenterPaint.setColor(argbColor);
    	invalidate();
    }
    
    public int getColor() {
    	return mCenterPaint.getColor();
    }
    
    public void setOnColorChangedListener(OnColorChangedListener listener) {
    	mListener = listener;
    }
}


