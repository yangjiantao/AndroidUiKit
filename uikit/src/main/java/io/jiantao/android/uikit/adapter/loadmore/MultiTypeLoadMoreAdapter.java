package io.jiantao.android.uikit.adapter.loadmore;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.multitype.MultiTypePool;
import me.drakeet.multitype.TypePool;

/**
 * 分页加载Adapter
 *
 * @author jiantao
 */

public class MultiTypeLoadMoreAdapter extends MultiTypeAdapter implements LoadMoreDelegate.LoadMoreSubject {
    private static final String TAG = MultiTypeLoadMoreAdapter.class.getSimpleName();
    private AbstractItemViewBinder loadMoreItemViewBinder;
    final LoadMoreItem loadMoreItem;
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
        loadMoreItem = new LoadMoreItem();
        loadMoreDelegate = new LoadMoreDelegate(this);
        loadMoreItemViewBinder = new DefaultLoadMoreItemViewBinder();
        register(LoadMoreItem.class, loadMoreItemViewBinder);
    }

    @Override
    public void setItems(@NonNull List<?> items) {
        if (items.isEmpty()) {
            // empty call super
            super.setItems(items);
            return;
        }

        Items tempItems = new Items(items.size() + 1);
        //插在loadmore之前
        tempItems.addAll(items);
        tempItems.add(loadMoreItem);
        super.setItems(tempItems);
    }


    public void setLoadMoreItemState(@LoadMoreItem.ItemState int state) {
        this.loadMoreItem.setState(state);
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * @param listener retry event callback
     */
    public void setLoadMoreRetryListener(ILoadMoreRetryListener listener) {
        loadMoreItemViewBinder.setLoadMoreRetryListener(listener);
    }

    public void setLoadMoreItemViewBinder(@NonNull AbstractItemViewBinder viewBinder) {
        viewBinder.setLoadMoreRetryListener(loadMoreItemViewBinder.mListener);
        this.loadMoreItemViewBinder = viewBinder;
        register(LoadMoreItem.class, viewBinder);
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
                    Object obj = getItems().get(position);
                    return obj.getClass().equals(LoadMoreItem.class) ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
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
        if (loadMoreSubject != null && loadMoreItem.getState() == LoadMoreItem.STATE_LOADING) {
            loadMoreSubject.onLoadMore();
        }
    }

    /**
     * dataEntity for LoadMoreViewBinder
     */
    public static class LoadMoreItem {

        public static final int STATE_LOADING = 0;
        /**
         * finish load
         */
        public static final int STATE_NO_MORE_DATA = 1;
        public static final int STATE_FAILED = 2;
        /**
         * not visible
         */
        public static final int STATE_GONE = 3;

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({STATE_NO_MORE_DATA, STATE_FAILED, STATE_LOADING, STATE_GONE})
        public @interface ItemState {
        }

        private int mState = STATE_LOADING;

        @ItemState
        public int getState() {
            return mState;
        }

        public void setState(@ItemState int mState) {
            this.mState = mState;
        }
    }

    static class DefaultLoadMoreItemViewBinder extends AbstractItemViewBinder<DefaultLoadMoreItemViewBinder.DefaultViewHolder> {

        @Override
        protected DefaultViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            TextView view = new TextView(inflater.getContext());
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setGravity(Gravity.CENTER);
            return new DefaultViewHolder(view);
        }

        class DefaultViewHolder extends ViewHolder {

            public DefaultViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            @Override
            public void setState(int state) {
                String tips = "state_none";
                switch (state) {
                    case LoadMoreItem.STATE_LOADING:
                        tips = "state loading";
                        break;

                    case LoadMoreItem.STATE_FAILED:
                        tips = "state failed";
                        break;
                    case LoadMoreItem.STATE_NO_MORE_DATA:
                        tips = "state no more data";
                        break;
                    case LoadMoreItem.STATE_GONE:
                        itemView.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                ((TextView) itemView).setText(tips);
            }
        }
    }

    /**
     * loadMoreViewBinder基类，限制子类实现各种状态。
     */
    public static abstract class AbstractItemViewBinder<VH extends ViewHolder> extends ItemViewBinder<LoadMoreItem, VH> {

        private ILoadMoreRetryListener mListener;

        @NonNull
        @Override
        protected final VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            VH viewHolder = createViewHolder(inflater, parent);
            viewHolder.mListener = mListener;
            return viewHolder;
        }

        @Override
        protected final void onBindViewHolder(@NonNull VH holder, @NonNull LoadMoreItem item) {
            holder.data = item;
            holder.setState(item.getState());
        }

        protected abstract VH createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

        void setLoadMoreRetryListener(ILoadMoreRetryListener listener) {
            this.mListener = listener;
        }
    }

    /**
     * loadMore ViewHolder 基类
     */
    public static abstract class ViewHolder extends RecyclerView.ViewHolder {

        protected LoadMoreItem data;

        ILoadMoreRetryListener mListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null && data != null && data.getState() == LoadMoreItem.STATE_FAILED) {
                        mListener.retry();
                        data.mState = LoadMoreItem.STATE_LOADING;
                        setState(data.mState);
                    }
                }
            });
        }

        public abstract void setState(@LoadMoreItem.ItemState int state);
    }

    public interface ILoadMoreRetryListener {
        /**
         * try again
         */
        void retry();
    }
}
