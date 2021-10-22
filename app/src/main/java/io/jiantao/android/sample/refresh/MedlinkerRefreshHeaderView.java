package io.jiantao.android.sample.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import io.jiantao.android.sample.R;
import io.jiantao.android.uikit.refresh.IRefreshTrigger;
import io.jiantao.android.uikit.view.FrameAnimDrawable;

/**
 * 医联下拉刷新效果
 * Created by jiantao on 2017/6/20.
 */

public class MedlinkerRefreshHeaderView extends FrameLayout implements IRefreshTrigger {

    private ImageView loadingView;
    private TextView refreshState;

    FrameAnimDrawable drawable;

    public MedlinkerRefreshHeaderView(Context context) {
        this(context, null);
    }

    public MedlinkerRefreshHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedlinkerRefreshHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews();
    }
    private void setupViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_medlinker_refresh_header, this);
        loadingView = (ImageView) view.findViewById(R.id.iv_loadingview);
        refreshState = (TextView) view.findViewById(R.id.tv_refresh_state);

        int[] RES_IDS = new int[]{R.mipmap.loading_00000, R.mipmap.loading_00001, R.mipmap.loading_00002, R.mipmap.loading_00003, R.mipmap.loading_00004,
                R.mipmap.loading_00005, R.mipmap.loading_00006, R.mipmap.loading_00007, R.mipmap.loading_00008, R.mipmap.loading_00009,
                R.mipmap.loading_00010, R.mipmap.loading_00011, R.mipmap.loading_00012, R.mipmap.loading_00013, R.mipmap.loading_00014,
                R.mipmap.loading_00015, R.mipmap.loading_00016, R.mipmap.loading_00017, R.mipmap.loading_00018, R.mipmap.loading_00019,
                R.mipmap.loading_00020, R.mipmap.loading_00021, R.mipmap.loading_00022, R.mipmap.loading_00023, R.mipmap.loading_00024,
                R.mipmap.loading_00025, R.mipmap.loading_00026, R.mipmap.loading_00027, R.mipmap.loading_00028, R.mipmap.loading_00029,
                R.mipmap.loading_00030, R.mipmap.loading_00031, R.mipmap.loading_00032, R.mipmap.loading_00033, R.mipmap.loading_00034,
                R.mipmap.loading_00035, R.mipmap.loading_00036, R.mipmap.loading_00037, R.mipmap.loading_00038, R.mipmap.loading_00039,
        };

        drawable = new FrameAnimDrawable(RES_IDS, getResources());
        loadingView.setImageDrawable(drawable);
    }

    @Override
    public void onPullDownState(float progress) {
//        cancelAnim();
//        setResIndex(Math.round(progress * size));
        refreshState.setText("下拉刷新");
    }

    @Override
    public void onRefreshing() {
        System.out.println("refreshing");
        drawable.start();
//        startAnim();
        refreshState.setText("刷新中");
    }

    @Override
    public void onReleaseToRefresh() {
//        startAnim();
        refreshState.setText("松开刷新");
    }

    @Override
    public void onComplete() {
        drawable.stop();
//        cancelAnim();
        refreshState.setText("刷新完成");
    }

    @Override
    public void init() {
        drawable.stop();
//        checkAnimator();
//        valueAnimator.cancel();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        drawable.stop();
    }
}
