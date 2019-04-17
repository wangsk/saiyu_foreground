package com.saiyu.foreground.ui.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.saiyu.foreground.App;
import com.saiyu.foreground.R;
import com.saiyu.foreground.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarGraphView extends View {
    private Paint blueLightBg;
    private Paint whiteBg;
    private Paint yellowBg;
    private Paint mPaint;
    private Paint bg;

    private Paint dotPain;//圆点的画笔
    private Paint dotPain_2;//圆点边缘的画笔
    private Paint textBgPain;//字背景
    private Rect mBound;
    private float mHeight, mSize;//屏幕宽度高度、柱状图宽度
    //x轴的原点坐标
    private int xOri;
    //y轴的原点坐标
    private int yOri;
    //第一个点对应的最大Y坐标
    private float maxXInit;
    //第一个点对应的最小X坐标
    private float minXInit;
    //第一个柱形的最大Y坐标
    private float maxZxInit;
    //第一个柱形对应的最小X坐标
    private float minZxInit;
    private LinearGradient gradient;//背景渐变色

    private float linearStartX;//折线第一个点
    private float zhuStartX;//柱形第一个位置
    private Context context;
    //是否在ACTION_UP时，根据速度进行自滑动，没有要求，建议关闭，过于占用GPU
    private boolean isScroll = false;
    //是否正在滑动
    private boolean isScrolling = false;
    //点击的点对应的X轴的第几个点，默认1
    private int selectIndex = 1;
    //数据源
    //x轴坐标对应的数据
    private List<String> xValue = new ArrayList<>();
    //y轴坐标对应的数据
    private int yValue = 0;
    //折线对应的数据
    private Map<String, Integer> value = new HashMap<>();
    private Map<String, Integer> value_2 = new HashMap<>();

    private int posMonth = 7;//当前月份，今年若超过当前月份的则不画
    private int posPlace = 0;//一开始的位移 用于显示到最新的月份


    private boolean isMonthFirst = false;//用于位移时是否第一次改变数据
    private boolean isRectFirst = false;
    private boolean ischangeFirst = true;//防止onlayout两次改变数据
    private int valueSize = 7;//正常分割为7份

    //速度检测器
    private VelocityTracker velocityTracker;

    public BarGraphView(Context context) {
        this(context, null);

    }

    public BarGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyChartView, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.MyChartView_bargrapColor:
                    break;
                case R.styleable.MyChartView_selectRightColor:
                    break;
                case R.styleable.MyChartView_xyColor:
                    break;
                case R.styleable.MyChartView_textSize:
                    //xml的属性可以在这里设置
//                    textSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
//                            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MyChartView_textColor:
//                    textColor = array.getColor(attr, Color.YELLOW);
                    break;

            }
        }
        array.recycle();
        init();
    }


    /**
     * 初始化画笔
     */
    private void init() {
        bg = new Paint();
        bg.setAntiAlias(true);
        bg.setColor(context.getResources().getColor(R.color.transparent));
        bg.setStyle(Paint.Style.FILL);
        blueLightBg = new Paint();
        blueLightBg.setAntiAlias(true);
        blueLightBg.setColor(Color.parseColor("#10ffffff"));
        blueLightBg.setStyle(Paint.Style.FILL);
        whiteBg = new Paint();
        whiteBg.setAntiAlias(true);
        whiteBg.setColor(context.getResources().getColor(R.color.white));
        yellowBg = new Paint();
        yellowBg.setAntiAlias(true);
        yellowBg.setColor(context.getResources().getColor(R.color.yellow));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //柱状体de形状
        mBound = new Rect();

        //圆点的画笔
        dotPain = new Paint();
        dotPain.setAntiAlias(true);
        //圆点边缘的画笔
        dotPain_2 = new Paint();
        dotPain_2.setAntiAlias(true);
        //字背景
        textBgPain = new Paint();
        textBgPain.setAntiAlias(true);
        textBgPain.setColor(Color.parseColor("#98f0f0f0"));

    }

    /**
     * 数据初始化
     */
    private void initData() {


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize * 1 / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize * 1 / 2;
        }

        setMeasuredDimension(width, height);
    }

    //计算高度宽度
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (ischangeFirst) {
            ischangeFirst = false;
            mHeight = getHeight();
            mSize = getWidth() / valueSize;
            linearStartX = mSize / 2;
            xOri = 0;
            yOri = getHeight();
            minXInit = getWidth() - mSize * (valueSize - 1) - mSize / 2;//计算最小的长度
            minZxInit = getWidth() - mSize * (valueSize);
            maxXInit = linearStartX;
            zhuStartX = 0;
            maxZxInit = mSize;
            initData();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //渐变
//        gradient = new LinearGradient(0, 0, getWidth(), mHeight, context.getResources().getColor(R.color.color_7C98ff), context.getResources().getColor(R.color.color_77b0ff), Shader.TileMode.REPEAT);
//        mbackgroundPaint.setShader(gradient);
        canvas.drawRect(0, 0, getMeasuredWidth(), mHeight, bg);
        //画柱状图
        drawRect(canvas);
        //划地下月份
        drawDate(canvas);
        //画折线图和圆点
        drawLine(canvas);
        drawLine_2(canvas);
        LogUtils.print("linearStartX====" + linearStartX);
    }

    //划柱形Rect
    private void drawRect(Canvas canvas) {
        if (value == null || xValue == null || value.size() == 0 || xValue.size() == 0)
            return;
        int sizeCount = value.size();
        //新建图层
        if (isRectFirst) {
            isRectFirst = false;
            zhuStartX = zhuStartX - mSize * posPlace;
        }
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        for (int i = 0; i < sizeCount; i++) {
            //画柱状图
            float rectX = zhuStartX + (mSize) * (i);
            RectF rectF = new RectF();
            rectF.left = rectX;
            rectF.right = rectX + mSize;
            rectF.bottom = mHeight;
            rectF.top = mHeight - 390;
            if (i % 2 != 0) {
                canvas.drawRect(rectF, bg);
            } else {
                canvas.drawRect(rectF, blueLightBg);
            }
        }
        //保存图层
        canvas.restoreToCount(layerId);
    }

    //画日期
    private void drawDate(Canvas canvas) {
        if (value == null || xValue == null || value.size() == 0 || xValue.size() == 0)
            return;

        mPaint.setTextAlign(Paint.Align.CENTER);
        int sizeCount = value.size();
        if (isMonthFirst) {
            isMonthFirst = false;
            linearStartX = linearStartX - mSize * posPlace;
        }
        for (int i = 0; i < sizeCount; i++) {
            float tx = linearStartX + (mSize) * i;
            String text = xValue.get(i);
            mPaint.getTextBounds(text, 0, text.length(), mBound);
            if (i == selectIndex - 1) {
                mPaint.setTextSize(35);
                mPaint.setColor(context.getResources().getColor(R.color.white));
            } else {
                mPaint.setTextSize(32);
                mPaint.setColor(context.getResources().getColor(R.color.grey_white));
            }
            canvas.drawText(text, tx, mHeight - 40, mPaint);
        }
    }

    /**
     * 画白色折线图和圆点
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        if (value == null || xValue == null || value.size() == 0 || xValue.size() == 0)
            return;
        int sizeCount = value.size();
        whiteBg.setColor(context.getResources().getColor(R.color.white));
        whiteBg.setStrokeWidth(3);
        whiteBg.setAntiAlias(true);
        whiteBg.setStyle(Paint.Style.STROKE);
        dotPain.setColor(context.getResources().getColor(R.color.white));
        dotPain.setAntiAlias(true);
        dotPain_2.setColor(Color.parseColor("#80ffffff"));
        dotPain_2.setAntiAlias(true);
        //划折线
        Path path = new Path();
        Path bcpath = new Path();
        float x = linearStartX + (mSize) * 0;
        float y = yOri - yOri * value.get(xValue.get(0)) / yValue;
        path.moveTo(x, y);
        bcpath.moveTo(x, mHeight);
        bcpath.lineTo(x, y);
        int count = 0;//记录第几月份好用于计算出x的值
        boolean isSub = false;
        for (int i = 1; i < sizeCount; i++) {
            y = yOri - yOri * value.get(xValue.get(i)) / yValue;
            x = linearStartX + (mSize) * i;
            if (i + 1 <= posMonth) {
                path.lineTo(x, y);
                bcpath.lineTo(x, y);
                count = i;
                isSub = true;
            } else
                break;
        }
        if (isSub) {
            x = linearStartX + (mSize) * count;
            bcpath.lineTo(x, mHeight);
        } else {
            bcpath.lineTo(x, mHeight);
        }
        bcpath.close();
        canvas.drawPath(path, whiteBg);

        //划圆点
        for (int i = 0; i < sizeCount; i++) {
            float ry = yOri - yOri * value.get(xValue.get(i)) / yValue;
            float rx = linearStartX + (mSize) * i;
            if (i + 1 <= posMonth) {
                if (posMonth == 0 && value.get(xValue.get(i)) <= 0)
                    break;
                canvas.drawCircle(rx, ry, 6, dotPain);
                if (i == selectIndex - 1) {
                    canvas.drawCircle(rx, ry, 8, dotPain);
                    canvas.drawCircle(rx, ry, 22, dotPain_2);
                }
            }
        }
    }

    /**
     * 画折线图和圆点
     *
     * @param canvas
     */
    private void drawLine_2(Canvas canvas) {
        if (value_2 == null || xValue == null || value_2.size() == 0 || xValue.size() == 0)
            return;
        int sizeCount = value_2.size();
        yellowBg.setColor(context.getResources().getColor(R.color.yellow));
        yellowBg.setStrokeWidth(3);
        yellowBg.setAntiAlias(true);
        yellowBg.setStyle(Paint.Style.STROKE);
        dotPain.setColor(context.getResources().getColor(R.color.yellow));
        dotPain.setAntiAlias(true);
        dotPain_2.setColor(Color.parseColor("#80ffa800"));
        dotPain_2.setAntiAlias(true);
        //划折线
        Path path = new Path();
        Path bcpath = new Path();
        float x = linearStartX + (mSize) * 0;
        float y = yOri - yOri * value_2.get(xValue.get(0)) / yValue;
        path.moveTo(x, y);
        bcpath.moveTo(x, mHeight);
        bcpath.lineTo(x, y);
        int count = 0;//记录第几月份好用于计算出x的值
        boolean isSub = false;
        for (int i = 1; i < sizeCount; i++) {
            y = yOri - yOri * value_2.get(xValue.get(i)) / yValue;
            x = linearStartX + (mSize) * i;
            if (i + 1 <= posMonth) {
                path.lineTo(x, y);
                bcpath.lineTo(x, y);
                count = i;
                isSub = true;
            } else
                break;
        }
        if (isSub) {
            x = linearStartX + (mSize) * count;
            bcpath.lineTo(x, mHeight);
        } else {
            bcpath.lineTo(x, mHeight);
        }
        bcpath.close();
        canvas.drawPath(path, yellowBg);

        //划圆点
        for (int i = 0; i < sizeCount; i++) {
            float ry = yOri - yOri * value_2.get(xValue.get(i)) / yValue;
            float rx = linearStartX + (mSize) * i;
            if (i + 1 <= posMonth) {
                if (posMonth == 0 && value_2.get(xValue.get(i)) <= 0)
                    break;
                canvas.drawCircle(rx, ry, 6, dotPain);
                if (i == selectIndex - 1) {
                    canvas.drawCircle(rx, ry, 8, dotPain);
                    canvas.drawCircle(rx, ry, 22, dotPain_2);
                    String text = value.get(xValue.get(i)) + "";
                    String text_2 = value_2.get(xValue.get(i)) + "";
                    String text_3 = "最高折扣: "+text_2 + "\n平均折扣: " + text;
                    drawFloatTextBox(canvas, rx, ry, text_3, i);
                }
            }
        }
    }
    /**
     * 绘制显示Y值的浮动框
     *
     * @param canvas
     * @param x
     * @param y
     * @param text
     * @param pos    第几个
     */
    private void drawFloatTextBox(Canvas canvas, float x, float y, String text,int pos) {
        //p1
        Path path = new Path();
        float left = x - 150;
        float top = y - 150;
        float right = x + 150;
        float bottom = y - 20;
        if (pos == 0) {
            left = x - mSize / 2;
            right = 150 * 2 + x - mSize / 2;
        }
        if (pos == value.size() - 1) {
            left = x + mSize / 2 - 150 * 2;
            right = x + mSize / 2;
        }
        RectF oval3 = new RectF(left, top, right, bottom);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 7, 7, textBgPain);//第二个参数是x半径，第三个参数是y半径
        path.moveTo(x, y);
        path.lineTo(x - 20, y - 20);
        path.lineTo(x + 20, y - 20);
        path.close();
        canvas.drawPath(path, textBgPain);//绘制背景的下角

        //绘制三角形
        Path path1 = new Path();
        path1.moveTo(x, mHeight - 20);
        path1.lineTo(x - 20, mHeight);
        path1.lineTo(x + 20, mHeight);
        path1.close();
        mPaint.setColor(context.getResources().getColor(R.color.white));
        canvas.drawPath(path1, mPaint);

        mPaint.setTextSize(40);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(context.getResources().getColor(R.color.blue));//要写在draw里面不然画不出来
        mPaint.getTextBounds(text, 0, text.length(), mBound);
        float y_1 = y - 95;
        if (pos == value.size() - 1) {
            float x_1 = x - 70;
            String[] arg = text.split("\n");
            for(int i = 0; i < arg.length; i++){
                if(i == 0){
                    dotPain.setColor(context.getResources().getColor(R.color.yellow));
                } else if(i == 1){
                    dotPain.setColor(context.getResources().getColor(R.color.white));
                }
                canvas.drawCircle(x_1 - 130, y_1 - 15, 6, dotPain);
                canvas.drawText(arg[i], x_1, y_1, mPaint);
                y_1 += mPaint.descent() - mPaint.ascent();
            }
        } else if (pos == 0) {
            float x_1 = x + 80;
            String[] arg = text.split("\n");
            for(int i = 0; i < arg.length; i++){
                if(i == 0){
                    dotPain.setColor(context.getResources().getColor(R.color.yellow));
                } else if(i == 1){
                    dotPain.setColor(context.getResources().getColor(R.color.white));
                }
                canvas.drawCircle(x_1 - 130, y_1 - 15, 6, dotPain);
                canvas.drawText(arg[i], x_1, y_1, mPaint);
                y_1 += mPaint.descent() - mPaint.ascent();
            }
        } else {
            float x_1 = x;
            String[] arg = text.split("\n");
            for(int i = 0; i < arg.length; i++){
                if(i == 0){
                    dotPain.setColor(context.getResources().getColor(R.color.yellow));
                } else if(i == 1){
                    dotPain.setColor(context.getResources().getColor(R.color.white));
                }
                canvas.drawCircle(x_1 - 130, y_1 - 15, 6, dotPain);
                canvas.drawText(arg[i], x_1, y_1, mPaint);
                y_1 += mPaint.descent() - mPaint.ascent();
            }
        }
    }

    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isScrolling)
            return super.onTouchEvent(event);
        this.getParent().requestDisallowInterceptTouchEvent(true);//当该view获得点击事件，就请求父控件不拦截事件
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float offX = event.getX() - lastX;
                lastX = event.getX();
                if (linearStartX + offX < minXInit) {
                    linearStartX = minXInit;
                    zhuStartX = minZxInit;
                } else if (linearStartX + offX > maxXInit) {
                    linearStartX = maxXInit;
                    zhuStartX = 0;
                } else {
                    linearStartX = linearStartX + offX;
                    zhuStartX += offX;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);//当该view获得点击事件，就请求父控件不拦截事件
                clickAction(event);
                scrollAfterActionUp();
                recycleVelocityTracker();
                break;

        }
        return true;
    }

    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     * @return
     */
    private Rect getTextBounds(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * 点击X轴坐标或者折线节点
     *
     * @param event
     */
    private void clickAction(MotionEvent event) {
        if (value == null || xValue == null || value.size() == 0 || xValue.size() == 0)
            return;
        int dp25 = 80;
        float eventX = event.getX();
        float eventY = event.getY();
        for (int i = 0; i < xValue.size(); i++) {
            //节点
            float x = linearStartX + mSize * i;
//            float y = yOri - yOri * value.get(xValue.get(i)) / yValue;
            if (eventX >= x - dp25 && eventX <= x + dp25 && selectIndex != i + 1) {//每个节点所在的柱状图内都是可点击区域
                if (i + 1 <= posMonth) {
                    selectIndex = i + 1;
                    invalidate();
                }
                return;
            }
        }
//        for (int i = 0; i < xValue.size(); i++) {
//            //节点
//            float x = linearStartX + mSize * i;
//            float y = yOri - yOri * value_2.get(xValue.get(i)) / yValue;
//            if (eventX >= x - dp25 && eventX <= x + dp25 &&
//                    eventY >= y - dp25 && eventY <= y + dp25 && selectIndex != i + 1) {//每个节点周围8dp都是可点击区域
//                if (i + 1 <= posMonth) {
//                    selectIndex = i + 1;
//                    invalidate();
//                }
//                return;
//            }
//        }
    }

    /**
     * dp转化成为px
     *
     * @param dp
     * @return
     */
//    private int dpToPx(int dp) {
//        float density = getContext().getResources().getDisplayMetrics().density;
//        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
//    }

    /**
     * 获取速度跟踪器
     *
     * @param event
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (!isScroll)
            return;
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    /**
     * 回收速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 获取速度
     *
     * @return
     */
    private float getVelocity() {
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000);
            return velocityTracker.getXVelocity();
        }
        return 0;
    }

    /**
     * 手指抬起后的滑动处理
     */
    private void scrollAfterActionUp() {
        if (!isScroll)
            return;
        final float velocity = getVelocity();
        float scrollLength = maxXInit - minXInit;
        if (Math.abs(velocity) < 10000)//10000是一个速度临界值，如果速度达到10000，最大可以滑动(maxXInit - minXInit)
            scrollLength = (maxXInit - minXInit) * Math.abs(velocity) / 10000;
        ValueAnimator animator = ValueAnimator.ofFloat(0, scrollLength);
        animator.setDuration((long) (scrollLength / (maxXInit - minXInit) * 1000));//时间最大为1000毫秒，此处使用比例进行换算
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                if (velocity < 0 && linearStartX > minXInit) {//向左滑动
                    if (linearStartX - value <= minXInit)
                        linearStartX = minXInit;
                    else
                        linearStartX = linearStartX - value;
                } else if (velocity > 0 && linearStartX < maxXInit) {//向右滑动
                    if (linearStartX + value >= maxXInit)
                        linearStartX = maxXInit;
                    else
                        linearStartX = linearStartX + value;
                }
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isScrolling = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isScrolling = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isScrolling = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();

    }

    /**
     * 设置初始值
     *
     * @param value
     * @param xValue
     * @param yValue 纵坐标的最大值
     */
    public void setValue(Map<String, Integer> value,Map<String, Integer> value_2, List<String> xValue, int yValue) {
        this.value = value;
        this.value_2 = value_2;
        this.xValue = xValue;
        this.yValue = yValue;
        invalidate();
    }

    /**
     * 设置当前日期
     *
     * @param posMonth
     */
    public void setCurrentMonth(int posMonth) {
        this.posMonth = posMonth;
        selectIndex = posMonth;
        setCurrentDisplace(0);
        invalidate();

    }

    /**
     * 设置一开始位移
     *
     * @param posPlace
     */
    private void setCurrentDisplace(int posPlace) {
        this.posPlace = posPlace;
        isRectFirst = true;
        isMonthFirst = true;
    }

}
