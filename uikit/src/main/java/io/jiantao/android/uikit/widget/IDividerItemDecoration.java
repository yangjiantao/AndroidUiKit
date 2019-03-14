package io.jiantao.android.uikit.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import io.jiantao.android.uikit.util.DimenUtil;

/**
 * features:
 * 1. 支持divider height 、color 、padding 等熟悉设置
 * 2. 自定义Divider Drawable
 * 3. divider绘制位置。call method {@link #setOffsetMode(int)}
 * Created by jiantao on 2017/6/23.
 */
public class IDividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;


    /**
     * divider 在itemView中的绘制位置。
     */
    public static final int OFFSET_MODE_TOP = 2;
    public static final int OFFSET_MODE_LEFT = 3;

    private final GradientDrawable mDivider;

    /**
     * custom divider
     */
    private Drawable mCustomDivider;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private final Rect mBounds = new Rect();

    /**
     * 竖直方向divider高度
     */
    private int mVerticalDividerHeight;

    private int mHorizontalDividerWidth;

    private int mDividerColor;

    private int mDividerPadding;

    /**
     * divider 绘制位置。 默认水平方向绘制在itemView右边，竖直方向绘制在itemView底部
     */
    private int mOffsetMode = -1;

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public IDividerItemDecoration(Context context, int orientation) {
        mDivider = new GradientDrawable();
        //默认divider 1dp
        mVerticalDividerHeight = DimenUtil.dp2px(context, 1);
        mHorizontalDividerWidth = DimenUtil.dp2px(context, 1);
        mDividerColor = Color.parseColor("lightgrey");
        setOrientation(orientation);
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public IDividerItemDecoration setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
        return this;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public IDividerItemDecoration setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mCustomDivider = drawable;
        return this;
    }

    /**
     * @param verticalDividerHeight the vertical height in pixels
     * @return
     */
    public IDividerItemDecoration setVerticalDividerHeight(@Px int verticalDividerHeight) {
        this.mVerticalDividerHeight = verticalDividerHeight;
        return this;
    }

    /**
     * @param horizontalDividerWidth the horizontal width in pixels
     * @return
     */
    public IDividerItemDecoration setHorizontalDividerWidth(@Px int horizontalDividerWidth) {
        this.mHorizontalDividerWidth = horizontalDividerWidth;
        return this;
    }

    public IDividerItemDecoration setDividerColor(@ColorInt int dividerColor) {
        this.mDividerColor = dividerColor;
        return this;
    }

    /**
     * @param dividerPadding the divider padding in pixels
     * @return
     */
    public IDividerItemDecoration setDividerPadding(@Px int dividerPadding) {
        this.mDividerPadding = dividerPadding;
        return this;
    }

    /**
     * set divider's position
     *
     * @param offsetMode {@link #OFFSET_MODE_LEFT} or {@link #OFFSET_MODE_TOP}
     */
    public IDividerItemDecoration setOffsetMode(int offsetMode) {
        this.mOffsetMode = offsetMode;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        boolean topMode = mOffsetMode == OFFSET_MODE_TOP;
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }
        // use customDivider drawable
        if (mCustomDivider != null) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                int bottom, top;
                if (topMode) {
                    top = mBounds.top + Math.round(child.getTranslationY());
                    bottom = top + mCustomDivider.getIntrinsicHeight();
                } else {
                    bottom = mBounds.bottom + Math.round(child.getTranslationY());
                    top = bottom - mCustomDivider.getIntrinsicHeight();
                }
                mCustomDivider.setBounds(left, top, right, bottom);
                mCustomDivider.draw(canvas);
            }
            canvas.restore();
            return;
        }

        if (mDividerPadding > 0) {//设置了padding，调整left和right的值
            left = left + mDividerPadding;
            right = right - mDividerPadding;
        }

        mDivider.setColor(mDividerColor);//自定义color，没设置就用默认值
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            int bottom, top;
            if (topMode) {
                top = mBounds.top + Math.round(child.getTranslationY());
                bottom = top + mVerticalDividerHeight;
            } else {
                bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                top = bottom - mVerticalDividerHeight;
            }

            mDivider.setBounds(left, top, right, bottom);
            //mDivider.setCornerRadius();//设置矩形圆角
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        boolean leftMode = mOffsetMode == OFFSET_MODE_LEFT;
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        // use customDivider drawable
        if (mCustomDivider != null) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
                int right, left;
                if (leftMode) {
                    left = mBounds.left + Math.round(child.getTranslationX());
                    right = left + mCustomDivider.getIntrinsicWidth();
                } else {
                    right = mBounds.right + Math.round(child.getTranslationX());
                    left = right - mCustomDivider.getIntrinsicWidth();
                }
                mCustomDivider.setBounds(left, top, right, bottom);
                mCustomDivider.draw(canvas);
            }
            canvas.restore();
            return;
        }

        if (mDividerPadding > 0) {
            top = top + mDividerPadding;
            bottom = bottom - mDividerPadding;
        }
        mDivider.setColor(mDividerColor);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            int right, left;
            if (leftMode) {
                left = mBounds.left + Math.round(child.getTranslationX());
                right = left + mHorizontalDividerWidth;
            } else {
                right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
                left = right - mHorizontalDividerWidth;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            int height = mVerticalDividerHeight > 0 ? mVerticalDividerHeight : mDivider.getIntrinsicHeight();
            int top = mOffsetMode == OFFSET_MODE_TOP ? height : 0;
            int bottom = mOffsetMode == OFFSET_MODE_TOP ? 0 : height;
            outRect.set(0, top, 0, bottom);
        } else {
            int width = mHorizontalDividerWidth > 0 ? mHorizontalDividerWidth : mDivider.getIntrinsicWidth();
            int left = mOffsetMode == OFFSET_MODE_LEFT ? width : 0;
            int right = mOffsetMode == OFFSET_MODE_LEFT ? 0 : width;
            outRect.set(left, 0, right, 0);
        }
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
