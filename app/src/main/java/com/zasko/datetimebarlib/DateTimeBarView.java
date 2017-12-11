package com.zasko.datetimebarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zasko on 2017/12/11.
 */

public class DateTimeBarView extends View {

    public DateTimeBarView(Context context) {
        super(context);
        initData(context, null);
    }

    public DateTimeBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }

    public DateTimeBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    private float mLineWidth; /*刻度宽*/
    private float mCenterLineWidth; /*中间刻度宽*/
    private int mLineColor; /*刻度颜色*/
    private int mCenterLineColor; /*中间刻度颜色*/

    /*文字*/
    private Paint mPaintText;
    /*刻度*/
    private Paint mPaintLine;
    /*中间刻度*/
    private Paint mPaintCenterLine;

    private void initData(Context context, AttributeSet attrs) {
        mPaintCenterLine = new Paint();
        mPaintLine = new Paint();
        mPaintText = new Paint();

        mPaintCenterLine.setAntiAlias(true);
        mPaintLine.setAntiAlias(true);
        mPaintText.setAntiAlias(true);

        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DateTimeBarView);
        }

        mPaintLine.setStrokeWidth(optPixelSize(typedArray, R.styleable.DateTimeBarView_timeBar_lineWidth, 4f));
        mPaintLine.setColor(optColor(typedArray, R.styleable.DateTimeBarView_timeBar_lineColor, Color.parseColor("#728abb")));

        mPaintCenterLine.setStrokeWidth(optPixelSize(typedArray, R.styleable.DateTimeBarView_timeBar_centerLineWidth, 4f));
        mPaintCenterLine.setColor(optColor(typedArray, R.styleable.DateTimeBarView_timeBar_centerLineColor, Color.parseColor("#728abb")));


        mPaintText.setStrokeWidth(2);
        mPaintText.setTextSize(optPixelSize(typedArray, R.styleable.DateTimeBarView_timeBar_textSize, 33f));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    private static float dp2px() {
        return 0;
    }

    /**/
    private static float optPixelSize(TypedArray typedArray, int index, float def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getDimension(index, def);
    }

    /**/
    private static int optPixelSize(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getDimensionPixelOffset(index, def);
    }

    /*color*/
    private static int optColor(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getColor(index, def);
    }


}
