package com.fitmanager.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.fragment.HomeFragment;
import com.fitmanager.app.fragment.MyPageFragment;
import com.fitmanager.app.fragment.PopularityVideoFragment;
import com.fitmanager.app.util.LoginUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends AppCompatActivity /*DrawerActivity*/ {
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private final String[] ACTION_BAR_TITLE = {"코치", "인기동영상", "마이페이지"};


    Context mContext;
    View view;
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("코치");

        mViewPager =  findViewById(R.id.pager);
        mViewPager.setAdapter(fragmentStatePagerAdapter);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeActionBarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Bind the title indicator to the adapter
        TitlePageIndicator mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setCurrentItem(0);

        mContext = getApplicationContext();


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        getFcmToken();
    }

    private void getFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d(TAG, "token: " + token);
//                        Toast.makeText(MainActivity.this, "token: " + token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeActionBarTitle(int position) {
        getSupportActionBar().setTitle(ACTION_BAR_TITLE[position]);
    }

    FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();

                case 1:
                    return PopularityVideoFragment.newInstance();
                case 2:
                    return MyPageFragment.newInstance();
                default:
                    return HomeFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "코치";
                case 1:
                    return "인기동영상";
                case 2:
                    return "마이페이지";

            }
            return "";
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof MyPageFragment) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

//        getMenuInflater().inflate(R.menu.coach_menu, menu);
        mMenu = menu;
//        MenuInflater inflater = getMenuInflater();
        MenuItem item = menu.add(0, 1, 0, "로그인");
        item.setIcon(R.drawable.star);
        menu.add(0, 2, 0, "관리자에게 글남기기").setIcon(R.drawable.star_yellow);
        menu.add(0, 3, 0, "공지사항").setIcon(R.drawable.star_gray);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                if (LoginUtils.isLoggedIn()) {
                    LoginUtils.setLoginUserVO(null);
                    fragmentStatePagerAdapter.notifyDataSetChanged();
//                    finish();
//                    startActivity(getIntent());
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;
            }

        }
        return false;
    }

    private View.OnTouchListener menuListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            openOptionsMenu();
            return false;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenu != null) {
            MenuItem loginItem = mMenu.getItem(0);
            if (loginItem != null) {
                if (LoginUtils.isLoggedIn()) {
                    loginItem.setTitle("로그아웃");
                } else {
                    loginItem.setTitle("로그인");
                }
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }
}



class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}



