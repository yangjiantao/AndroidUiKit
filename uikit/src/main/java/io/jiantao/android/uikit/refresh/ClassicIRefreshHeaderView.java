package io.jiantao.android.uikit.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.jiantao.android.uikit.R;


public class ClassicIRefreshHeaderView extends FrameLayout implements IRefreshTrigger {
    private ImageView ivArrow;

    private ImageView ivSuccess;

    private TextView tvRefresh;

    private ProgressBar progressBar;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public ClassicIRefreshHeaderView(Context context) {
        this(context, null);
    }

    public ClassicIRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicIRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_irecyclerview_classic_refresh_header_view, this);

        tvRefresh = (TextView) findViewById(R.id.tvRefresh);

        ivArrow = (ImageView) findViewById(R.id.ivArrow);

        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);

        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onPullDownState(float progress) {
        ivArrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(GONE);
        if (progress < 1.0f) {
            if (rotated) {
                ivArrow.clearAnimation();
                ivArrow.startAnimation(rotateDown);
                rotated = false;
            }

            tvRefresh.setText("SWIPE TO REFRESH");
        }else{
            onReleaseToRefresh();
        }
    }

    @Override
    public void onRefreshing() {
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText("REFRESHING");
    }

    @Override
    public void onReleaseToRefresh() {
        ivArrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(GONE);
        tvRefresh.setText("RELEASE TO REFRESH");
        if (!rotated) {
            ivArrow.clearAnimation();
            ivArrow.startAnimation(rotateUp);
            rotated = true;
        }
    }

    @Override
    public void onComplete() {
        rotated = false;
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText("COMPLETE");
    }

    @Override
    public void init() {
        rotated = false;
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }
}
