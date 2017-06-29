package io.jiantao.android.uikit.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import io.jiantao.android.uikit.R;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.multitype.MultiTypePool;
import me.drakeet.multitype.TypePool;

/**
 * 分页加载Adapter
 * Created by jiantao on 2017/6/29.
 */

public class MultiTypeLoadMoreAdapter extends MultiTypeAdapter implements LoadMoreDelegate.LoadMoreSubject {

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
        if(items.isEmpty()){
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
        if(getItemCount() <= 0){//有数据就表示已添加
            tempItems.add(loadMoreItem);
        }
    }

    public void appendItems(@NonNull List<?> datas){
        if(datas.isEmpty()){
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

    public void setLoadMoreItemState(@LoadMoreItem.ItemState int state){
        int tips;
        switch(state){
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

    public void setLoadMoreItemRetryListener(OnLoadMoreRetryListener listener){
        loadMoreItemViewBinder.setRetryListener(listener);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        loadMoreDelegate.attach(recyclerView);
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
        if(loadMoreSubject != null){
            loadMoreSubject.onLoadMore();
        }
    }
}
