package io.jiantao.android.uikit.tablayout;

import androidx.annotation.DrawableRes;

public interface CommonTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}