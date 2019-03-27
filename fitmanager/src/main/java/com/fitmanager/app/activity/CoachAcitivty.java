package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fitmanager.app.R;
import com.fitmanager.app.fragment.MealFragment;
import com.fitmanager.app.fragment.VideoFragment;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.ImageUtils;
import com.fitmanager.app.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rey.material.widget.SnackBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CoachAcitivty extends AppCompatActivity {
    private static FitProgressBar mProgressBar = new FitProgressBar();
    final static String TAG = "CoachAcitivty";
    private Toolbar mToolbar;
    private int mCoachId;
    ViewPagerAdaper viewPagerAdapter;
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_exercisetype)
    TextView tvExerciseType;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.btn_bookmark_num_count)
    TextView tvBookmarkCount;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.toolbarCollapse)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.btn_info)
    ImageButton btnInfo;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SnackBar mSnackBar;
        mContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);
        ButterKnife.bind(this);
        setToolbar();
        getIntentInit();
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        collapsingToolbar.setTitle(" ");


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoachInfomationActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("coachId", mCoachId);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


        getCoachInfoRestService();

    }


    private void initViewPager() {
        viewPagerAdapter = new ViewPagerAdaper(getSupportFragmentManager());
        viewPagerAdapter.addFragment(MealFragment.createInstance(mCoachId), "식단");
        viewPagerAdapter.addFragment(VideoFragment.createInstance(mCoachId), "운동영상");
        mViewPager.setAdapter(viewPagerAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCoachId = bundle.getInt("coachId");
        Log.d(TAG, "coachId: " + mCoachId);


    }


    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "Back button clicked", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
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

    private void getCoachInfoRestService() {
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<CoachVO> coachVOCall = service.getCoachInfo(mCoachId);

        coachVOCall.enqueue(new Callback<CoachVO>() {
            @Override
            public void onResponse(Call<CoachVO> call, Response<CoachVO> response) {

                final CoachVO coachVO = response.body();
                if (coachVO != null) {

                    String sbAppendHeight = coachVO.getHeight().toString() + "cm";
                    String sbAppendWeight = coachVO.getWeight().toString() + "kg";

                    Log.d(TAG, "coachVO : " + coachVO);
                    tvName.setText(coachVO.getCoachName());
                    tvHeight.setText(strInsertBlank(sbAppendHeight));
                    tvHeight.setBackgroundResource(R.drawable.xml_colorbg_main);
                    tvWeight.setText(strInsertBlank(sbAppendWeight));
                    tvWeight.setBackgroundResource(R.drawable.xml_colorbg_main);
                    tvBookmarkCount.setText(coachVO.getBookmarkCount() + "");
                    tvExerciseType.setText(Utils.getExerciseType(coachVO.getExerciseType()));
                    tvExerciseType.setBackgroundResource(Utils.getExerciseByColorType((coachVO.getExerciseType())));
                    tvCompany.setText(strInsertBlank(coachVO.getCompany()));
                    tvCompany.setBackgroundResource(R.drawable.xml_colorbg_main);
                    Glide.with(getApplicationContext())
                            .load(coachVO.getProfileImg())
                            .centerCrop()
                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                            .crossFade()
                            .into(imgProfile);


                    ImageUtils.getProfileImage(getApplicationContext(), imgProfile,
                            coachVO.getProfileImg()
                    );

                    MealFragment mealFragment = (MealFragment) viewPagerAdapter.getItem(0);
                    mealFragment.setCoachVO(coachVO);
                    VideoFragment videoFragment = (VideoFragment) viewPagerAdapter.getItem(1);
                    videoFragment.setCoachVO(coachVO);
                    hideProgress();

                }
            }

            @Override
            public void onFailure(Call<CoachVO> call, Throwable t) {
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");
                hideProgress();

            }
        });
    }

    private String strInsertBlank(String getStrData) {
        String strData = "";
        switch (getStrData) {

            default:
                strData = "  " + getStrData + "  ";
                break;
        }
        return strData;
    }


    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.show(mContext);
        }
    }


    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
    }

}
