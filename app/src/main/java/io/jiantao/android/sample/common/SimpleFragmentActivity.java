package io.jiantao.android.sample.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import io.jiantao.android.sample.R;

/**
 * Created by jiantao on 2017/6/30.
 */

public class SimpleFragmentActivity extends FragmentActivity {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private static final String FRAGMENT_CLASS_NAME = "FRAGMENT_CLASS_NAME";
    private static final String FRAGMENT_ARGS = "FRAGMENT_ARGS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment);

        String tag = getIntent().getStringExtra(FRAGMENT_TAG);

        String fragmentClass = getIntent().getStringExtra(FRAGMENT_CLASS_NAME);

        Bundle bundle = getIntent().getBundleExtra(FRAGMENT_ARGS);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content,
                Fragment.instantiate(this, fragmentClass, bundle), tag).commit();
    }


    public static void toSimpleFragmentActivity(Context context, Class<Fragment> fragmentClass, Bundle bundle){
        Intent intent = new Intent(context, SimpleFragmentActivity.class);
        intent.putExtra(FRAGMENT_CLASS_NAME, fragmentClass.getName());
        intent.putExtra(FRAGMENT_ARGS, bundle);
        if(!(context instanceof Activity)){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
