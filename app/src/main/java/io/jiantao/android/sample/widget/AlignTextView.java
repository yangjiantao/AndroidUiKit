package io.jiantao.android.sample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 兼容vivo api22手机 TextView换行问题
 *
 * @author Created by jiantaoyang
 * @date 2019-08-20
 */
public class AlignTextView extends AppCompatTextView {

    private static final String DOT_SUFFIX = "...";

    private static final String TAG = AlignTextView.class.getSimpleName();

    private boolean needCheckDevice = true;
    private Layout mLayout;

    public AlignTextView(Context context) {
        super(context);
    }

    public AlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlignTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (needCheckDevice && isVivoApi22()) {

            int width = getMeasuredWidth();
            CharSequence charSequence = getText();
            Layout layout = createWorkingLayout(charSequence, getPaint(), width);
            float dotWidth = getPaint().measureText(DOT_SUFFIX);
            int lineCount = layout.getLineCount();
            Log.d(TAG, "onMeasure , lineCount : " + lineCount);
            // 多行且第一行未占满
            if (lineCount > 1 && Math.abs(width - layout.getLineWidth(0)) > dotWidth) {
                Log.d(TAG, "onMeasure , firstLineEndIndex : " + layout.getLineEnd(0));
                int index = layout.getLineEnd(1);
                while (true) {
                    index--;
                    charSequence = charSequence.subSequence(0, index);
                    charSequence = SpannableStringBuilder.valueOf(charSequence).append(DOT_SUFFIX);
                    layout = createWorkingLayout(charSequence, getPaint(), width);
                    if (layout.getLineCount() == 1 && (width - layout.getLineWidth(0) <= dotWidth || index <= 10)) {
                        mLayout = layout;
                        break;
                    }
                }
            }
            needCheckDevice = false;
        }
    }

    private boolean isVivoApi22() {
        final String brand = "vivo";
        return Build.BRAND.contains(brand) && Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLayout != null) {
            mLayout.draw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 创建一个可以自动换行的layout， 不显示，仅仅用于比较文字是否过长
     *
     * @param text 文案
     * @return Layout
     */
    private Layout createWorkingLayout(CharSequence text, TextPaint paint, int width) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return StaticLayout.Builder.obtain(text, 0, text.length(), paint, width).build();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new StaticLayout(text, getPaint(), width, Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), false);
        } else {
            return new StaticLayout(text, getPaint(), width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
    }
}
