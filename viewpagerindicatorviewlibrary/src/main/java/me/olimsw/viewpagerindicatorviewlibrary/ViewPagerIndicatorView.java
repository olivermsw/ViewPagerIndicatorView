package me.olimsw.viewpagerindicatorviewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by musiwen on 2016/11/25.
 */

public class ViewPagerIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private int defaultWidth;
    private int radius = 30;
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
    private AnimationMode animationMode;
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
        animationMode = AnimationMode.SLIDE;
        defaultWidth = dp2px(2);
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
        switch (animationMode) {
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
            case ZOOM:
                onDrawZoomView(canvas);
                break;
            case DRAG:
                onDrawDragView(canvas);
                break;
            default:
                onDrawStandardView(canvas);
                break;
        }
    }

    /**
     * DRAG模式动画绘制,拖拽
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawDragView(Canvas canvas) {

        int y = getHeight() / 2;
        unselectedPaint.setAlpha(translucenceInt);
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            canvas.drawCircle(x, y, radius, unselectedPaint);
        }
        Path path = new Path();
        path.moveTo(radius, 0);
        path.cubicTo(radius, 0, 2 * radius + padding / 2, getHeight(), 3 * radius + padding, 0);
        path.lineTo(3 * radius + padding, getHeight());
        path.cubicTo(3 * radius + padding, getHeight(), 2 * radius + padding / 2, 0, radius, getHeight());
        path.lineTo(radius, 0);
//        unselectedPaint.setStyle(Paint.Style.STROKE);
//        unselectedPaint.setStrokeWidth(3);
        canvas.drawPath(path, unselectedPaint);
    }

    /**
     * ZOOM模式动画绘制,缩放
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawZoomView(Canvas canvas) {
        int y = getHeight() / 2;
        int radius = this.radius * 2 / 3;
        unselectedPaint.setAlpha(translucenceInt);
        for (int i = 0; i < count; i++) {
            if (i != selectingPosition && i != selectedPosition) {
                int x = getCenterOfCircleX(i);
                canvas.drawCircle(x, y, radius, unselectedPaint);
            }
        }
        int alphaInt;
        float offsetWidth;
        if (isSlideToRightSide) {
            offsetWidth = selectingProgress;
            alphaInt = (int) ((255 - translucenceInt) * offsetWidth);
        } else {
            offsetWidth = 1 - selectingProgress;
            alphaInt = (int) ((255 - translucenceInt) * offsetWidth);
        }
        if (selectedPosition == selectingPosition) {
            unselectedPaint.setAlpha(255);
            canvas.drawCircle(getCenterOfCircleX(selectedPosition), y, this.radius, unselectedPaint);
        } else {
            float selectedRadius = radius + (this.radius - radius) * (1 - offsetWidth);
            unselectedPaint.setAlpha(255 - alphaInt);
            canvas.drawCircle(getCenterOfCircleX(selectedPosition), y, selectedRadius, unselectedPaint);
            float selectingRadius = radius + (this.radius - radius) * offsetWidth;
            unselectedPaint.setAlpha(translucenceInt + alphaInt);
            canvas.drawCircle(getCenterOfCircleX(selectingPosition), y, selectingRadius, unselectedPaint);
        }
    }

    /**
     * SMALLRECTSLIDE模式动画绘制,缩小圆角矩形滑动
     *
     * @param canvas 画布{@link Canvas}
     */
    private void onDrawSmallRectSlideView(Canvas canvas) {
        int radius = this.radius * 2 / 3;
        int offestRadius = this.radius / 3;
        int y = getHeight() / 2;
        for (int i = 0; i < count; i++) {
            int x = getCenterOfCircleX(i);
            canvas.drawCircle(x, y, this.radius, unselectedPaint);
        }
        int h = y * 2;
        int selectedCenterOfCircleX = getCenterOfCircleX(selectedPosition);
        if (isSlideToRightSide) {
            if (selectingProgress > 0.8f) {
                int offset = (int) ((1 - (1 - selectingProgress) / 0.2f) * (h + padding));
                selectedCenterOfCircleX += offset;
                radius = (int) (radius + offestRadius * (1 - (1 - selectingProgress) / 0.2f));
            } else if (selectingProgress < 0.2) {
                radius = (int) (radius + offestRadius * (1 - selectingProgress / 0.2f));
            }
        } else {
            if (selectingProgress < 0.2f) {
                int offset = (int) ((1 - selectingProgress / 0.2f) * (h + padding));
                selectedCenterOfCircleX -= offset;
                radius = (int) (radius + offestRadius * (1 - selectingProgress / 0.2f));
            } else if (selectingProgress > 0.8f) {
                radius = (int) (radius + offestRadius * (1 - (1 - selectingProgress) / 0.2f));
            }
        }
        float relationX = getCenterOfCircleX(relationPosition) + selectingProgress * (radius * 2 + padding);
        if (selectingProgress != 0) {
            canvas.drawCircle(relationX, y, radius, selectedPaint);
            canvas.drawCircle(selectedCenterOfCircleX, y, radius, selectedPaint);
            if (isSlideToRightSide) {
                canvas.drawRect(selectedCenterOfCircleX, this.radius - radius, relationX, h - (this.radius - radius), selectedPaint);
            } else {
                canvas.drawRect(relationX, this.radius - radius, selectedCenterOfCircleX, h - (this.radius - radius), selectedPaint);
            }
        } else {
            canvas.drawCircle(relationX, y, radius, selectedPaint);
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
        if (isSlideToRightSide) {
            if (selectingProgress > 0.8f) {
                int offset = (int) ((1 - (1 - selectingProgress) / 0.2f) * (h + padding));
                selectedCenterOfCircleX += offset;
            }
        } else {
            if (selectingProgress < 0.2f) {
                int offset = (int) ((1 - selectingProgress / 0.2f) * (h + padding));
                selectedCenterOfCircleX -= offset;
            }
        }
        if (selectingProgress != 0) {
            canvas.drawCircle(relationX, y, radius, selectedPaint);
            canvas.drawCircle(selectedCenterOfCircleX, y, radius, selectedPaint);
            if (isSlideToRightSide) {
                canvas.drawRect(selectedCenterOfCircleX, 0, relationX, h, selectedPaint);
            } else {
                canvas.drawRect(relationX, 0, selectedCenterOfCircleX, h, selectedPaint);
            }
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
        unselectedPaint.setStyle(Paint.Style.STROKE);
        unselectedPaint.setStrokeWidth(defaultWidth);
        for (int i = 0; i < count; i++) {
            if (i != selectingPosition && i != selectedPosition) {
                int x = getCenterOfCircleX(i);
                canvas.drawCircle(x, y, radius - defaultWidth / 2, unselectedPaint);
            }
        }
        float offsetWidth;
        if (isSlideToRightSide) {
            offsetWidth = selectingProgress;
        } else {
            offsetWidth = 1 - selectingProgress;
        }
        if (selectedPosition == selectingPosition) {
            unselectedPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getCenterOfCircleX(selectedPosition), y, radius, unselectedPaint);
        } else {
            unselectedPaint.setStyle(Paint.Style.STROKE);
            float selectedOffsetWidth = radius * (1 - offsetWidth);
            if (defaultWidth >= selectedOffsetWidth) {
                selectedOffsetWidth = defaultWidth;
            }
            unselectedPaint.setStrokeWidth(selectedOffsetWidth);
            canvas.drawCircle(getCenterOfCircleX(selectedPosition), y, radius - selectedOffsetWidth / 2, unselectedPaint);
            float selectingOffsetWidth = radius * offsetWidth;
            if (defaultWidth >= selectingOffsetWidth) {
                selectingOffsetWidth = defaultWidth;
            }
            unselectedPaint.setStrokeWidth(selectingOffsetWidth);
            canvas.drawCircle(getCenterOfCircleX(selectingPosition), y, radius - selectingOffsetWidth / 2, unselectedPaint);
        }
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
     * @param position       位置
     * @param positionOffset 偏移量（0~1f）
     */
    private void onProgress(int position, float positionOffset) {
        relationPosition = position;
        boolean isRightOverScrolled = position > selectedPosition;
        boolean isLeftOverScrolled = position + 1 < selectedPosition;
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
     * @param position 位置
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
     * @param animationMode 动画效果美枚举{@link AnimationMode}
     */
    public void setAnimationMode(AnimationMode animationMode) {
        this.animationMode = animationMode;
        switch (animationMode) {
            case COMPRESSSLIDE:
                unselectedPaint.setStyle(Paint.Style.STROKE);
                break;
            default:
                unselectedPaint.setStyle(Paint.Style.FILL);
                break;
        }
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onProgress(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        pageSelectedPosition = position;
        if (animationMode == AnimationMode.STANDARD) {
            selectedPosition = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            selectedPosition = pageSelectedPosition;
            if (animationMode == AnimationMode.STANDARD) {
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

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public int dp2px(float dpValue) {
        final float scale = this.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void log(String log) {
        Log.e("msw", log);
    }

}
