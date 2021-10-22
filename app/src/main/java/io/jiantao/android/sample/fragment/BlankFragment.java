package io.jiantao.android.sample.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import io.jiantao.android.sample.R;

public class BlankFragment extends Fragment {

    public static final String KEY_LABEL = "key_label";
    private static final String TAG = BlankFragment.class.getSimpleName();
    private BlankViewModel mViewModel;
    private String mLabel;

    public static BlankFragment newInstance(String label) {
        Bundle args = new Bundle();
        args.putString(KEY_LABEL, label);
        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String label = getArguments().getString(KEY_LABEL, "blankFragment");
        mLabel = label;
        Log.d(TAG, "onCreate: label " + mLabel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blank_fragment, container, false);
        TextView tips = view.findViewById(R.id.tv_tips);
        tips.setText(mLabel);
        Log.d(TAG, "onCreateView: label : " + mLabel);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BlankViewModel.class);
        // 如果Fragment有detach和attach的情况，即调用了onDestroyView却没有调用onDestroy，随后恢复时调用onCreateView的情况。
        // 出现场景：1. 使用ViewPager+FragmentPagerAdapter 2. 使用FragmentB replace替换FragmentA，并设置backstack，手动返回到FragmentA。
        // 问题：由于没有回调onDestroy生命周期函数，没有及时remove已存在的Observer，反复使用导致添加多个Observer。
        // 解决方法：1. 添加新的observer前，手动移除已存在的。2. 使用新版本新版本Fragment中getViewLifecycleOwner
        mViewModel.testString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onActivityCreated, label = " + mLabel + "; onChanged :" + hashCode());
            }
        });

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewModel.updateData();
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: label = " + mLabel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: label = " + mLabel);
    }

}
