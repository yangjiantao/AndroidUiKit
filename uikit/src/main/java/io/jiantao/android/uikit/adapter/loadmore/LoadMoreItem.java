package io.jiantao.android.uikit.adapter.loadmore;

import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jiantao on 2017/6/29.
 */

public class LoadMoreItem {

    public static final int STATE_LOADING = 0;
    //加载完成
    public static final int STATE_COMPLETED = 1;
    public static final int STATE_FAILED = 2;
    //隐藏
    public static final int STATE_HIDE = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_COMPLETED, STATE_FAILED, STATE_HIDE, STATE_LOADING})
    public @interface ItemState {
    }

    private int mTips;

    private int mState;

    public LoadMoreItem(@StringRes int mTips) {
        this.mTips = mTips;
        //默认隐藏
        this.mState = STATE_LOADING;
    }

    public int getTips() {
        return mTips;
    }

    public void setTips(@StringRes int mTips) {
        this.mTips = mTips;
    }

    @ItemState
    public int getState() {
        return mState;
    }

    public void setState(@ItemState int mState) {
        this.mState = mState;
    }
}
