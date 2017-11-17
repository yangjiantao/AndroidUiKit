package io.jiantao.android.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

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
