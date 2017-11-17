package io.jiantao.android.uikit.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 只支持新增Fragment。不能删除fragment和更新arguments
 * @author jiantao
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = BaseFragmentPagerAdapter.class.getSimpleName();

    private SparseArray<Fragment> mCachedFragments = new SparseArray<Fragment>();
    private final List<FragmentData> mFragmentDatas;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<FragmentData> fragmentDatas) {
        super(fm);
        this.mFragmentDatas = fragmentDatas;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            Fragment fragment = mCachedFragments.get(position);
            FragmentData data = mFragmentDatas.get(position);
            if (fragment == null || fragment.isRemoving() || !fragment.getClass().equals(data.fragmentClass)) {
                fragment = (Fragment) data.fragmentClass.newInstance();
                mCachedFragments.put(position, fragment);
            }

            if (data.bundle != null) {
                fragment.setArguments(data.bundle);
            }
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentDatas.size();
    }

    public Fragment getFragment(int position) {
        return mCachedFragments.get(position);
    }

    public void addFragment(FragmentData data) {
        mFragmentDatas.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentDatas.get(position).title;
    }

    /**
     * call setAdapter before
     */
    public void sort() {
        if (this.mFragmentDatas != null && !mFragmentDatas.isEmpty()) {
            mCachedFragments.clear();
            Collections.sort(mFragmentDatas);
        }
    }

    /**
     * contains: title, fragmentClass and fragment args(arguments)
     */
    public static class FragmentData implements Comparable<FragmentData> {
        /**
         * the title to show
         */
        private String title;
        /**
         * the fragment class
         */
        private Class<?> fragmentClass;
        /**
         * the arguments of create fragment
         */
        private Bundle bundle;
        /**
         * this is only used in {@link BaseFragmentPagerAdapter#sort(Comparator)}
         */
        private int priority;

        /**
         * the extra data
         */
        private Object extra;


        private FragmentData(@NonNull String title, @NonNull Class<?> fragmentClass, Bundle bundle, int priority, Object extra) {
            super();
            this.title = title;
            this.fragmentClass = fragmentClass;
            this.bundle = bundle;
            this.priority = priority;
            this.extra = extra;
        }

        @Override
        public int compareTo(@NonNull FragmentData another) {
            return this.priority < another.priority ? -1 :
                    (this.priority == another.priority ? 0 : 1);
            //return Integer.compare(this.priority, another.priority);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !(o instanceof FragmentData))
                return false;
            FragmentData that = (FragmentData) o;
            return title.equals(that.title);
        }

        @Override
        public int hashCode() {
            return title.hashCode();
        }

        @Override
        public String toString() {
            return "FragmentData{" +
                    "title='" + title + '\'' +
                    ", fragmentClass=" + fragmentClass +
                    ", bundle=" + bundle +
                    ", priority=" + priority +
                    ", extra=" + extra +
                    '}';
        }


        public static class Builder {
            /**
             * the title to show
             */
            private String title;
            /**
             * the fragment class
             */
            private Class<?> fragmentClass;
            /**
             * the arguments of create fragment
             */
            private Bundle bundle;
            /**
             * this is only used in {@link BaseFragmentPagerAdapter#sort(Comparator)}
             */
            private int priority;

            /**
             * the extra data
             */
            private Object extra;

            public Builder setTitle(@NonNull String title) {
                this.title = title;
                return this;
            }

            public Builder setFragmentClass(@NonNull Class<?> fragmentClass) {
                this.fragmentClass = fragmentClass;
                return this;
            }

            public Builder setBundle(Bundle bundle) {
                this.bundle = bundle;
                return this;
            }

            public Builder setPriority(int priority) {
                this.priority = priority;
                return this;
            }

            public Builder setExtra(Object extra) {
                this.extra = extra;
                return this;
            }

            public FragmentData build() {
                return new FragmentData(this.title, this.fragmentClass, this.bundle, this.priority, this.extra);
            }

        }
    }
}
