package demo.binea.com.basketballdemo.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import demo.binea.com.basketballdemo.R;

/**
 * Created by xubinggui on 6/25/15.
 */
public class BasketballView extends ImageView {

    public int screenWidth = 0;
    public int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
            getResources().getDisplayMetrics());
    public int radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14,
            getResources().getDisplayMetrics());
    public int originWidth = 0;
    public int originHeight = 0;
    public int standardWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 351,
            getResources().getDisplayMetrics());
    public int standardHeight = standardWidth / 2;

    public final int LEFT_UP_POINT_STANDARD_X =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105,
                    getResources().getDisplayMetrics()) + radius;
    public final int LEFT_UP_POINT_STANDARD_Y =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 54,
                    getResources().getDisplayMetrics()) + radius;

    public final int LEFT_BOTTOM_POINT_STANDARD_X = LEFT_UP_POINT_STANDARD_X;
    public final int LEFT_BOTTOM_POINT_STANDARD_Y =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 153,
                    getResources().getDisplayMetrics()) + radius;

    public final int RIGHT_UP_POINT_STAND_X =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220,
                    getResources().getDisplayMetrics()) + radius;
    public final int RIGHT_UP_POINT_STAND_Y = LEFT_UP_POINT_STANDARD_Y;

    public final int RIGHT_BOTTOM_POINT_STAND_X = RIGHT_UP_POINT_STAND_X;
    public final int RIGHT_BOTTOM_POINT_STAND_Y = LEFT_BOTTOM_POINT_STANDARD_Y;

    public int BOTTOM_CENTER_POINT_STAND_X = 0;
    public int BOTTOM_CENTER_POINT_STAND_Y = 0;

    //public int strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
    //        getResources().getDisplayMetrics());
    public int strokeWidth = 1;

    public final int textOffsetFromCircle =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14,
                    getResources().getDisplayMetrics());

    private Paint mPaint;
    private int basketViewHeight = 0;

    private Path mOvalPath;
    private Path mRectPath;

    private Region mRegion;
    private Region tempRegon;
    private RegionIterator mRegionIterator;

    private RectF mRectF;
    private Rect mTempRect;

    private final int MAX = 5;

    private TextPaint mTextPaint;

    public BasketballView(Context context) {
        this(context, null);
    }

    public BasketballView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasketballView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),
                R.drawable.training_challenge_court_medium_active, options);
        originWidth = options.outWidth;
        originHeight = options.outHeight;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(Color.YELLOW);

        mOvalPath = new Path();
        mRectPath = new Path();

        mRectF = new RectF();

        mRegion = new Region();
        tempRegon = new Region();

        mRegionIterator = new RegionIterator(tempRegon);

        mTempRect = new Rect();

        mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        //mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28,
                getResources().getDisplayMetrics()));
        mTextPaint.setColor(Color.WHITE);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        basketViewHeight = getMeasuredHeight();
        setMeasuredDimension(widthMeasureSpec, getMeasuredHeight() + radius + 5);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        BOTTOM_CENTER_POINT_STAND_X = BOTTOM_CENTER_POINT_STAND_Y = getWidth() / 2;
    }

    @Override protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float widthRatio = (float) getWidth() / (float) standardWidth;
        float heightRatio = (float) (getHeight() - radius) / (float) standardHeight;
        widthRatio = widthRatio < heightRatio ? widthRatio : heightRatio;
        drawLeftUp(canvas, widthRatio, 1);
        drawRightUp(canvas, widthRatio, 2);
        drawLeftBottom(canvas, widthRatio, 3);
        drawRightBottom(canvas, widthRatio, 4);
        drawCenterBottom(canvas, 1, 5);
    }

    private void drawCenterPointAndText(Canvas canvas, float ratio, int current) {

        canvas.drawCircle(getWidth() / 2, basketViewHeight, radius, mPaint);
    }

    private void drawLeftUp(Canvas canvas, float ratio, int current) {
        drawPointAndText(canvas, LEFT_UP_POINT_STANDARD_X, LEFT_UP_POINT_STANDARD_Y, ratio, current,
                ORIENTATION.LEFT);
    }

    private void drawRightUp(Canvas canvas, float ratio, int current) {
        drawPointAndText(canvas, RIGHT_UP_POINT_STAND_X, RIGHT_UP_POINT_STAND_Y, ratio, current,
                ORIENTATION.RIGHT);
    }

    private void drawLeftBottom(Canvas canvas, float ratio, int current) {
        drawPointAndText(canvas, LEFT_BOTTOM_POINT_STANDARD_X, LEFT_BOTTOM_POINT_STANDARD_Y, ratio, current,
                ORIENTATION.LEFT);
    }

    private void drawRightBottom(Canvas canvas, float ratio, int current) {
        drawPointAndText(canvas, RIGHT_BOTTOM_POINT_STAND_X, RIGHT_BOTTOM_POINT_STAND_Y, ratio, current,
                ORIENTATION.RIGHT);
    }

    private void drawCenterBottom(Canvas canvas, float ratio, int current) {
        drawPointAndText(canvas, getWidth() / 2, basketViewHeight, ratio, current,
                ORIENTATION.CENTER);
    }

    private void drawPointAndText(Canvas canvas, int x, int y, float ratio, int current,
            ORIENTATION orientation) {
        float actualX = ratio * x;
        float actualY = ratio * y;

        int left = (int) (actualX - radius);
        int top = (int) (actualY - radius);
        int right = (int) (actualX + radius);
        int bottom = (int) (actualY + radius);
        mRectF.left = left;
        mRectF.top = top;
        mRectF.right = right;
        mRectF.bottom = bottom;
        float height = mRectF.bottom - mRectF.top;
        float yHeight = current / (float) MAX * (height);
        float radius = this.radius;
        float angle = (float) (Math.acos((radius - yHeight) / radius) * 180 / Math.PI);
        float startAngle = 90 - angle;
        float sweepAngle = 180 - startAngle * 2;
        canvas.save();
        mOvalPath.addOval(mRectF, Path.Direction.CCW);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mOvalPath, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
        String tempStr = current + "/" + MAX;
        mTextPaint.getTextBounds(tempStr, 0, tempStr.length(), mTempRect);
        float textWidth = mTextPaint.measureText(tempStr, 0, tempStr.length());
        //mTempRect.offset(0, - mTempRect.top);
        float textX = 0;
        float textY = 0;
        if (orientation == ORIENTATION.LEFT) {
            textX = actualX - radius - textOffsetFromCircle - mTempRect.width();
            textY = actualY + mTempRect.height() / 2;
        } else if (orientation == ORIENTATION.RIGHT) {
            textX = actualX + radius + textOffsetFromCircle;
            textY = actualY + mTempRect.height() / 2;
        } else {
            textX = actualX - mTempRect.width() / 2;
            textY = actualY - radius - textOffsetFromCircle;
        }
        //canvas.drawRect(textX, textY - mTempRect.height(), textX + mTempRect.width(), textY, mPaint);
        canvas.drawText(tempStr, textX, textY, mTextPaint);
        canvas.restore();
    }

    enum ORIENTATION {
        LEFT,
        RIGHT,
        CENTER
    }
}
