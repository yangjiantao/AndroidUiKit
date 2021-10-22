package io.jiantao.android.sample.refresh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.jiantao.android.sample.R;

/**
 * description
 *
 * @author Created by jiantaoyang
 * @date 2019/4/2
 */
public class TestRecyclerAdapter extends RecyclerView.Adapter<TestRecyclerAdapter.ViewHolder> {

    List<TextItem> items;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text, viewGroup, false);
        int height = 200;
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        layoutParams.height = height;
        root.setLayoutParams(layoutParams);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextItem textItem = items.get(i);
        viewHolder.text.setText("hello: " + textItem.text);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<TextItem> items) {
        this.items = items;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
