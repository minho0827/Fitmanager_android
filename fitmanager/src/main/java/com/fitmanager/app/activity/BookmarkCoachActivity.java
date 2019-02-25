package com.fitmanager.app.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.BookmarkCoachAdapter;
import com.fitmanager.app.model.CoachViewHistoryVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookmarkCoachActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private List<CoachViewHistoryVO> mCoachBookmarkList = new ArrayList<>();
    RecyclerView mRecyclerView;
    Context mContext;
    BookmarkCoachAdapter mBookmarkCoachAdapter;
    private final static String TAG = "BookmarkCoachActivity";
    private static FitProgressBar mProgressBar = new FitProgressBar();

    @Override
    protected void onResume() {
        super.onResume();
        getBookmarkCoachListRequestService();
    }

    private void getBookmarkCoachListRequestService() {
        showProgress();
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<List<CoachViewHistoryVO>> bookmarkCoachHistoryCall =
                service.getBookmarkCoachListRequestService(param);

        bookmarkCoachHistoryCall.enqueue(new Callback<List<CoachViewHistoryVO>>() {
            @Override
            public void onResponse(Call<List<CoachViewHistoryVO>> call, Response<List<CoachViewHistoryVO>> response) {

                mCoachBookmarkList = response.body();
                if (!Utils.isEmpty(mCoachBookmarkList)) {

                    mBookmarkCoachAdapter.setData(mCoachBookmarkList);

                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<CoachViewHistoryVO>> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");

            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_bookmark_coach);
        setLayout();
        mBookmarkCoachAdapter = new BookmarkCoachAdapter(mContext, mCoachBookmarkList);
        mRecyclerView.setAdapter(mBookmarkCoachAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void setLayout() {

        mRecyclerView = findViewById(R.id.recyclerview);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("북마크 코치");
        mToolbar.setTitleTextColor(Color.WHITE);


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
    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.show(this);
        }
    }


    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
    }
}
