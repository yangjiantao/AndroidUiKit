package io.jiantao.android.uikit.tablayout;

import android.support.annotation.DrawableRes;

public interface CommonTabAdapter {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}