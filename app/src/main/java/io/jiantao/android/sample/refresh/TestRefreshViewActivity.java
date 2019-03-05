package io.jiantao.android.sample.refresh;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import io.jiantao.android.sample.R;
import io.jiantao.android.uikit.adapter.LoadMoreDelegate;
import io.jiantao.android.uikit.adapter.LoadMoreItem;
import io.jiantao.android.uikit.adapter.MultiTypeLoadMoreAdapter;
import io.jiantao.android.uikit.adapter.OnLoadMoreRetryListener;
import io.jiantao.android.uikit.refresh.ISwipeRefreshLayout;
import io.jiantao.android.uikit.widget.IDividerItemDecoration;
import me.drakeet.multitype.Items;

/**
 * Created by jiantao on 2017/6/15.
 */

public class TestRefreshViewActivity extends Activity {

    ISwipeRefreshLayout refreshLayout;
    MultiTypeLoadMoreAdapter adapter;

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
        IDividerItemDecoration divierDecoration = new IDividerItemDecoration(this, IDividerItemDecoration.VERTICAL)
                .setVerticalDividerHeight(50)
                .setDividerColor(Color.BLUE)
                .setDrawable(getResources().getDrawable(R.drawable.custom_divider))
                .setDividerPadding(30);
        recyclerView.addItemDecoration(divierDecoration);
        recyclerView.setLayoutManager(getLayoutManager());

        adapter = new MultiTypeLoadMoreAdapter();
        adapter.register(TextItem.class, new TextItemViewBinder());
        recyclerView.setAdapter(adapter);

        adapter.setLoadMoreItemRetryListener(new OnLoadMoreRetryListener() {
            @Override
            public void onRetry() {
                isLoading = true;
                handler.sendEmptyMessageDelayed(2, 2000);
            }
        });

        adapter.setLoadMoreSubject(new LoadMoreDelegate.LoadMoreSubject() {
            @Override
            public boolean isLoading() {

                return isLoading;
            }

            @Override
            public void onLoadMore() {
                System.out.println(" onloacmore called ");
                isLoading = true;
                handler.sendEmptyMessageDelayed(2, 2000);
            }
        });

        /* Mock the data */
//        TextItem textItem = new TextItem("world");


        adapter.setItems(createItems());
        adapter.notifyDataSetChanged();

    }

    private RecyclerView.LayoutManager getLayoutManager() {
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        return layoutManager;
    }

    boolean isLoading;
    int index = 1;

    private List<?> createItems() {
        Items items = new Items();
        for (int i = index; i < index + 20; i++) {
            TextItem textItem = new TextItem("world no." + i);
            items.add(textItem);
        }
        index += items.size();
        return items;
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

                case 2://加载完成
                    boolean succeed = random.nextBoolean();
                    if (succeed) {
                        adapter.appendItems(createItems());
                    } else {
                        adapter.setLoadMoreItemState(LoadMoreItem.STATE_FAILED);
                    }

                    if (index < 101) {
                        isLoading = false;
                    } else {
                        adapter.setLoadMoreItemState(LoadMoreItem.STATE_COMPLETED);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

}
