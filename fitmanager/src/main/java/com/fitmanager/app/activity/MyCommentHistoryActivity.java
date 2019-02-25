package com.fitmanager.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fitmanager.app.R;
import com.fitmanager.app.fragment.MyCommentHistoryFragment;
import com.fitmanager.app.fragment.NotiFragment;
import com.fitmanager.app.util.LoginUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCommentHistoryActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    private Toolbar mToolbar;
    ViewPagerAdaper mViewpagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment_history);
        ButterKnife.bind(this);
        initViewPager();
        setLayout();
        mTabLayout.setupWithViewPager(mViewPager);

    }


    private void initViewPager() {
        mViewpagerAdapter = new ViewPagerAdaper(getSupportFragmentManager());
        mViewpagerAdapter.addFragment(MyCommentHistoryFragment.createInstance(LoginUtils.getLoginUserVO().getMemberId()), "내댓글");
        mViewpagerAdapter.addFragment(NotiFragment.createInstance(LoginUtils.getLoginUserVO().getMemberId()), "알림");
        mViewPager.setAdapter(mViewpagerAdapter);
    }


    private void setLayout() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("내가 단 댓글");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    static class ViewPagerAdaper extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdaper(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

    }

}
