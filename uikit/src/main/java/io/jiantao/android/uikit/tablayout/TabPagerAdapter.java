package io.jiantao.android.uikit.tablayout;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 在系统FragmentPagerAdapter源码基础上修改。
 * 建议用在底部多tab切换的app UI主结构中。
 * feature ：
 * 1. 轻松获取已存在Fragment
 * 2. 自如切换
 *
 * Created by jiantao on 2017/6/27.
 */

public class TabPagerAdapter extends PagerAdapter {
    private static final String TAG = "FragmentPagerAdapter";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    private final int pageCount;
    private final Context mContext;
    private final SparseArray<Class<?>> fragments;

    private int mLastPagePosition;
    private final ViewGroup mViewGroup;
    //是否保留页面状态
    private boolean retainPagesState = true;

    public TabPagerAdapter(Context context, FragmentManager fm, SparseArray<Class<?>> fragments, ViewGroup viewGroup) {
        mFragmentManager = fm;
        this.fragments = fragments;
        this.pageCount = fragments.size();
        this.mContext = context;
        this.mViewGroup = viewGroup;
        mLastPagePosition = -1;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public Fragment getItem(int position) {
        Fragment fragment = Fragment.instantiate(mContext, fragments.get(position).getName());
        return fragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    public void switchPage(int position) {
        if (mLastPagePosition >= 0) {
            Fragment lastPage = (Fragment) instantiateItem(mViewGroup, mLastPagePosition);
            destroyItem(mViewGroup, mLastPagePosition, lastPage);
        }
        Fragment fragment = (Fragment) instantiateItem(mViewGroup, position);
        setPrimaryItem(mViewGroup, position, fragment);
        finishUpdate(mViewGroup);
        mLastPagePosition = position;
    }

    public Fragment getFragment(int postion) {
        return getFragment(mViewGroup, postion);
    }

    /**
     * @param retainPagesState 保留页面状态
     */
    public void setRetainPagesState(boolean retainPagesState) {
        this.retainPagesState = retainPagesState;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            if(retainPagesState){
                mCurTransaction.show(fragment);
            }else{
                mCurTransaction.attach(fragment);
            }
        } else {
            fragment = getItem(position);
            if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment,
                    makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object
                + " v=" + ((Fragment) object).getView());
        if(retainPagesState){
            mCurTransaction.hide((Fragment) object);
        }else{
            mCurTransaction.detach((Fragment) object);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    /**
     * Return a unique identifier for the item at the given position.
     * <p>
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position) {
        return position;
    }

    protected Fragment getFragment(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), getItemId(position));
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        return fragment;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
