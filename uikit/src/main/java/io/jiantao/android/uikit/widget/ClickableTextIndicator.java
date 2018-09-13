package io.jiantao.android.uikit.widget;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 可点击的文本指示器
 *
 * @author jiantao.yang
 * @time 2018/8/29
 */
public class ClickableTextIndicator extends HorizontalScrollView {
    private static final String TAG = "ClickableTextIndicator";
    static final int TEXT_COLOR = 0x4D000000;
    static final int TEXT_HIGHLIGHT_COLOR = 0xFF1B96FE;
    private LinearLayout contentView;
    private List<RouteEntity> routes;
    private OnIndicatorChangeListener changeListener;

    public ClickableTextIndicator(Context context) {
        this(context, null);
    }

    public ClickableTextIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickableTextIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        routes = new ArrayList<>();
        // add a LinearLayout in a horizontal orientation
        contentView = new LinearLayout(context);
        contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        addView(contentView);
    }

    public void addRoute(String parentId, String orgId, String orgName) {
        if (TextUtils.isEmpty(orgId) || TextUtils.isEmpty(orgName)) {
            Log.d(TAG, "addRoute params is empty orgId = " + orgId + "; name = " + orgName);
            return;
        }
        if (routes != null && !routes.isEmpty()) {
            final int size = routes.size();
            RouteEntity last = routes.get(size - 1);
            if (size == contentView.getChildCount() && TextUtils.equals(last.parentId, parentId)) {
                // 处理同级切换
                last.routeId = orgId;
                last.routeName = orgName;
                View child = contentView.getChildAt(size - 1);
                String name = " > ".concat(last.routeName);
                ((TextView) child).setText(name);
                child.setTag(last);
                notifyOnChanged(last, null);
                return;
            }
        }
        ClickableTextIndicator.RouteEntity entity = new ClickableTextIndicator.RouteEntity();
        entity.setRouteId(orgId);
        entity.setRouteName(orgName);
        entity.setParentId(parentId);
        RouteEntity[] array = new RouteEntity[1];
        array[0] = entity;
        addRoutes(array);
    }

    private void addRoutes(RouteEntity[] data) {
        if (data == null || data.length <= 0) {
            return;
        }
        changeLastOneColor(true);
        for (int i = 0; i < data.length; i++) {
            TextView textView = createTextView();
            RouteEntity entity = data[i];
            int size = routes.size();
            String name = size <= 0 ? entity.routeName : " > ".concat(entity.routeName);
            if (i == data.length - 1) {
                textView.setText(name);
            } else {
                int start = i <= 0 ? 0 : 1;
                int end = name.length();
                SpannableString spannableString = new SpannableString(name);
                spannableString.setSpan(new ForegroundColorSpan(TEXT_HIGHLIGHT_COLOR), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
            }
            textView.setTag(entity);
            this.routes.add(entity);
            contentView.addView(textView);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 200);

        if (routes != null && routes.size() > 0) {
            notifyOnChanged(routes.get(routes.size() - 1), null);
        }
    }

    public void setTextClickListener(OnIndicatorChangeListener listener) {
        this.changeListener = listener;
    }

    /**
     * 返回上一级
     */
    public void goBack() {
        if (canGoBack()) {
            int tail = routes.size() - 1;
            deleteRoute(routes.get(tail));
        }
    }

    /**
     * size > 1,返回到第一级时在返回就应该退出了。
     *
     * @return
     */
    public boolean canGoBack() {
        return this.routes != null && this.routes.size() > 1;
    }

    private void changeLastOneColor(boolean highlight) {
        int childCount = contentView.getChildCount();
        if (childCount <= 0) {
            return;
        }
        int index = childCount - 1;
        TextView child = ((TextView) contentView.getChildAt(index));
        CharSequence text = child.getText();
        if (highlight) {
            int start = childCount == 1 ? 0 : 1;
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(TEXT_HIGHLIGHT_COLOR), start, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            child.setText(spannableString);
        } else {
            String routeName = routes.get(index).routeName;
            String name = index <= 0 ? routeName : " > ".concat(routeName);
            child.setText(name);
        }
    }

    private TextView createTextView() {
        TextView view = new TextView(getContext());
        view.setTextSize(15);
        view.setTextColor(TEXT_COLOR);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteEntity tag = (RouteEntity) v.getTag();
                if (TextUtils.equals(tag.getRouteId(), routes.get(routes.size() - 1).routeId)) {
                    // 忽略最后一个
                    return;
                }
                RouteEntity[] routeEntities = updateSelf(tag);
                notifyOnChanged(tag, routeEntities);
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        view.setLayoutParams(params);
        return view;
    }

    /**
     * 返回被移除的节点
     *
     * @param entity
     * @return
     */
    private RouteEntity[] updateSelf(RouteEntity entity) {
        int childCount = contentView.getChildCount();
        Stack<RouteEntity> stack = new Stack<>();
        for (int i = childCount - 1; i >= 0; i--) {
            Object tag = contentView.getChildAt(i).getTag();
            RouteEntity temp = (RouteEntity) tag;
            if (TextUtils.equals(entity.getRouteId(), temp.getRouteId())) {
                break;
            }
            contentView.removeViewAt(i);
            stack.push(routes.remove(i));
        }
        changeLastOneColor(false);
        RouteEntity[] result = new RouteEntity[stack.size()];
        int i = 0;
        while (!stack.empty()) {
            result[i] = stack.pop();
            i++;
        }
        return result;
    }

    public void deleteRoute(RouteEntity entity) {
        if (entity == null) {
            return;
        }
        RouteEntity[] data = new RouteEntity[1];
        data[0] = entity;
        deleteRoutes(data);
    }

    public void deleteRoutes(RouteEntity[] data) {
        if (data == null || data.length <= 0) {
            return;
        }
        // 只需处理第一个
        int childCount = routes.size();
        for (int i = childCount - 1; i > 0; i--) {
            RouteEntity temp = routes.get(i);
            contentView.removeViewAt(i);
            routes.remove(i);
            if (TextUtils.equals(data[0].getRouteId(), temp.getRouteId())) {
                break;
            }
        }
        changeLastOneColor(false);
        if (routes != null && routes.size() > 0) {
            notifyOnChanged(routes.get(routes.size() - 1), null);
        }
    }

    private void notifyOnChanged(RouteEntity entity, RouteEntity[] removedRoutes) {
        if (changeListener != null) {
            changeListener.onChanged(entity, removedRoutes);
        }
    }

    public static class RouteEntity {
        private String parentId;
        private String routeId;
        private String routeName;

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public void setRouteName(String routeName) {
            this.routeName = routeName;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }

    public interface OnIndicatorChangeListener {
        /**
         * @param entity        当前指向（最后）的数据
         * @param removedRoutes 点击上层节点，返回删除的节点数。可能为null
         */
        void onChanged(RouteEntity entity, RouteEntity[] removedRoutes);
    }
}
