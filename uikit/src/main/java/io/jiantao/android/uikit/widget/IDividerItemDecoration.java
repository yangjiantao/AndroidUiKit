package io.jiantao.android.uikit.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import io.jiantao.android.uikit.util.DimenUtil;

/**
 * 支持divider height 、color 、padding
 * Created by jiantao on 2017/6/23.
 */

public class IDividerItemDecoration extends RecyclerView.ItemDecoration{
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };

    private final GradientDrawable mDivider;

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
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public IDividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = new GradientDrawable();
        //默认divider 1dp
        mVerticalDividerHeight = DimenUtil.dp2px(context, 1);
        mHorizontalDividerWidth = DimenUtil.dp2px(context, 1);
        mDividerColor = Color.parseColor("lightgrey");
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    public void setVerticalDividerHeight(int verticalDividerHeight) {
        this.mVerticalDividerHeight = verticalDividerHeight;
    }

    public void setHorizontalDividerWidth(int horizontalDividerWidth) {
        this.mHorizontalDividerWidth = horizontalDividerWidth;
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
    }

    public void setDividerPadding(int dividerPadding) {
        this.mDividerPadding = dividerPadding;
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
        if(mDividerPadding > 0){
            left = left + mDividerPadding;
            right = right - mDividerPadding;
        }

        mDivider.setColor(mDividerColor);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            int top = bottom - mDivider.getIntrinsicHeight();
            if(mVerticalDividerHeight > 0){
                top = bottom - mVerticalDividerHeight;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
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

        if(mDividerPadding > 0){
            top = top + mDividerPadding;
            bottom = bottom - mDividerPadding;
        }
        mDivider.setColor(mDividerColor);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
            int left = right - mDivider.getIntrinsicWidth();
            if(mHorizontalDividerWidth > 0){
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
            outRect.set(0, 0, 0, mVerticalDividerHeight > 0 ? mVerticalDividerHeight : mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mHorizontalDividerWidth > 0 ? mHorizontalDividerWidth : mDivider.getIntrinsicWidth(), 0);
        }
    }

}
