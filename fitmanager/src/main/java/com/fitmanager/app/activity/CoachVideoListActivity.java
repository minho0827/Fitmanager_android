package com.fitmanager.app.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.CoachVideoListAdapter;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoachVideoListActivity extends AppCompatActivity {
    private static FitProgressBar mProgressBar = new FitProgressBar();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private List<VideoVO> mVideoList = new ArrayList<>();
    private int mCoachId;
    private String mCoachName;

    Context mContext;
    CoachVideoListAdapter mCoachVideoListAdapter;
    private final static String TAG = "CoachVideoListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_history);
        ButterKnife.bind(this);
        getIntentInit();
        setToolbar();
        mCoachVideoListAdapter = new CoachVideoListAdapter(mContext, mVideoList);
        mRecyclerView.setAdapter(mCoachVideoListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(mCoachName + "\n강사 동영상 목록");
            mToolbar.setTitleTextColor(Color.WHITE);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        getCoachVideoListRestService();
    }

    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCoachId = bundle.getInt("coachId");
        mCoachName = bundle.getString("coachName");
        Log.d(TAG, "coachId: " + mCoachId);
        Log.d(TAG, "coachName: " + mCoachName);


    }


    private void getCoachVideoListRestService() {
        showProgress();
        Log.i(TAG, "getRestService start");
//        underscore된 json feild name을 camelcase로 mapping
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.i(TAG, "Retrofit addConverterFactory build()");


        RestService service = retrofit.create(RestService.class);
        final Call<List<VideoVO>> videoList = service.getVideoList(mCoachId);


        videoList.enqueue(new Callback<List<VideoVO>>() {

            @Override
            public void onResponse(Call<List<VideoVO>> call, Response<List<VideoVO>> response) {
                mVideoList = response.body();
                if (mVideoList == null) {
                    mVideoList = Collections.EMPTY_LIST;
                }
                Log.i(TAG,"=========================");
                Log.i(TAG, "video size: " + mVideoList.size());
                Log.i(TAG,"=========================");

                    mCoachVideoListAdapter.setData(mVideoList);
                    hideProgress();

            }

            @Override
            public void onFailure(Call<List<VideoVO>> call, Throwable t) {
                Log.e(TAG, "ERROR: " + t.getMessage());
                hideProgress();
            }
        });

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

