package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fitmanager.app.R;
import com.fitmanager.app.model.BookmarkVO;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.LoginUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CoachInfomationActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = "CoachInfomationActivity";
    private List<CoachVO> mCoachItems = new ArrayList<>();
    RecyclerView mRecyclerView;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private LinearLayout mTitleContainer;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private int mCoachId;
    private String mCoachName;
    private static FitProgressBar mProgressBar = new FitProgressBar();
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_job_name)
    TextView tvJobName;
    @BindView(R.id.main_tv_name)
    TextView mainTvName;
    @BindView(R.id.tv_career)
    TextView tvCareer;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_certificate)
    TextView tvCertificate;
    @BindView(R.id.img_profile)
    ImageView profileImg;
    @BindView(R.id.coach_background)
    ImageView coachBackground;
    Context mContext;
    @BindView(R.id.fab)
    FloatingActionButton mFabBtn;
    private String mInstaUri;
    private String mfacebookUri;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_infomation);
        mContext = this;
        ButterKnife.bind(this);
        bindActivity();
        getIntentInit();
        mAppBarLayout.addOnOffsetChangedListener(this);
        mToolbar.inflateMenu(R.menu.coach_menu);
        startAlphaAnimation(tvName, 0, View.INVISIBLE);
        startAlphaAnimation(tvJobName, 0, View.INVISIBLE);

        mFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LoginUtils.isLoggedIn()) {

                    postBookmarkService();
                } else {
                    LoginUtils.showLoginDialog(mContext);
                }
            }
        });
        mToolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            //Change the ImageView image source depends on menu item click
                            case R.id.video:
                                calllActivity();

                                return true;
                            case R.id.instagram:
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri instaUri = Uri.parse(mInstaUri);
                                intent.setData(instaUri);
                                startActivity(intent);

                                return true;
                        }
                        //If above criteria does not meet then default is false;
                        return false;
                    }
                });


    }

    private void calllActivity() {

        Intent intent = new Intent(mContext, CoachVideoListActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("coachId", mCoachId);
        extras.putString("coachName", mCoachName);
        intent.putExtras(extras);
        mContext.startActivity(intent);

    }


    private void bindActivity() {
        mToolbar = findViewById(R.id.toolbar);
        tvName = findViewById(R.id.tv_name);
        mTitleContainer = findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = findViewById(R.id.main_appbar);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
        getCoachInfoRestService();
        getRestIsBookmarkService();

    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(tvName, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(tvJobName, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(tvName, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(tvJobName, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCoachId = bundle.getInt("coachId");
        Log.d(TAG, "coachId: " + mCoachId);
    }


    private void getRestIsBookmarkService() {
        if (LoginUtils.isLoggedIn() == true) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.SERVER_ADDR)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Map<String, Object> param = new HashMap<>();
//        {target_id}, #{target_type}, #{member_id}
            param.put("targetId", mCoachId);
            param.put("targetType", Constant.TargetType.COACH.value);
            param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());


            RestService service = retrofit.create(RestService.class);
            final Call<Integer> getIsBookmarkCall = service.getIsBookmark(param);

            getIsBookmarkCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    int result = response.body();
                    if (result == 0) {
                        mFabBtn.setSelected(false);
                        mFabBtn.setImageResource(R.drawable.white_star);
                        Log.d(TAG, "onResponse: 북마크 안되어 있음.");

                        Log.d(TAG, "onResponse: 북마크 안되어 있음.");

                    } else {
                        mFabBtn.setSelected(true);
                        mFabBtn.setImageResource(R.drawable.purple_star);
                        Log.d(TAG, "onResponse: 북마크 설정되어 있음.");
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    hideProgress();
                    Log.e(TAG, "onFailure: getVideoRestIsBookmarkService rest 통신실패");
                }
            });
        }
    }


    private void getCoachInfoRestService() {
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
                    mCoachName = coachVO.getCoachName();
                    mCoachId = coachVO.getCoachId();
                    mInstaUri = coachVO.getInstagramUrl();
                    Log.i(TAG, "mInstaUri :" + coachVO.getInstagramUrl());
                    mfacebookUri = coachVO.getFacebookUrl();
                    Log.d(TAG, "coachVO : " + coachVO);
                    tvName.setText(coachVO.getCoachName());
                    mainTvName.setText(coachVO.getCoachName());
//                    coachBackground.setBackgroundResource(setDrawbleBackground(coachVO.getExerciseType()));
//                    tvJob.setText(coachVO.getJob());

                    Glide.with(getApplicationContext())
                            .load(coachVO.getProfileImg())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.placeholder)
                                    .bitmapTransform(new CropCircleTransformation(mContext))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .centerCrop())
                            .transition(withCrossFade())
                            .into(profileImg);



                    tvCareer.setText(coachVO.getCareer());
                    tvCertificate.setText(coachVO.getCertificate());
                    tvIntro.setText(coachVO.getIntro());
                    coachSetBackGround(coachVO.getExerciseType());
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<CoachVO> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");
            }
        });
    }

    private int setDrawbleBackground(int exerciseType) {
        int background = 0;

        switch (exerciseType) {
            case 1:
                //필라테스
                background = R.drawable.pilates_background;
                break;
            case 2:
                //요가
                background = R.drawable.pilates_background;
                break;
            case 3:
                //웨이트
                background = R.drawable.xml_colorbg_main;
                break;
            case 4:
                //발레핏
                background = R.drawable.xml_colorbg_main;
                break;
            case 5:
                //홈트레이닝
                background = R.drawable.xml_colorbg_main;
                break;
            case 6:
                // 크로스핏
                background = R.drawable.xml_colorbg_main;
                break;
            case 7:
                //체형교정
                background = R.drawable.xml_colorbg_main;
                break;
            case 8:
                //폴댄스
                background = R.drawable.xml_colorbg_main;
                break;

        }

        return background;

    }

    private void coachSetBackGround(int exerciseType) {

        switch (exerciseType) {
            // 필라테스
            case 1:
                break;
            // 요가
            case 2:
                break;
            // 웨이트
            case 3:
                break;
            // 발레핏
            case 4:
                break;
            // 홈트레이닝
            case 5:
                break;
            // 크로스핏
            case 6:
                break;
            // 체형교정
            case 7:
                break;
            // 폴댄스
            case 8:
                break;
        }


    }


    private void postBookmarkService() {
//        bookmark insert
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Map<String, Object> param = new HashMap<>();
//        {target_id}, #{target_type}, #{member_id}
        param.put("targetId", mCoachId);
        param.put("targetType", Constant.TargetType.COACH.value);
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        param.put("bookmarkYn", mFabBtn.isSelected() ? "N" : "Y");

        RestService service = retrofit.create(RestService.class);
        final Call<BookmarkVO> insertOrDeleteBookmarkRequest = service.insertOrDeleteBookmark(param);

        insertOrDeleteBookmarkRequest.enqueue(new Callback<BookmarkVO>() {
            @Override
            public void onResponse(Call<BookmarkVO> call, Response<BookmarkVO> response) {
                BookmarkVO bookmarkVO = response.body();
                // 서버 리턴값 - 북마크 설정: 1, 북마크 해제: 2
                if (bookmarkVO.getResultType() == 1) {
                    mFabBtn.setSelected(true);
                    mFabBtn.setImageResource(R.drawable.purple_star);
                    Toast.makeText(CoachInfomationActivity.this, "북마크 되었습니다.", Toast.LENGTH_SHORT).show();

                    // 총 북마크 업데이트 해줌
                    getCoachInfoRestService();
                } else if (bookmarkVO.getResultType() == 2) {
                    mFabBtn.setSelected(false);
                    mFabBtn.setImageResource(R.drawable.white_star);
                    Toast.makeText(CoachInfomationActivity.this, "북마크 해제 되었습니다.", Toast.LENGTH_SHORT).show();

                    // 총 북마크 업데이트 해줌
                    getCoachInfoRestService();
                } else {
                    Log.d(TAG, "onResponse: result: " + bookmarkVO.getResultType());
                }
            }

            @Override
            public void onFailure(Call<BookmarkVO> call, Throwable t) {
                Log.e(TAG, "onFailure: postVideoBookmarkService rest 통신실패");
            }
        });
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

