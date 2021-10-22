package io.jiantao.android.sample.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jiantao on 2017/11/13.
 */

public class RecyclerViewTestActivity extends AppCompatActivity{

    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = new RecyclerView(this);

        setContentView(mRecyclerView);
    }
}
