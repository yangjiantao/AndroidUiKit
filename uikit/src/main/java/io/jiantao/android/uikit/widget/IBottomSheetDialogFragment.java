package io.jiantao.android.uikit.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.ref.WeakReference;

import io.jiantao.android.uikit.util.LogUtils;

/**
 * 1. 支持数据恢复
 * 2. 背景透明
 * 3. 固定高度？
 *
 * @author Created by jiantaoyang
 * @date 2019-06-17
 */
public class IBottomSheetDialogFragment extends AppCompatDialogFragment {
    private static final String TAG = IBottomSheetDialogFragment.class.getSimpleName();

    private Dialog mDialog;

    private boolean shouldRetainInstance = false;

    private WeakReference<FragmentManager> fragmentManagerWeakReference;

    private View mView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new BottomSheetDialog(getContext(), this.getTheme());
        return mDialog;
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateView");
        if (mView == null) {
            mView = onCreateView(inflater, container);
        } else {
            ViewParent parent = mView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mView);
            }
        }
        return mView;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        LogUtils.d(TAG, "onDismiss");
        if (mDialog == null) {
            return;
        }
        // 统一处理dismiss
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        LogUtils.d(TAG, "onCancel");
    }

    @Override
    public void dismiss() {
        LogUtils.d(TAG, "dismiss , shouldRetainInstance " + shouldRetainInstance);
        if (!shouldRetainInstance || fragmentManagerWeakReference == null || fragmentManagerWeakReference.get() == null) {
            dismissAllowingStateLoss();
        } else {
            detachSelf(fragmentManagerWeakReference.get());
        }
    }

    /**
     * just detach this fragment for next show
     *
     * @param manager
     */
    private void detachSelf(FragmentManager manager) {
        manager.beginTransaction().detach(this).commitAllowingStateLoss();
    }

    private void commonShow(FragmentManager manager, String tag) {
        if (tag == null) {
            shouldRetainInstance = false;
        }
        fragmentManagerWeakReference = new WeakReference<>(manager);
        super.show(manager, tag);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        // 使用此方法只能在dismiss时remove当前fragment
        shouldRetainInstance = false;
        return super.show(transaction, tag);
    }


    @Override
    public final void show(FragmentManager manager, String tag) {
        commonShow(manager, tag);
    }

    @Override
    public final void showNow(FragmentManager manager, String tag) {
        commonShow(manager, tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, "onActivityCreated");
        // after Dialog.setContentView , set it's parent backgroundColor transparent
        if (mView != null && mView.getParent() != null) {
            ((View) mView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d(TAG, "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, "onDestroyView");
        mDialog = null;
    }

    /**
     * 显示Fragment并在dismiss是不remove自己，以便再次显示时恢复状态。如果此需求直接调用方法：show()
     */
    public static void showWithRetainInstance(FragmentManager manager, String tag, @NonNull ICreateFragmentListener listener) {
        Fragment fragmentByTag = manager.findFragmentByTag(tag);
        if (fragmentByTag != null) {
            manager.beginTransaction().attach(fragmentByTag).commitAllowingStateLoss();
            return;
        }

        IBottomSheetDialogFragment fragment = listener.get();
        fragment.shouldRetainInstance = true;
        fragment.show(manager, tag);
    }

    public interface ICreateFragmentListener {
        IBottomSheetDialogFragment get();
    }

}
