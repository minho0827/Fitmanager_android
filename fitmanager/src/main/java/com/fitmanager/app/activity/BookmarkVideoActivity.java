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
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.BookmarkVideoAdapter;
import com.fitmanager.app.model.BookmarkVO;
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

public class BookmarkVideoActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private List<VideoVO> mBookmarkVideoList = new ArrayList<>();
    RecyclerView mRecyclerView;
    Context mContext;
    BookmarkVideoAdapter mBookmarkVideoAdapter;
    private final static String TAG = "BookmarkVideoActivity";
    private static FitProgressBar mProgressBar = new FitProgressBar();

    @Override
    protected void onResume() {
        super.onResume();
        HashMap map = new HashMap();
        map.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        getBookmarkVideoListRequestService(map);
    }

    private void getBookmarkVideoListRequestService(Map<String, Object> param) {
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<List<VideoVO>> getBookmarkVideoListCall = service.getBookmarkVideoList(param);

        getBookmarkVideoListCall.enqueue(new Callback<List<VideoVO>>() {
            @Override
            public void onResponse(Call<List<VideoVO>> call, Response<List<VideoVO>> response) {

                mBookmarkVideoList = response.body();

                if (mBookmarkVideoList == null) {
                    mBookmarkVideoList = Collections.EMPTY_LIST;
                }
                mBookmarkVideoAdapter.setData(mBookmarkVideoList);
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<VideoVO>> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_bookmark_video);
        setLayout();
        mBookmarkVideoAdapter = new BookmarkVideoAdapter(mContext, mBookmarkVideoList);
        mRecyclerView.setAdapter(mBookmarkVideoAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }



    private void deleteVideoRequestService(Map<String, Object> param, final int position) {
        showProgress();
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
                    mBookmarkVideoAdapter.removeItemAt(position);
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");

            }

        });
    }



    private void setLayout() {

        mRecyclerView = findViewById(R.id.recyclerview);
        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("북마크 비디오");
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



    public void postVideoBookmarkService(boolean isSelected, int videoId) {
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final Map<String, Object> param = new HashMap<>();
//        {target_id}, #{target_type}, #{member_id}
        param.put("targetId", videoId);
        param.put("targetType", Constant.TargetType.VIDEO.value);
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        param.put("bookmarkYn", isSelected ? "Y" : "N");

        Log.d(TAG, "postVideoBookmarkService: param: " + param);

        RestService service = retrofit.create(RestService.class);
        final Call<BookmarkVO> insertOrDeleteBookmarkRequestCall = service.insertOrDeleteBookmark(param);

        insertOrDeleteBookmarkRequestCall.enqueue(new Callback<BookmarkVO>() {
            @Override
            public void onResponse(Call<BookmarkVO> call, Response<BookmarkVO> response) {
                BookmarkVO bookmarkVO = response.body();
                if (bookmarkVO != null) {
                    // 서버 리턴값 - 북마크 설정: 1, 북마크 해제: 2
                    String msg;
                    if (bookmarkVO.getResultType() == 1) {
//                        mBookmarkVideoAdapter.setVideoBookmarkResultData(true);
                        msg = "북마크 되었습니다.";
                    } else {
                        mBookmarkVideoAdapter.setVideoBookmarkResultData(false);

                        msg = "북마크 해제 되었습니다.";
                    }
                    hideProgress();
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookmarkVO> call, Throwable t) {
                Log.e(TAG, "onFailure: rest 통신실패");
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
