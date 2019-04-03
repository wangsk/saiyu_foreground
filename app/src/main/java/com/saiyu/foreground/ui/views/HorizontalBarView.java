package com.saiyu.foreground.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.saiyu.foreground.utils.LogUtils;

import java.util.ArrayList;

/**
 * autour : lbing
 * date : 2018/9/5 11:27
 * className :
 * version : 1.0
 * description :
 */


public class HorizontalBarView extends View {

    private ArrayList<HoBarEntity> hoBarEntityList = new ArrayList<>();
    private float barStartX = 0f;//
    private Paint mbarPaint;
    private Paint mCountPaint;
    private float barHeight = dp2px(15); //bar的高度
    private float barInterval = dp2px(5);//bar之间的距离
    private int mbarPaintColor = Color.parseColor("#fda33c");//bar的颜色
    private int mCountPaintColor = Color.parseColor("#5e5e5e");//最右边文字的颜色
    private int countTextSize = 30;//最右边文字的大小
    private float topAndBottomInterval = dp2px(5);//上下边距的距离（上下线与bar的距离）
    private float rightInterval = dp2px(10);//最右侧文字和右边距离
    private int defaltHeight = 0;
    private int maxCount = 100;
    private float countMaxWidth = 0;
    private float countTextHeight = 0;

    public HorizontalBarView(Context context) {
        this(context, null);
    }

    public HorizontalBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    public float getBarHeight() {
        return barHeight;
    }

    public float getBarInterval() {
        return barInterval;
    }

    public int getMbarPaintColor() {
        return mbarPaintColor;
    }

    public int getmCountPaintColor() {
        return mCountPaintColor;
    }

    public int getCountTextSize() {
        return countTextSize;
    }

    public float getTopAndBottomInterval() {
        return topAndBottomInterval;
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void initPaints() {

        mbarPaint = new Paint();
        mbarPaint.setAntiAlias(true);
        mbarPaint.setStyle(Paint.Style.FILL);
        mbarPaint.setColor(Color.parseColor("#fda33c"));
        mbarPaint.setStrokeCap(Paint.Cap.ROUND);

        mCountPaint = new Paint();
        mCountPaint.setAntiAlias(true);
        mCountPaint.setStyle(Paint.Style.FILL);
        mCountPaint.setTextSize(countTextSize);
        mCountPaint.setColor(mCountPaintColor);
        mCountPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getScreenWidth();
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //计算高度
            computeHeightAndTextMaxWidth();
            height = defaltHeight;
        }
        setMeasuredDimension(width, height);
    }

    //计算高度
    private void computeHeightAndTextMaxWidth() {
        if (hoBarEntityList.size() == 0) {
            defaltHeight = getPaddingTop() + getPaddingBottom();
        } else {
            defaltHeight = (int) ((int) (getPaddingTop() + getPaddingBottom() + barHeight * hoBarEntityList.size() + (hoBarEntityList.size() - 1) * barInterval) + 2 * topAndBottomInterval);
            for (int i = 0; i < hoBarEntityList.size(); i++) {
                if (countTextHeight <= 0) {
                    Rect rect = new Rect();
                    mCountPaint.getTextBounds(hoBarEntityList.get(i).count + "", 0, (hoBarEntityList.get(i).count + "").length(), rect);
                    countTextHeight = rect.width();
                }
                if (hoBarEntityList.get(i).count > maxCount) {
                    maxCount = hoBarEntityList.get(i).count;
                }
            }
            countMaxWidth = mCountPaint.measureText(maxCount + "");
            barStartX = getPaddingLeft();
            LogUtils.print("    countMaxWidth===" + countMaxWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算最长的bar的长度
        float maxBarWidth = getMeasuredWidth() - getPaddingRight() - barStartX - countMaxWidth  - rightInterval;
        float perBarWidth = 0;
        if (maxCount > 0) {
            perBarWidth = maxBarWidth / maxCount;
        }

        //绘制bar
        float barStartY = getPaddingTop() + topAndBottomInterval;
        float barEndX = 0;
        float barEndY = 0;

        for (int i = 0; i < hoBarEntityList.size(); i++) {
            barEndX = barStartX + perBarWidth * hoBarEntityList.get(i).count;
            barEndY = barStartY + barHeight;
            LogUtils.print("barStartX-barEndX===" + (barStartX - barEndX));
            if(i == 0){
                mbarPaint.setColor(Color.parseColor("#fda33c"));
            } else {
                mbarPaint.setColor(Color.parseColor("#148cf1"));
            }
            //绘制bar
            canvas.drawRect(barStartX, barStartY, barEndX, barEndY, mbarPaint);

            //绘制count
            Paint.FontMetricsInt fontMetrics = mCountPaint.getFontMetricsInt();
            float countBaseLine = barStartY + barHeight / 2 + -(fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(" "+hoBarEntityList.get(i).count, barEndX, countBaseLine, mCountPaint);

            barStartY = barEndY + barInterval;
        }


    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


    public void setHoBarData(ArrayList<HoBarEntity> list) {
        hoBarEntityList.clear();
        hoBarEntityList.addAll(list);
        requestLayout();
        invalidate();
    }

    public static class HoBarEntity {
        public String content;
        public int count;
    }

}


