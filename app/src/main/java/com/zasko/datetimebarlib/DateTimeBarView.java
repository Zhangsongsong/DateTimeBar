package com.zasko.datetimebarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zasko on 2017/12/11.
 */

public class DateTimeBarView extends View {

    private static final String TAG = "DateTimeBarView";


    /*默认线宽*/
    private static final float DEFAULT_LINE_WIDTH = 3f;


    /*默认刻度颜色*/
    private static final int DEFAULT_LINE_COLOR = Color.parseColor("#728abb");

    /*默认文本大小*/
    private static final float DEFAULT_TEXT_SIZE = 33f;

    /*一个小时默认条数*/
    private static final int DEFAULT_HOUR_MAX_NUM = 6;

    /*屏幕最大支持小时*/
    private static final int DEFAULT_VIEW_MAX_NUM = 2;

    /*默认小时底部缩进（比例）*/
    private static final float DEFAULT_HOUR_TOP_PADDING = 0.9f;

    /*默认小时顶部缩进（比例）*/
    private static final float DEFAULT_HOUR_BOTTOM_PADDING = 0.3f;

    private static final float DEFAULT_HALF_PADDING = 0.5f;


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

        mPaintLine.setStrokeWidth(optPixelSize(typedArray, R.styleable.DateTimeBarView_timeBar_lineWidth, DEFAULT_LINE_WIDTH));
        mPaintLine.setColor(optColor(typedArray, R.styleable.DateTimeBarView_timeBar_lineColor, DEFAULT_LINE_COLOR));

        mPaintCenterLine.setStrokeWidth(optPixelSize(typedArray, R.styleable.DateTimeBarView_timeBar_centerLineWidth, DEFAULT_LINE_WIDTH));
        mPaintCenterLine.setColor(optColor(typedArray, R.styleable.DateTimeBarView_timeBar_centerLineColor, DEFAULT_LINE_COLOR));


        mPaintText.setStrokeWidth(2);
        mPaintText.setTextSize(optPixelSize(typedArray, R.styleable.DateTimeBarView_timeBar_textSize, DEFAULT_TEXT_SIZE));
        mPaintText.setColor(optColor(typedArray, R.styleable.DateTimeBarView_timeBar_textColor, DEFAULT_LINE_COLOR));


        mHourMaxNum = optInt(typedArray, R.styleable.DateTimeBarView_timeBar_hoursMax, DEFAULT_HOUR_MAX_NUM);
        mViewMaxNum = optInt(typedArray, R.styleable.DateTimeBarView_timeBar_viewMax, DEFAULT_VIEW_MAX_NUM);


        mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        mCurrentTime = System.currentTimeMillis();
    }


    /*一秒所占据的像素*/
    private float mSecWidth = 0;
    /*一分钟所占据的像素*/
    private float mMinWidth = 0;
    /*一个小时占据的*/
    private float mHouWidth = 0;


    /*一个像素占据多少时间*/
    private float mPxTime;

    /*View 的长度*/
    private float mViewWidth;
    /*View的高度*/
    private float mViewHeight;


    /*一个小时最大刻度*/
    private int mHourMaxNum = 0;
    /*屏幕宽度占据多少个小时*/
    private int mViewMaxNum = 0;


    private SimpleDateFormat mDateFormat;
    private long mCurrentTime = 0;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();

        mHouWidth = mViewWidth / mViewMaxNum;
        mMinWidth = mHouWidth / 60f;
        mSecWidth = mMinWidth / 60f;

        mPxTime = 1 / mSecWidth;

        Log.d(TAG, "onMeasure: ------->" + mViewWidth + "---小时："
                + mHouWidth + "---分钟：" + mMinWidth + "---秒：" + mSecWidth + "---像素：" + mPxTime);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawLine(canvas);
        drawCenterLine(canvas);

    }

    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
    }

    /**
     * 绘制刻度线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        String tmpDate = mDateFormat.format(new Date(mCurrentTime));
        mPaintLine.setColor(Color.WHITE);

//        Log.d(TAG, "drawLine: -------->" + tmpDate);

        String timeDate = tmpDate.split(" ")[1];
        String[] timeDates = timeDate.split(":");
        int hours = Integer.parseInt(timeDates[0]);
        int minute = Integer.parseInt(timeDates[1]);
        int sec = Integer.parseInt(timeDates[2]);
        hours++;

        Rect rect = new Rect();
        mPaintLine.getTextBounds("00:00", 0, "00:00".length(), rect);
        int strWidth = rect.width();
        int strHeight = rect.height();

        float halfViewWidth = mViewWidth / 2;
        float nextHourWidth = halfViewWidth + ((60 - minute) * 60 + (60 - sec)) * mSecWidth;

//        Log.d(TAG, "drawLine: --------->" + halfViewWidth + "---" + nextHourWidth);

        float tmpMaxTopHeight = mViewHeight * DEFAULT_HOUR_BOTTOM_PADDING;
        float tmpMaxBottomHeight = mViewHeight * DEFAULT_HOUR_TOP_PADDING;
        float tmpMaxHalfHeight = mViewHeight * DEFAULT_HALF_PADDING;

        /*绘制右边*/
        float tmpRightLocal = nextHourWidth;
        int rightCount = 0;
        int rightHour = hours;
        do {

            if (rightCount % mHourMaxNum == 0) {
                canvas.drawLine(tmpRightLocal, tmpMaxTopHeight, tmpRightLocal, tmpMaxBottomHeight, mPaintLine);
                String timeText = String.format("%02d:00", (rightHour % 24));
                canvas.drawText(timeText, tmpRightLocal - strWidth, tmpMaxTopHeight + strHeight - 10, mPaintText);
                rightHour++;


            } else {
                canvas.drawLine(tmpRightLocal, tmpMaxHalfHeight, tmpRightLocal, tmpMaxBottomHeight, mPaintLine);
            }

            tmpRightLocal += mMinWidth * (60 / mHourMaxNum);
            rightCount++;

        } while (tmpRightLocal < mViewWidth);


        /*绘制左边*/
        float tmpLeftLocal = nextHourWidth;
        int leftCount = 0;
        int leftHour = hours;
        do {
//            canvas.drawLine(tmpLeftLocal, 0, tmpLeftLocal, mViewHeight, mPaintLine);
            if (leftCount % mHourMaxNum == 0) {
                canvas.drawLine(tmpLeftLocal, tmpMaxTopHeight, tmpLeftLocal, tmpMaxBottomHeight, mPaintLine);
                String timeText = String.format("%02d:00", ((leftHour + 24) % 24));
                canvas.drawText(timeText, tmpLeftLocal - strWidth, tmpMaxTopHeight + strHeight - 10, mPaintText);
                leftHour--;
            } else {
                canvas.drawLine(tmpLeftLocal, tmpMaxHalfHeight, tmpLeftLocal, tmpMaxBottomHeight, mPaintLine);
            }
            tmpLeftLocal -= mMinWidth * (60 / mHourMaxNum);
            leftCount++;
        } while (tmpLeftLocal > -1);

        canvas.drawLine(0, mViewHeight * DEFAULT_HOUR_TOP_PADDING, mViewWidth, mViewHeight * DEFAULT_HOUR_TOP_PADDING, mPaintLine);

    }

    /**
     * 绘制中间的线
     *
     * @param canvas
     */
    private void drawCenterLine(Canvas canvas) {

        canvas.drawLine(mViewWidth / 2, 0, mViewWidth / 2, mViewHeight, mPaintCenterLine);
    }

    /*是否双指以上按下*/
    private boolean isActionPointerDown;

    /*滑动需要的参数： 第一次按下时记录当前位置*/
    private float mActionDownFirstX;
    /*第一次按下时记录当前的时间*/
    private long mActionDownFirstTime;
    /*单指移动的距离*/
    private float mTmpMoveX = 0f;

