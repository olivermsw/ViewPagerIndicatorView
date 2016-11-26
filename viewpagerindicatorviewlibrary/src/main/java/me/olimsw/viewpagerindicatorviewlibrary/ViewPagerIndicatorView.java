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
    private int radius = 20;
    private int padding = 30;
    private int count;
    private int selectedPosition;
    private int pageSelectedPosition;
    private int selectingPosition;
    private int relationPosition;
    private int unselectedColor;
    private int selectedColor;
    private Paint unselectedPaint = new Paint();
    private Paint selectedPaint = new Paint();
    private float selectingProgress;
    private boolean isSlideToRightSide;
    private AnimationType animationType;
    private int translucenceInt = 127;

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
        selectedPaint.setColor(selectedColor);
        selectedPaint.setAntiAlias(true);
        selectedPaint.setStyle(Paint.Style.FILL);
        selectedPaint.setStrokeWidth(0);
        animationType = AnimationType.SLIDE;
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
        switch (animationType) {
            case STANDARD:
                onDrawStandardView(canvas);
                break;
            case SLIDE:
                onDrawSlideView(canvas);
                break;
            case COMPRESSSLIDE:
                onDrawCompressSlideView(canvas);
                break;
            case GRADIENTCOLOR:
                onDrawGradientColorView(canvas);
                break;
            case RECTSLIDE:
                onDrawRectSlideView(canvas);
                break;
            case SMALLRECTSLIDE:
                onDrawSmallRectSlideView(canvas);
                break;
            default:
                onDrawStandardView(canvas);
                break;
        }
    }

    /**
     * SMALLRECTSLIDE模式动画绘制,缩小圆角矩形滑动
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawSmallRectSlideView(Canvas canvas) {
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            canvas.drawCircle(x, y, radius, unselectedPaint);
        }
        int h;
        if (isSlideToRightSide) {
            h = (int) ((255 - translucenceInt) * selectingProgress);
        } else {
            h = (int) ((255 - translucenceInt) * (1 - selectingProgress));
        }

    }

    /**
     * RECTSLIDE模式动画绘制,圆角矩形滑动
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawRectSlideView(Canvas canvas) {
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            canvas.drawCircle(x, y, radius, unselectedPaint);
        }
        int h = y * 2;
        float relationX = getCenterOfCircleX(relationPosition) + selectingProgress * (radius * 2 + padding);
        int selectedCenterOfCircleX = getCenterOfCircleX(selectedPosition);
        if (selectingProgress != 0) {
            canvas.drawCircle(relationX, y, radius, selectedPaint);
            canvas.drawCircle(selectedCenterOfCircleX, y, radius, selectedPaint);
            canvas.drawRect(selectedCenterOfCircleX, h, relationX, 0, selectedPaint);
        } else {
            canvas.drawCircle(relationX, y, radius, selectedPaint);
        }
    }

    /**
     * GRADIENTCOLOR模式动画绘制,透明渐变
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawGradientColorView(Canvas canvas) {
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            int alphaInt;
            if (isSlideToRightSide) {
                alphaInt = (int) ((255 - translucenceInt) * selectingProgress);
            } else {
                alphaInt = (int) ((255 - translucenceInt) * (1 - selectingProgress));
            }
            if (i == selectedPosition) {
                if (selectedPosition != selectingPosition) {
                    unselectedPaint.setAlpha(255 - alphaInt);
                } else {
                    unselectedPaint.setAlpha(255);
                }
                canvas.drawCircle(x, y, radius, unselectedPaint);
            } else if (i == selectingPosition) {
                if (selectedPosition != selectingPosition) {
                    unselectedPaint.setAlpha(translucenceInt + alphaInt);
                } else {
                    unselectedPaint.setAlpha(127);
                }
                canvas.drawCircle(x, y, radius, unselectedPaint);
            } else {
                unselectedPaint.setAlpha(translucenceInt);
                canvas.drawCircle(x, y, radius, unselectedPaint);
            }
        }
    }

    /**
     * COMPRESSSLIDE模式动画绘制,缩放滑动
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawCompressSlideView(Canvas canvas) {
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            canvas.drawCircle(x, y, radius, unselectedPaint);
        }
        canvas.drawCircle(getCenterOfCircleX(relationPosition) + selectingProgress * (radius * 2 + padding), y, radius, selectedPaint);
    }

    /**
     * SLIDE模式动画绘制
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawSlideView(Canvas canvas) {
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            canvas.drawCircle(x, y, radius, unselectedPaint);
        }
        canvas.drawCircle(getCenterOfCircleX(relationPosition) + selectingProgress * (radius * 2 + padding), y, radius, selectedPaint);
    }

    /**
     * STANDARD模式动画绘制
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawStandardView(Canvas canvas) {
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            if (i == selectedPosition) {
                canvas.drawCircle(x, y, radius, selectedPaint);
            } else {
                canvas.drawCircle(x, y, radius, unselectedPaint);
            }
        }
    }


    /**
     * 计算当前位置信息
     *
     * @param position
     * @param positionOffset
     */
    private void onProgress(int position, float positionOffset) {
        relationPosition = position;
        boolean isRightOverScrolled = position > selectedPosition;
        boolean isLeftOverScrolled = position + 1 < selectedPosition;
//        log("selectingProgress[" + selectingProgress + "]position[" + position + "]selectedPosition[" + selectedPosition
//                + "]selectingPosition[" + selectingPosition
//                + "]isSlideToRightSide[" + isSlideToRightSide + "]");
        if (isLeftOverScrolled || isRightOverScrolled) {
            selectedPosition = position;
        }
        isSlideToRightSide = selectedPosition == position && positionOffset != 0;
        int selectingPosition;
        selectingProgress = positionOffset;
        if (isSlideToRightSide) {
            selectingPosition = position + 1;
        } else {
            selectingPosition = position;
        }
        if (selectingProgress > 1) {
            selectingProgress = 1;
        } else if (selectingProgress < 0) {
            selectingProgress = 0;
        }
        if (selectingProgress == 1) {
            selectedPosition = selectingPosition;
        }
        if (selectingPosition < 0) {
            selectingPosition = 0;
        } else if (selectingPosition > count - 1) {
            selectingPosition = count - 1;
        }
        this.selectingPosition = selectingPosition;
        invalidate();
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
     * @param viewPager 指示的ViewPager{@link ViewPager}
     */
    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(this);
        count = viewPager.getAdapter().getCount();
        requestLayout();
    }

    /**
     * 设置动画效果
     *
     * @param animationType 动画效果美枚举{@link AnimationType}
     */
    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onProgress(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        pageSelectedPosition = position;
        if (animationType == AnimationType.STANDARD) {
            selectedPosition = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            selectedPosition = pageSelectedPosition;
            if (animationType == AnimationType.STANDARD) {
                invalidate();
            }
        }
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        this.selectedPaint.setColor(selectedColor);
    }

    public void setUnselectedColor(int unselectedColor) {
        this.unselectedColor = unselectedColor;
        this.unselectedPaint.setColor(unselectedColor);
    }

    private void log(String log) {
        Log.e("msw", log);
    }

}
