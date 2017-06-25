package io.jiantao.android.sample.tablayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.jiantao.android.sample.R;
import io.jiantao.android.sample.common.SimpleCardFragment;
import io.jiantao.android.uikit.tablayout.CommonTabAdapter;
import io.jiantao.android.uikit.tablayout.CommonTabLayout;
import io.jiantao.android.uikit.tablayout.MsgView;
import io.jiantao.android.uikit.tablayout.OnTabSelectListener;
import io.jiantao.android.uikit.tablayout.SlidingTabLayout;

/**
 * 代码来源及用户参考： https://github.com/H07000223/FlycoTabLayout
 */
public class SlidingTabActivity extends AppCompatActivity implements OnTabSelectListener {
    @BindView(R.id.tl_1)
    SlidingTabLayout tabLayout_1;
    @BindView(R.id.tl_2)
    SlidingTabLayout tabLayout_2;
    @BindView(R.id.tl_3)
    SlidingTabLayout tabLayout_3;
    @BindView(R.id.tl_4)
    SlidingTabLayout tabLayout_4;
    @BindView(R.id.tl_5)
    SlidingTabLayout tabLayout_5;
    @BindView(R.id.tl_6)
    SlidingTabLayout tabLayout_6;
    @BindView(R.id.tl_7)
    SlidingTabLayout tabLayout_7;
    @BindView(R.id.tl_8)
    SlidingTabLayout tabLayout_8;
    @BindView(R.id.tl_9)
    SlidingTabLayout tabLayout_9;
    @BindView(R.id.ctl_10)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    };
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_tablayout);
        ButterKnife.bind(this);

        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }


        View decorView = getWindow().getDecorView();
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        tabLayout_1.setViewPager(vp);
        tabLayout_2.setViewPager(vp);
        tabLayout_2.setOnTabSelectListener(this);
        tabLayout_3.setViewPager(vp);
        tabLayout_4.setViewPager(vp);
        tabLayout_5.setViewPager(vp);
        tabLayout_6.setViewPager(vp);
        tabLayout_7.setViewPager(vp, mTitles);
        tabLayout_8.setViewPager(vp, mTitles, this, mFragments);
        tabLayout_9.setViewPager(vp);

        vp.setCurrentItem(4);

        tabLayout_1.showDot(4);
        tabLayout_3.showDot(4);
        tabLayout_2.showDot(4);

        tabLayout_2.showMsg(3, 5);
        tabLayout_2.setMsgMargin(3, 0, 10);
        MsgView rtv_2_3 = tabLayout_2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        tabLayout_2.showMsg(5, 5);
        tabLayout_2.setMsgMargin(5, 0, 10);

        initCommonTabLayout();
    }

    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};

    private void initCommonTabLayout() {
        List<CommonTabAdapter> tabEntitys = new ArrayList<>(mIconUnselectIds.length);
        for(int i=0;i<mIconUnselectIds.length;i++){
            TabEntity entity = new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]);
            tabEntitys.add(entity);
        }
        commonTabLayout.setTabData(tabEntitys);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                System.out.println(" onTabSelect position "+ position);
                vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                System.out.println(" onTabReselect position "+ position);
            }
        });

        //两位数
        commonTabLayout.showMsg(0, 55);
        commonTabLayout.setMsgMargin(0, -5, 5);

        //三位数
        commonTabLayout.showMsg(1, 100);
        commonTabLayout.setMsgMargin(1, -5, 5);

        //设置未读消息红点
        commonTabLayout.showDot(2);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                System.out.println(" onPageScrolled position "+ position);
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(" onPageSelected position "+ position);
                commonTabLayout.setCurrentTab(position >= mIconSelectIds.length ? mIconSelectIds.length - 1 : position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println(" onPageScrollStateChanged state "+ state);
            }
        });
    }

    @Override
    public void onTabSelect(int position) {
        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
