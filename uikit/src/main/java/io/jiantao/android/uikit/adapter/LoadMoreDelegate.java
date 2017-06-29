package io.jiantao.android.uikit.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * FIXME 目前只支持 LinearLayoutManager。
 * 后期考虑：GridLayoutManager
 * @author drakeet
 */
public class LoadMoreDelegate {

    private final LoadMoreSubject loadMoreSubject;


    public LoadMoreDelegate(LoadMoreSubject loadMoreSubject) {
        this.loadMoreSubject = loadMoreSubject;
    }


    /**
     * Should be called after recyclerView setup with its layoutManager and adapter
     *
     * @param recyclerView the RecyclerView
     */
    public void attach(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager
            = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(
            new EndlessScrollListener(layoutManager, loadMoreSubject));
    }


    private static class EndlessScrollListener extends RecyclerView.OnScrollListener {

        private static final int VISIBLE_THRESHOLD = 6;
        private final LinearLayoutManager layoutManager;
        private final LoadMoreSubject loadMoreSubject;


        private EndlessScrollListener(LinearLayoutManager layoutManager, LoadMoreSubject loadMoreSubject) {
            this.layoutManager = layoutManager;
            this.loadMoreSubject = loadMoreSubject;
        }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy < 0 || loadMoreSubject == null || !loadMoreSubject.isLoading()) return;

            final int itemCount = layoutManager.getItemCount();
            final int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
            final boolean isBottom = (lastVisiblePosition >= itemCount - VISIBLE_THRESHOLD);
            if (isBottom) {
                loadMoreSubject.onLoadMore();
            }
        }
    }


    public interface LoadMoreSubject {
        boolean isLoading();
        void onLoadMore();
    }
}