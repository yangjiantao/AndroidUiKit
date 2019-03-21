package io.jiantao.android.sample.refresh;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;
import io.jiantao.android.sample.R;
import io.jiantao.android.uikit.adapter.loadmore.LoadMoreDelegate;
import io.jiantao.android.uikit.adapter.loadmore.MultiTypeLoadMoreAdapter;
import io.jiantao.android.uikit.refresh.ISwipeRefreshLayout;
import io.jiantao.android.uikit.widget.IDividerItemDecoration;
import me.drakeet.multitype.Items;

/**
 * Created by jiantao on 2017/6/15.
 */

public class TestRefreshViewActivity extends Activity {

    private static final String TAG = "TestRefreshViewActivity";
    ISwipeRefreshLayout refreshLayout;
    MultiTypeLoadMoreAdapter adapter;

    List<TextItem> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        refreshLayout = (ISwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new ISwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do refresh ...
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        });
        refreshLayout.setRefreshHeaderView(new MedlinkerRefreshHeaderView(this));
        handler.sendEmptyMessageDelayed(0, 2000);
        refreshLayout.setEnabled(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(getLayoutManager());
        IDividerItemDecoration divierDecoration = new IDividerItemDecoration(this, IDividerItemDecoration.VERTICAL)
                .setVerticalDividerHeight(50)
                .setHorizontalDividerWidth(50)
                .setDividerColor(Color.GRAY)
                .setDividerPadding(30);

        // or setCustomDrawable
        // divierDecoration.setDrawable(getResources().getDrawable(R.drawable.custom_divider))
        recyclerView.addItemDecoration(divierDecoration);

        adapter = new MultiTypeLoadMoreAdapter();
        adapter.register(TextItem.class, new TextItemViewBinder());
        recyclerView.setAdapter(adapter);

        adapter.setLoadMoreRetryListener(new MultiTypeLoadMoreAdapter.ILoadMoreRetryListener() {
            @Override
            public void retry() {
                Log.d(TAG, "onLoadMore retry listener called.");
                isLoading = true;
                handler.sendEmptyMessage(2);
            }
        });

        adapter.setLoadMoreSubject(new LoadMoreDelegate.LoadMoreSubject() {
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore method called.");
                isLoading = true;
                handler.sendEmptyMessage(2);
            }
        });

        /* Mock the data */
//        TextItem textItem = new TextItem("world");

        items = createItems();
        adapter.setItems(items);
        adapter.notifyDataSetChanged();

    }

    private RecyclerView.LayoutManager getLayoutManager() {
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        return layoutManager;
    }

    boolean isLoading;
    int index = 1;

    private List<TextItem> createItems() {
        List<TextItem> newData = new ArrayList<>(20);
        for (int i = index; i < index + 20; i++) {
            TextItem textItem = new TextItem("world no." + i);
            newData.add(textItem);
        }
        index += 20;
        return newData;
    }

    Random random = new Random();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// refreshing ture
                    refreshLayout.setRefreshing(true);
                    handler.sendEmptyMessageDelayed(1, 30000);
                    break;

                case 1:// refreshing false
                    refreshLayout.setRefreshing(false);
                    break;

                case 2://加载ing
                    handler.sendEmptyMessageDelayed(3, 5000);
                    break;
                case 3:// 加载成功
                    boolean succeed = random.nextBoolean();
                    if (succeed) {
                        items.addAll(createItems());
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                        if (items.size() > 100) {
                            adapter.setLoadMoreItemState(MultiTypeLoadMoreAdapter.LoadMoreItem.STATE_NO_MORE_DATA);
                            Log.d(TAG, " load success, no more data. itemCount = "+adapter.getItemCount());
                        }else{
                            Log.d(TAG, " load success, has more data. itemCount = "+adapter.getItemCount());
                        }
                    } else {
                        adapter.setLoadMoreItemState(MultiTypeLoadMoreAdapter.LoadMoreItem.STATE_FAILED);
                        Log.d(TAG, " load failed.");
                    }
                    isLoading = false;
                    break;
                default:
                    break;
            }
            return false;
        }
    });

}
