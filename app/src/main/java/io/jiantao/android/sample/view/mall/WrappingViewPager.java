package io.jiantao.android.sample.view.mall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import androidx.viewpager.widget.ViewPager;

/**
 * ViewPager with dynamic height support. For basic usage, replace {@link ViewPager} with {@link WrappingViewPager}
 * in your layout file, and set its height property to {@code wrap_content}.
 *
 * @author Santeri Elo
 * @author Abhishek V (http://stackoverflow.com/a/32410274)
 * @author Vihaan Verma (http://stackoverflow.com/a/32488566)
 * @since 14-06-2016
 */
public class WrappingViewPager extends ViewPager implements Animation.AnimationListener {
    private View mCurrentView;
    private PagerAnimation mAnimation = new PagerAnimation();
    private boolean mAnimStarted = false;
    private long mAnimDuration = 100;

    public WrappingViewPager(Context context) {
        super(context);
        mAnimation.setAnimationListener(this);
    }

    public WrappingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimation.setAnimationListener(this);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!mAnimStarted && mCurrentView != null) {
            int height;
            mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = mCurrentView.getMeasuredHeight();

            if (height < getMinimumHeight()) {
                height = getMinimumHeight();
            }

            int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            // 比较新的pager视图和之前的视图高度差，然后做动画。
            // 目前这种方式的动画会导致外层列表滑动，可考虑监控滑动手势。
            if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight) {
                mAnimation.setDimensions(height, getLayoutParams().height);
                mAnimation.setDuration(mAnimDuration);
                startAnimation(mAnimation);
                mAnimStarted = true;
            } else {
                heightMeasureSpec = newHeight;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * This method should be called when the ViewPager changes to another page. For best results
     * call this method in the adapter's setPrimary
     *
     * @param currentView PagerAdapter item view
     */
    public void onPageChanged(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration Duration in ms
     */
    public void setAnimationDuration(long duration) {
        mAnimDuration = duration;
    }

    /**
     * Sets the interpolator used by the animation.
     *
     * @param interpolator {@link Interpolator}
     */
    public void setAnimationInterpolator(Interpolator interpolator) {
        mAnimation.setInterpolator(interpolator);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mAnimStarted = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mAnimStarted = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    /**
     * Custom animation to animate the change of height in the {@link WrappingViewPager}.
     */
    private class PagerAnimation extends Animation {
        private int targetHeight;
        private int currentHeight;
        private int heightChange;

        /**
         * Set the dimensions for the animation.
         *
         * @param targetHeight  View's target height
         * @param currentHeight View's current height
         */
        void setDimensions(int targetHeight, int currentHeight) {
            this.targetHeight = targetHeight;
            this.currentHeight = currentHeight;
            this.heightChange = targetHeight - currentHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (interpolatedTime >= 1) {
                getLayoutParams().height = targetHeight;
            } else {
                int stepHeight = (int) (heightChange * interpolatedTime);
                getLayoutParams().height = currentHeight + stepHeight;
            }
            requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}