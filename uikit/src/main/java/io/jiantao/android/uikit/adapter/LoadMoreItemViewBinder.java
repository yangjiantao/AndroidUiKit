package io.jiantao.android.uikit.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.jiantao.android.uikit.R;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 列表加载更多view
 * Created by jiantao on 2017/6/29.
 */

class LoadMoreItemViewBinder extends ItemViewBinder<LoadMoreItem, LoadMoreItemViewBinder.ViewHolder> {

    private OnLoadMoreRetryListener mRetryListener;

    public LoadMoreItemViewBinder() {
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_recyclerview_loadmore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LoadMoreItem item) {
        holder.data = item;
        holder.updateView(item.getState());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LoadMoreItem data;
        ProgressBar progressBar;
        TextView tips;

        public ViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tips = (TextView) itemView.findViewById(R.id.tv_tips);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getState() == LoadMoreItem.STATE_FAILED && mRetryListener != null) {
                        mRetryListener.onRetry();
                        data.setState(LoadMoreItem.STATE_LOADING);
                        updateView(LoadMoreItem.STATE_LOADING);
                    }
                }
            });
        }

        void updateView(@LoadMoreItem.ItemState int state) {
            switch (state) {
                case LoadMoreItem.STATE_LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    tips.setVisibility(View.GONE);
                    break;

                case LoadMoreItem.STATE_HIDE:
                    progressBar.setVisibility(View.GONE);
                    tips.setVisibility(View.GONE);
                    break;
                case LoadMoreItem.STATE_FAILED:
                case LoadMoreItem.STATE_COMPLETED:
                    progressBar.setVisibility(View.GONE);
                    tips.setVisibility(View.VISIBLE);

                    tips.setText(data.getTips());
                    break;
                default:
                    break;
            }

        }
    }

    void setRetryListener(OnLoadMoreRetryListener mRetryListener) {
        this.mRetryListener = mRetryListener;
    }

}
