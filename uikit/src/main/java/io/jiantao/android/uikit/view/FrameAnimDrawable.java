package io.jiantao.android.uikit.view;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 帧动画Drawable。避免内存泄漏。
 * Created by jiantao on 2017/7/30.
 */

public class FrameAnimDrawable extends Drawable implements Animatable {

    private static final int DEFAULT_FPS = 25;
    private int fps = DEFAULT_FPS;
    private final Paint mPaint;
    private final
    @DrawableRes
    int[] RES_IDS;
    private int resIndex;

    private final Resources mResources;

    private ValueAnimator mAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimUpdateListener;

    //取第一帧，用于获取图片宽高
    private Drawable mFirstDrawable;

    public FrameAnimDrawable(@NonNull int[] RES_IDS, @NonNull Resources resources) {
        this(DEFAULT_FPS, RES_IDS, resources);
    }

    public FrameAnimDrawable(int fps, @NonNull int[] RES_IDS, @NonNull Resources resources) {
        this.fps = fps;
        this.RES_IDS = RES_IDS;
        this.mResources = resources;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        if (RES_IDS.length <= 0) {
            throw new RuntimeException(" FrameAnimDrawable RES_IDS can not empty !!!");
        }
        mFirstDrawable = resources.getDrawable(RES_IDS[0]);
        createAnimator();
    }

    private void createAnimator() {
        mAnimator = ValueAnimator.ofInt(RES_IDS.length - 1);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setDuration(RES_IDS.length / fps * 1000);

        mAnimUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate(((int) animation.getAnimatedValue()));
//                System.out.println("FrameAnimDrawable onAnimationUpdate resIndex "+resIndex);
            }
        };
    }

    /**
     * 重绘。亦可供外部调用。
     * @param index 帧索引
     */
    public void invalidate(int index) {
        this.resIndex = index;
        invalidateSelf();
    }

    /**
     * @return 帧数量
     */
    public int getFrameCount(){
        return RES_IDS.length;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mResources != null) {
            BitmapDrawable drawable = (BitmapDrawable) mResources.getDrawable(RES_IDS[resIndex % RES_IDS.length]);
            Bitmap bitmap = drawable.getBitmap();
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
            System.out.println("FrameAnimDrawable  draw resIndex " + resIndex
                    + " ;\n bitmap =" + bitmap + " canvas.width" + canvas.getWidth()
                    + " height" + canvas.getHeight()
                    + "w" + drawable.getIntrinsicWidth() + "h" + drawable.getIntrinsicHeight());
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void start() {
        // If the animators has not ended, do nothing.
        if (mAnimator.isStarted()) {
            return;
        }
        startAnimator();
        invalidateSelf();
    }

    private void startAnimator() {
        mAnimator.addUpdateListener(mAnimUpdateListener);
        mAnimator.start();
    }

    @Override
    public void stop() {
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.removeAllUpdateListeners();
            mAnimator.end();
        }
    }

    @Override
    public boolean isRunning() {
        return mAnimator.isRunning();
    }

    @Override
    public int getIntrinsicWidth() {
        return mFirstDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mFirstDrawable.getIntrinsicHeight();
    }
}
