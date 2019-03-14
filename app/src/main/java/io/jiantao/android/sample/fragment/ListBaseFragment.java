package io.jiantao.android.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.jiantao.android.sample.R;
import io.jiantao.android.uikit.adapter.loadmore.LoadMoreDelegate;
import io.jiantao.android.uikit.refresh.ISwipeRefreshLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author drakeet
 */
public abstract class ListBaseFragment extends Fragment
    implements ISwipeRefreshLayout.OnRefreshListener, LoadMoreDelegate.LoadMoreSubject {

    private static final String TAG = ListBaseFragment.class.getSimpleName();

    @BindView(android.R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    ISwipeRefreshLayout iSwipeRefreshLayout;

    private LoadMoreDelegate loadMoreDelegate;

    private AtomicInteger loadingCount;
    private boolean isEnd;

    protected MultiTypeAdapter adapter;
    protected Items items;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        loadMoreDelegate = new LoadMoreDelegate(this);
        loadingCount = new AtomicInteger(0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, root);
        recyclerView.setAdapter(adapter);
        loadMoreDelegate.attach(recyclerView);
        loadData(true);
        return root;
    }


    protected abstract void loadData(boolean clear);


    protected boolean onInterceptLoadMore() {
        return false;
    }


    protected void setRefresh(boolean refresh) {
        iSwipeRefreshLayout.setRefreshing(refresh);
    }


    @Override
    public void onRefresh() {
        loadData(true);
    }


    @Override
    public final void onLoadMore() {
        if (!isEnd()) {
            Log.d(TAG, "[onLoadMore]" + "isEnd == false");
            if (!onInterceptLoadMore()) {
                loadData(false);
            }
        }
    }


    protected boolean isShowingRefresh() {
        return iSwipeRefreshLayout.isRefreshing();
    }


    public void setEnd(boolean end) {
        isEnd = end;
    }


    public boolean isEnd() {
        return isEnd;
    }


    protected void setSwipeToRefreshEnabled(boolean enable) {
        iSwipeRefreshLayout.setEnabled(enable);
    }


    public void smoothScrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
    }


    @Override
    public boolean isLoading() {
        return loadingCount.get() > 0;
    }


    protected void notifyLoadingStarted() {
        loadingCount.getAndIncrement();
    }


    protected void notifyLoadingFinished() {
        loadingCount.decrementAndGet();
    }
}