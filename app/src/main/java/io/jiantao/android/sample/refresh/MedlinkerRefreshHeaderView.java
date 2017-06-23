package io.jiantao.android.sample.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.jiantao.android.sample.R;
import io.jiantao.android.uikit.refresh.IRefreshTrigger;

/**
 * 医联下拉刷新效果
 * 执行帧动画，动画周期间隙，有空白情况出现。解决方案：固定LoadingView的宽高。
 * Created by jiantao on 2017/6/20.
 */

public class MedlinkerRefreshHeaderView extends FrameLayout implements IRefreshTrigger{

    private ImageView loadingView;
    private TextView refreshState;
    //资源文件的下标
    private int resIndex;
    //资源文件的前缀名称
    private String resStartName;
    //资源文件的文件夹，mimmap,drawable
    private String resFolder = "drawable";
    //播放的频率
    private final int fps = 20;

    private final int size;

    private ValueAnimator valueAnimator;

    public MedlinkerRefreshHeaderView(Context context) {
        this(context, null);
    }

    public MedlinkerRefreshHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedlinkerRefreshHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        size = 30;
        setupViews();
    }

    private void setupViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_medlinker_refresh_header, this);
        loadingView = (ImageView) view.findViewById(R.id.iv_loadingview);
        refreshState = (TextView) view.findViewById(R.id.tv_refresh_state);
    }

    public int getResIndex() {
        return resIndex;
    }

    public MedlinkerRefreshHeaderView setResStartName(String resStartName) {
        this.resStartName = resStartName;
        return this;
    }

    public MedlinkerRefreshHeaderView setResFolder(String resFolder) {
        this.resFolder = resFolder;
        return this;
    }

    @Keep
    public void setResIndex(int resIndex) {
        System.out.println(" setResIndex resIndex "+resIndex);
        this.resIndex = resIndex;
        if (TextUtils.isEmpty(resStartName)) {
            throw new RuntimeException("资源文件前缀名称不能为空");
        }
        loadingView.setImageResource(getResources().getIdentifier(resStartName + resIndex, resFolder, getContext().getPackageName()));
    }

    /**
     * 开始动画
     */
    private ValueAnimator getRepeateAnimatior() {
        ValueAnimator animator = getObjectAnimatior(size).setDuration((size / fps) * 1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setResIndex(((Integer) animation.getAnimatedValue()) + 5);
            }
        });
        return animator;
    }

    private ValueAnimator getObjectAnimatior(int resSize) {
        return ValueAnimator.ofInt( 0, resSize);
    }

    private void checkAnimator() {
        if(valueAnimator == null){
            valueAnimator = getRepeateAnimatior();
        }
    }
    @Override
    public void onPullDownState(float progress) {
        //查看素材 05 - 35 正好是一个完整动画
        setResIndex(Math.round(progress * size) + 5);
        refreshState.setText("下拉刷新");
    }

    @Override
    public void onRefreshing() {
        System.out.println("refreshing");
        setResIndex(35);
        checkAnimator();
        valueAnimator.start();
        refreshState.setText("刷新中");
    }

    @Override
    public void onReleaseToRefresh() {
        refreshState.setText("松开刷新");
        setResIndex(35);
    }

    @Override
    public void onComplete() {
        checkAnimator();
        valueAnimator.cancel();
        setResIndex(35);
        refreshState.setText("刷新完成");
    }

    @Override
    public void reset() {
        checkAnimator();
        valueAnimator.cancel();
    }
}
