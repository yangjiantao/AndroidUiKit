package io.jiantao.android.uikit.util;

import android.util.Log;

import io.jiantao.android.uikit.BuildConfig;

/**
 * description
 *
 * @author Created by jiantaoyang
 * @date 2019-06-17
 */
public class LogUtils {

    public static void d(String tag, String onDismiss) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, onDismiss);
        }
    }

    public static void e(String tag, String onDismiss) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, onDismiss);
        }
    }
}
