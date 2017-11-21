package io.jiantao.android.uikit.photoviewer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import io.jiantao.android.uikit.util.DimenUtil;

/**
 * 上下拖拽删除
 *
 * @author jiantao
 * @date 2017/11/16
 */

class DragPhotoView extends SubsamplingScaleImageView {
    private static final String TAG = DragPhotoView.class.getSimpleName();
    private static final boolean DEBUG = true;

    /**
     * 图片第一次显示后缩放比例
     */
    private float firstDisplayScale;

    /**
     * 背景画笔
     */
    private Paint bgPaint;
    private int bgAlpha;
    private final int minBgAlpha = 50;
    private float bitmapScale;
    private final float minBitmapScale = 0.5f;
    private float downX, downY, translateX, translateY;
    private final int touchSlop;
    /**
     * 上下拖动最大距离
     */
    private final int maxTranslateY;
    private boolean isDragging;
    private boolean canDrag;
    /**
     * 正在动画
     */
    private ValueAnimator valueAnimator;

    private OnPhotoViewActionListener dismissListener;
    private OnLongClickListener longClickListener;

    public DragPhotoView(Context context, AttributeSet attr) {
        super(context, attr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        maxTranslateY = DimenUtil.dp2px(context, 100);
        bgAlpha = 255;
        bitmapScale = 1;
        init(context);
    }

    public DragPhotoView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        setDebug(DEBUG);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isReady()) {
                    if (firstDisplayScale <= 0 && getScale() > 0) {
                        firstDisplayScale = getScale();
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if(DEBUG){
                            Log.d(TAG, "onGlobalLayout final getScale = " + getScale() + " firstDisplayScale = " + firstDisplayScale);
                        }
                    }
                }
            }

        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dismissListener != null){
                    dismissListener.onClick(v);
                }
            }
        });

        super.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean consume = !isDragging && dismissListener != null && dismissListener.onLongClick(v);
                canDrag = !consume;
                return consume;
            }
        });

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //如果正在动画，拦截不处理。
                if (isAnimating()) {
                    return true;
                }
                //判断图片是否为初始状态（第一次进入后显示的大小）
                if (isReady() && (firstDisplayScale == getScale())) {
                    final int action = event.getAction();
                    switch (action & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            if (DEBUG) {
                                Log.d(TAG, "action_down  = " + firstDisplayScale + ", getScale = " + getScale() + " isFirstEnterState = " + (firstDisplayScale == getScale()));
                            }
                            downX = event.getX();
                            downY = event.getY();
                            canDrag = true;
                            break;

                        case MotionEvent.ACTION_MOVE:

                            if (canDrag) {

                                final float dy = Math.abs(downY - event.getY());
                                if (firstDisplayScale == getScale() && dy > touchSlop) {
                                    isDragging = true;
                                    translateX = event.getX() - downX;
                                    translateY = event.getY() - downY;

                                    float percent = dy / maxTranslateY;
                                    if (percent > 1.0f) {
                                        percent = 1.0f;
                                    }
                                    bgAlpha = (int) (255 * (1 - percent));
                                    bgAlpha = bgAlpha < minBgAlpha ? minBgAlpha : bgAlpha;
                                    //尽量将图片缩放比例设置小点
                                    final float p = dy / getHeight();
                                    bitmapScale = (1 - p);
                                    bitmapScale = bitmapScale < minBitmapScale ? minBitmapScale : bitmapScale;
                                    if(DEBUG) {
                                        Log.d(TAG, "action_move translateX = " + translateX + "; translateY = " + translateY + "; pointerCount = " + event.getPointerCount());
                                    }
                                    invalidate();
                                }
                            }
                            break;

                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            if(DEBUG) {
                                Log.d(TAG, "action = " + action + "; pointerCount = " + event.getPointerCount());
                            }
                            if (isDragging) {
                                //最后一个手指离开屏幕时,检查y轴方向拖拽距离是否超过阀值，若超过则回调dismiss接口
                                if (Math.abs(translateY) >= maxTranslateY && dismissListener != null) {
                                    dismissListener.onDismiss();
                                }else{
                                    //若为超过阀值，则指定动画回到初始位置。
                                    restoreFirstEnterState();
                                }
                                isDragging = false;
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            if(DEBUG){
                                Log.d(TAG, "action pointer down or up= " + action + "; pointerCount = " + event.getPointerCount());
                            }
                            //防止拖拽过程中多点触摸导致事件错乱。
                            canDrag = isDragging;
                            break;

                        default:

                            break;
                    }
                }
                return isDragging;
            }
        });
    }

    void setDismissListener(OnPhotoViewActionListener listener){
        this.dismissListener = listener;
    }

    private void restoreFirstEnterState() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimator.setDuration(150);
        }
        final int tempAlpha = bgAlpha;
        final float tempTranslateX = translateX;
        final float tempTranslateY = translateY;
        final float tempScale = bitmapScale;

        final int offsetAlpha = 255 - bgAlpha;
        final float offsetX = 0 - translateX;
        final float offsetY = 0 - translateY;
        final float offsetScale = 1.0f - bitmapScale;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float) animation.getAnimatedValue();
                bgAlpha = (int) (tempAlpha + offsetAlpha * value);
                translateX = tempTranslateX + offsetX * value;
                translateY = tempTranslateY + offsetY * value;
                bitmapScale = tempScale + offsetScale * value;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private boolean isAnimating() {
        return valueAnimator != null && valueAnimator.isRunning();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //肯定touch move事件，不断更新以下几个变量的值来达到动画效果。
        createPaint();
        bgPaint.setAlpha(bgAlpha);
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
        canvas.translate(translateX, translateY);
        canvas.scale(bitmapScale, bitmapScale, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        if(DEBUG){
            Log.e(TAG, "please use setOnPhotoViewActionListener !!!");
        }
    }

    private void createPaint() {
        if (bgPaint == null) {
            bgPaint = new Paint();
            bgPaint.setColor(Color.BLACK);
        }
    }

    public interface OnPhotoViewActionListener extends OnClickListener, OnLongClickListener{

        /**
         * page dismiss listener
         */
        void onDismiss();
    }
}