//    /*双指移动的距离*/
//    private float mDoubleMoveX = 0f;
//    private float mDoubleFirstBet = 0;
//    private int mDoubleMoveIndex = 0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "onTouchEvent: ---------->" + event.getAction());
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                mActionDownFirstX = event.getX();
                mActionDownFirstTime = mCurrentTime;

                Log.d(TAG, "onTouchEvent: ------> MotionEvent.ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: ------> MotionEvent.ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
//                L
//                if (mDoubleMoveIndex >= 2) {
//                    float moveX = Math.abs(getBetweenX(event));
//
//                    if (moveX != mDoubleMoveX) {
//                        mDoubleMoveX = moveX;
//                        float tmp = mDoubleMoveX - mDoubleFirstBet; /*计算出缩放的距离*/
//                        Log.d(TAG, "onTouchEvent: -------->" + "双指操控！！！" + tmp + "------" + mDoubleFirstBet);
//                        scale(tmp / 100.0f);
//
//                    }
//
//                } else {
                float moveX = event.getX();
                float tmpBetX = moveX - mActionDownFirstX;
                //Android按住不动的时候也会回调，会导致View抖动，不像IOS，虽然是回调了，但是可以通过计算，防止抖动
                if (tmpBetX != mTmpMoveX) {
                    mTmpMoveX = tmpBetX;
                    Log.d(TAG, "onTouchEvent: ------------->" + Math.round(mTmpMoveX * mPxTime));
                    long tmpTime = mActionDownFirstTime - Math.round(mTmpMoveX * mPxTime) * 1000;
                    mCurrentTime = tmpTime;
                    invalidate();
                    if (mOnTimeBarMoveListener != null) {
                        mOnTimeBarMoveListener.onTimeBarMove(mCurrentTime);
                    }
                }
//                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                mDoubleMoveIndex += 1;
////                isActionPointerDown = true;
//                mDoubleFirstBet = Math.abs(getBetweenX(event));
//                Log.d(TAG, "onTouchEvent: ------> MotionEvent.双指操控!!!" + mDoubleFirstBet);

                break;
            case MotionEvent.ACTION_POINTER_UP:
                isActionPointerDown = false;
                Log.d(TAG, "onTouchEvent: ------> MotionEvent.ACTION_POINTER_UP");
                break;
        }

        return true;
    }

    private void scale(float value) {

        /*范围限制*/
        if (value > 0) {
            if (mHouWidth > mViewWidth) {
                return;
            }
        }

        if (value < 0) {
            if (mHouWidth < mViewWidth / 10) {
                return;
            }
        }

        mHouWidth = mHouWidth + value;
        mMinWidth = mHouWidth / 60f;
        mSecWidth = mMinWidth / 60f;

        mPxTime = 1 / mSecWidth;


        invalidate();
    }

    /**
     * @param event
     * @return
     */
    private float getBetweenX(MotionEvent event) {
        return event.getX(0) - event.getX(1);
    }

    /**
     * 设置当前时间
     *
     * @param time
     */
    public void setCurrentTime(long time) {
        mCurrentTime = time;
        invalidate();
    }


    private OnTimeBarMoveListener mOnTimeBarMoveListener;

    public void setOnTimeBarMoveListener(OnTimeBarMoveListener listener) {
        this.mOnTimeBarMoveListener = listener;
    }

    public OnTimeBarMoveListener getOnTimeBarMoveListener() {
        return mOnTimeBarMoveListener;
    }

    /**
     * 时间轴滑动回调
     */
    public interface OnTimeBarMoveListener {
        void onTimeBarMove(long time);
    }










    /*计算工具方法-------------------*/

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

    public static int optInt(TypedArray typedArray, int index, int def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getInt(index, def);
    }


}
