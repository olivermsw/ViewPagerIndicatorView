package me.olimsw.viewpagerindicatorviewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by musiwen on 2016/11/25.
 */

public class ViewPagerIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private int radius = 60;
    private int padding = 60;
    private int count;
    private int selectedPosition;
    private int selectingPosition;
    private int lastSelectedPosition;
    private int unselectedColor;
    private int selectedColor;
    private Paint unselectedPaint = new Paint();

    public ViewPagerIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public ViewPagerIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    public ViewPagerIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs 属性集合
     */
    private void init(AttributeSet attrs) {
        unselectedColor = Color.BLACK;
        selectedColor = Color.CYAN;
        unselectedPaint.setColor(unselectedColor);
        unselectedPaint.setAntiAlias(true);
        unselectedPaint.setStyle(Paint.Style.FILL);
        unselectedPaint.setStrokeWidth(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int circleDiameter = radius * 2;
        int desiredHeight = circleDiameter;
        int desiredWidth = 0;
        if (count != 0) {
            desiredWidth = circleDiameter * count + (padding * (count - 1));
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            drawCircle(canvas, i, x, y);
        }
    }

    /**
     * 画圆
     *
     * @param canvas   画布
     * @param position 位置
     * @param x        x坐标
     * @param y        y坐标
     */
    private void drawCircle(Canvas canvas, int position, int x, int y) {
        boolean selectedItem = (position == selectedPosition || position == lastSelectedPosition);
        boolean selectingItem = (position == selectingPosition || position == selectedPosition);
        boolean isSelectedItem = selectedItem | selectingItem;

        if (isSelectedItem) {
             unselectedPaint.setColor(selectedColor);
            canvas.drawCircle(x, y, radius, unselectedPaint);
        } else {
            unselectedPaint.setColor(unselectedColor);
            canvas.drawCircle(x, y, radius, unselectedPaint);
        }
    }

    /**
     * 获得相应位置圆心x坐标
     *
     * @param position
     * @return
     */
    private int getCenterOfCircleX(int position) {
        return position * padding + position * radius * 2 + radius;
    }

    /**
     * 设置ViewPager
     *
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(this);
        count = viewPager.getAdapter().getCount();
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        log("position" + position + "||positionOffset" + positionOffset + "||positionOffsetPixels" + positionOffsetPixels);
        if(positionOffset>0&&positionOffset<1) {
            selectingPosition = position;
        }
    }

    @Override
    public void onPageSelected(int position) {
        log("position" + position);
        lastSelectedPosition = position;
        selectedPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void log(String log) {
        Log.e("msw", log);
    }

}
