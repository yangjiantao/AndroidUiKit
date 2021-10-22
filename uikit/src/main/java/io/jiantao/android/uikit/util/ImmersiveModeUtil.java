package io.jiantao.android.uikit.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.jiantao.android.uikit.R;

/**
 * statusbar 相关方法、全屏切换
 *
 * @author jiantao
 * @date 2018/6/7
 */
public class ImmersiveModeUtil {

    private static final int DEFAULT_STATUS_BAR_ALPHA = 112;
    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.immersiveutil_fake_status_bar_view;

    private static int mPreviousSystemUiVisibility;
    /**
     * 0: not support, 1：darkMode, 2: lightMode
     */
    private static int currentStatusBarStyle;

    // 判断MIUI系统
    private static boolean sIsMiuiV6;

    static {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            sIsMiuiV6 = "V6".equals((String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 全屏Frag
     */
    public static final int FULL_SCREEN_VISIBILITY =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    /**
     * 布局全屏会占用statusbar区域，通常配合设置状态栏背景为透明色。
     */
    public static final int LAYOUT_STABLE_AND_FULLSCREEN_VISIBILITY =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    /**
     * 请求全屏，隐藏状态和和导航栏。使用场景：视频播放
     *
     * @param activity
     */
    public static void requestFullScreen(@NonNull Activity activity) {
        setSystemUiVisibility(activity, FULL_SCREEN_VISIBILITY);
    }

    /**
     * 退出全屏，进入全屏前，缓存systemui状态
     *
     * @param activity
     */
    public static void quitFullScreen(@NonNull Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        systemUiVisibility &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        systemUiVisibility &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * @param activity
     * @param systemUiVisibility
     */
    public static void setSystemUiVisibility(@NonNull Activity activity, int systemUiVisibility) {
        View decorView = activity.getWindow().getDecorView();
        mPreviousSystemUiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 恢复
     *
     * @param activity
     */
    public static void restorePreviousSystemUiVisibility(@NonNull Activity activity) {
        if (mPreviousSystemUiVisibility != 0) {
            activity.getWindow().getDecorView().setSystemUiVisibility(mPreviousSystemUiVisibility);
        } else {
            setDefaultImmersiveMode(activity);
        }
    }

    /**
     * App默认的沉浸式默认
     *
     * @param activity
     */
    public static void setDefaultImmersiveMode(@NonNull Activity activity) {
        final boolean darkMode = setStatusBarDarkMode(activity, true);
        if (darkMode) {
            setStatusBarBgColor(activity, Color.WHITE);
        }
    }

    /**
     * 设置状态栏背景透明且layoutFullscreen
     *
     * @param activity
     */
    public static void setStatusBarTransparent(@NonNull Activity activity) {
        final Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            final int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(systemUiVisibility | LAYOUT_STABLE_AND_FULLSCREEN_VISIBILITY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setStatusBarBgColor(@NonNull Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }
                fakeStatusBarView.setBackgroundColor(color);
            } else {
                decorView.addView(createStatusBarView(activity, color));
            }
            setRootView(activity);
        }
    }

    /**
     * 先判断主流系统，然后MIUI系统，最后Flyme
     *
     * @param activity
     * @param isFontColorDark 深色字体(Light)模式
     * @return 是否设置成功
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean setStatusBarDarkMode(@NonNull Activity activity, boolean isFontColorDark) {
        boolean result = setCommonStatusBarDarkMode(activity, isFontColorDark);
        if (!result) {
            result = setMIUIStatusBarDarkIcon(activity, isFontColorDark);

            if (!result) {
                result = setMeizuStatusBarDarkIcon(activity, isFontColorDark);
            }
        }
        if (result) {
            currentStatusBarStyle = isFontColorDark ? 2 : 1;
        }
        return result;
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 已在style中配置
     * 官方及主流rom
     *
     * @param activity
     * @param darkmode
     */
    public static boolean setCommonStatusBarDarkMode(@NonNull Activity activity, boolean isFontColorDark) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (isFontColorDark) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(ui);
            // api>=23 默认成功
            result = true;
        }
        return result;
    }

    /**
     * set status bar darkmode
     *
     * @param isFontColorDark
     * @param activity
     */
    public static boolean setMIUIStatusBarDarkIcon(@NonNull Activity activity, boolean isFontColorDark) {
        boolean result = false;
        if (sIsMiuiV6) {
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), isFontColorDark ? darkModeFlag : 0, darkModeFlag);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private static boolean setMeizuStatusBarDarkIcon(@NonNull Activity activity, boolean isFontColorDark) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (isFontColorDark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * @return 1:不支持，0:支持
     */
    public static int canSetStatusBarStyle() {
        int status = 1;
        // 成功设置过，或者API>=23
        if (currentStatusBarStyle != 0 || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            status = 0;
        }
        return status;
    }

    /**
     * Return the status bar's height.
     *
     * @return the status bar's height
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
