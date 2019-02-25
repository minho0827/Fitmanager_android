package com.fitmanager.app.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.VideoHistoryAdapter;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.LoginUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoHistoryActivity extends AppCompatActivity implements VideoHistoryAdapter.VideoMoreBtnListener {
    private Toolbar mToolbar;
    private static FitProgressBar mProgressBar = new FitProgressBar();
    private List<VideoVO> mHistoryVideoList = new ArrayList<>();
    RecyclerView mRecyclerView;
    Context mContext;
    VideoHistoryAdapter mVideoHistoryAdapter;
    private final static String TAG = "VideoHistoryActivity";

    @Override
    protected void onResume() {
        super.onResume();

        HashMap map = new HashMap();
        map.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        showProgress();
        getVideoHistroyRequestService(map);
    }


    private void getVideoHistroyRequestService(Map<String, Object> param) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<List<VideoVO>> viddeoHistoryCall = service.getVideoHistoryList(param);

        viddeoHistoryCall.enqueue(new Callback<List<VideoVO>>() {
            @Override
            public void onResponse(Call<List<VideoVO>> call, Response<List<VideoVO>> response) {

                mHistoryVideoList = response.body();
                if (mHistoryVideoList == null) {
                    mHistoryVideoList = Collections.EMPTY_LIST;
                }
                mVideoHistoryAdapter.setData(mHistoryVideoList);
                hideProgress();

            }

            @Override
            public void onFailure(Call<List<VideoVO>> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");

            }

        });
    }

    public void deleteVideoRequestService(Map<String, Object> param, final int position) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<Integer> deleteViddeoHistoryCall = service.deleteVideoRequest(param);

        deleteViddeoHistoryCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                int result = response.body();
                if (result > 0) {
                    mVideoHistoryAdapter.removeItemAt(position);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");

            }

        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_video_history);
        setLayout();
        mVideoHistoryAdapter = new VideoHistoryAdapter(mContext, mHistoryVideoList);
        mRecyclerView.setAdapter(mVideoHistoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



    }


    private void setLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("기록");
        mToolbar.setTitleTextColor(Color.WHITE);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void videoMoreBtnClicked(Map<String, Object> param, int position) {
        deleteVideoRequestService(param, position);

    }


    private void showProgress() {
        if (mProgressBar != null) {
            Log.d(TAG, "showProgress()!!!");
            mProgressBar.show(mContext);
        }
    }


    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
    }
}