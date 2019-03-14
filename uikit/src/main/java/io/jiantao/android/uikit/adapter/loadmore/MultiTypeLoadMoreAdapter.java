package io.jiantao.android.uikit.adapter.loadmore;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import io.jiantao.android.uikit.R;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.multitype.MultiTypePool;
import me.drakeet.multitype.TypePool;

import java.util.Collections;
import java.util.List;

/**
 * 设计目标：1. 便于扩展loadMoreItemView 2. 代码易读、简洁 3. 满足设计模式原则
 * 分页加载Adapter
 * @author jiantao
 */

public class MultiTypeLoadMoreAdapter extends MultiTypeAdapter implements LoadMoreDelegate.LoadMoreSubject {
    private static final String TAG = MultiTypeLoadMoreAdapter.class.getSimpleName();
    /**
     * loadmore view类型值，对应Adapter中getitemviewtype，由于初始化时首先rigiter了loadmoreitem.class，所以对应type为0.
     */
    final int LOAD_MORE_ITEM_TYPE = 0;

    final LoadMoreItem loadMoreItem;
    final LoadMoreItemViewBinder loadMoreItemViewBinder;
    final LoadMoreDelegate loadMoreDelegate;
    private LoadMoreDelegate.LoadMoreSubject loadMoreSubject;

    public MultiTypeLoadMoreAdapter() {
        this(Collections.emptyList());
    }

    public MultiTypeLoadMoreAdapter(@NonNull List<?> items) {
        this(items, 16);
    }

    public MultiTypeLoadMoreAdapter(@NonNull List<?> items, int initialCapacity) {
        this(items, new MultiTypePool(initialCapacity));
    }

    public MultiTypeLoadMoreAdapter(@NonNull List<?> items, @NonNull TypePool pool) {
        super(items, pool);
        loadMoreItem = new LoadMoreItem(R.string.uikit_loadmore_default_tips);
        loadMoreDelegate = new LoadMoreDelegate(this);
        loadMoreItemViewBinder = new LoadMoreItemViewBinder();
        register(LoadMoreItem.class, loadMoreItemViewBinder);
    }

    @Override
    public void setItems(@NonNull List<?> items) {
        if (items.isEmpty()) {
            return;
        }

        Items tempItems = new Items();
        checkLoadMoreItem(tempItems);
        //插在loadmore之前
        final int index = getItemCount() - 1;
        tempItems.addAll(index < 0 ? 0 : index, items);
        super.setItems(tempItems);
    }

    private void checkLoadMoreItem(Items tempItems) {
        //有数据就表示已添加
        if (getItemCount() <= 0) {
            tempItems.add(loadMoreItem);
        }
    }

    public void appendItems(@NonNull List<?> datas) {
        if (datas.isEmpty()) {
            return;
        }

        Items tempItems = new Items(getItems());
        checkLoadMoreItem(tempItems);
        //插在loadmore之前
        final int index = getItemCount() - 1;
        tempItems.addAll(index < 0 ? 0 : index, datas);
        super.setItems(tempItems);

        //刷新列表
        notifyItemRangeChanged(index, datas.size());
    }

    public void setLoadMoreItemState(@LoadMoreItem.ItemState int state) {
        int tips;
        switch (state) {
            case LoadMoreItem.STATE_COMPLETED:
                tips = R.string.uikit_loadmore_complete_tips;
                break;
            case LoadMoreItem.STATE_FAILED:
                tips = R.string.uikit_loadmore_failed_tips;
                break;

            default:
                tips = R.string.uikit_loadmore_default_tips;
                break;
        }

        this.loadMoreItem.setState(state);
        this.loadMoreItem.setTips(tips);
        notifyItemChanged(getItemCount() - 1);
    }

    public void setLoadMoreItemRetryListener(OnLoadMoreRetryListener listener) {
        loadMoreItemViewBinder.setRetryListener(listener);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        loadMoreDelegate.attach(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            Log.e(TAG, "Cannot setSpanSizeLookup on a null LayoutManager Object. " +
                    "Call setLayoutManager with a non-null argument.");
            return;
        }

        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == LOAD_MORE_ITEM_TYPE ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
                }
            });
        }
    }

    public void setLoadMoreSubject(LoadMoreDelegate.LoadMoreSubject loadMoreSubject) {
        this.loadMoreSubject = loadMoreSubject;
    }

    @Override
    public boolean isLoading() {
        return loadMoreSubject == null || loadMoreSubject.isLoading();
    }

    @Override
    public void onLoadMore() {
        if (loadMoreSubject != null && loadMoreItem.getState() != LoadMoreItem.STATE_COMPLETED) {
            loadMoreSubject.onLoadMore();
        }
    }
}
