package io.jiantao.android.sample.refresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.LineScaleIndicator;

import io.jiantao.android.uikit.refresh.IRefreshTrigger;
import io.jiantao.android.uikit.util.DimenUtil;

/**
 * Created by jiantao on 2017/7/24.
 */

public class AvLoadingRefreshView extends AVLoadingIndicatorView implements IRefreshTrigger {

    public AvLoadingRefreshView(Context context) {
        this(context, null);
    }

    public AvLoadingRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvLoadingRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LineScaleIndicator indicator = new LineScaleIndicator();
        setIndicator(indicator);
        setIndicatorColor(Color.BLUE);
        setLayoutParams(new ViewGroup.LayoutParams(DimenUtil.dp2px(context, 48), DimenUtil.dp2px(context, 48)));
    }

    @Override
    public void onPullDownState(float progress) {

    }

    @Override
    public void onRefreshing() {
        show();
    }

    @Override
    public void onReleaseToRefresh() {

    }

    @Override
    public void onComplete() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void init() {
//        smoothToHide();
//        setVisibility(INVISIBLE);
    }
}
