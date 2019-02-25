package com.fitmanager.app.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fitmanager.app.R;
import com.fitmanager.app.activity.PopularityVideoFilterActivity;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.adapter.PopularityAdapter;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PopularityVideoFragment extends Fragment implements PopularityAdapter.EndlessScrollListener {
    private static FitProgressBar mProgressBar = new FitProgressBar();
    private SwipeRefreshLayout mSwipeContainer;
    public static final int FILTER = 0;
    static final boolean GRID_LAYOUT = false;
    private RecyclerView mRecyclerVIew;
    private PopularityAdapter mPopAdaper;
    private static final String TAG = "PopularityVideoFragment";
    private List<VideoVO> mPopularityItems = new ArrayList<>();
    private ArrayList<String> mExerciseTypeList;
    private ArrayList<String> mBodypartList;
    private String mOrder = "";
    private String mGender = "";
    Context mContext;
    private final int ITEM_REFRESH_SIZE = 5;
    LinearLayoutManager mLayoutManager;
    private boolean loadingDataOnActivityResult = false;


    public static PopularityVideoFragment newInstance() {
        return new PopularityVideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        getData();


    }

    private void getData() {
        getPopularityVideoCount();
        getRestPopularityListService();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER) {
            if (Activity.RESULT_OK == resultCode) {
                mExerciseTypeList = data.getStringArrayListExtra("exerciseType");
                mBodypartList = data.getStringArrayListExtra("bodypart");
                mGender = StringUtils.trimToEmpty(data.getStringExtra("gender"));
                mOrder = StringUtils.trimToEmpty(data.getStringExtra("order"));
                Log.d(TAG, "mGender : " + mGender);
                Log.d(TAG, "mOrder : " + mOrder);
                Log.d(TAG, "mBodypartList : " + mBodypartList);
                clearPopularityItemList();
                getData();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void clearPopularityItemList() {
        mPopularityItems.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 필터 적용하여 onActivityResult 를 거치지 않았을 경우에만 실행.
        if (loadingDataOnActivityResult == false) {
            getData();
        }
        loadingDataOnActivityResult = false;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerVIew = view.findViewById(R.id.recyclerView);
        mSwipeContainer = view.findViewById(R.id.swipeContainer);
        if (GRID_LAYOUT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerVIew.setLayoutManager(mLayoutManager);
        mRecyclerVIew.setHasFixedSize(true);
        mPopAdaper = new PopularityAdapter(this, mPopularityItems);
        mRecyclerVIew.setAdapter(mPopAdaper);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearPopularityItemList();
                getData();
            }
        });
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

//    검색 액티비티로 이동
    public void callSearchActivity() {
//        Intent intent = new Intent(getActivity(), SearchActivity.class);
//        startActivity(intent);
    }


    public void callVideoActivity(int videoId) {
        Log.i(TAG, "videoId: " + videoId);
        Intent intent = new Intent(getActivity(), VideoActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("targetId", videoId);
        intent.putExtras(extras);
        startActivity(intent);
    }


    private void getPopularityVideoCount() {
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        int start = mPopularityItems.size();
        int end = ITEM_REFRESH_SIZE;

        RestService service = retrofit.create(RestService.class);
        String exerciseType = getExerciseType();
        String order = mOrder;
        String gender = getGenderArgs();
        String bodypart = getBodypart();

        final Call<Integer> getPopularityVideoCountCall =
                service.getPopularityVideoCount(start, end, gender, exerciseType, bodypart, order);

        getPopularityVideoCountCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer resultInt = response.body();
                if (resultInt != null) {
                    mPopAdaper.setTotalVideoCount(resultInt);
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "ERROR: onFailure");

            }
        });

    }

    private void getRestPopularityListService() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        int start = mPopularityItems.size();
        int end = ITEM_REFRESH_SIZE;

        Log.d(TAG, "start: " + start + ", end: " + end);

        RestService service = retrofit.create(RestService.class);
        String exerciseType = getExerciseType();
        String order = mOrder;
        String gender = getGenderArgs();
        String bodypart = getBodypart();

        Log.d(TAG, "exerciseType: " + exerciseType);
        Log.d(TAG, "order: " + order);
        Log.d(TAG, "gender: " + gender);
        Log.d(TAG, "bodypart: " + bodypart);

        final Call<List<VideoVO>> popularityList = service.getPopulartyList
                (start, end, gender, exerciseType, bodypart, order);

        popularityList.enqueue(new Callback<List<VideoVO>>() {
            @Override
            public void onResponse(Call<List<VideoVO>> call, Response<List<VideoVO>> response) {
                mSwipeContainer.setRefreshing(false);
                List<VideoVO> resultList = response.body();
                if (!Utils.isEmpty(resultList)) {
                    mPopularityItems.addAll(resultList);
                }
                mPopAdaper.setData(mPopularityItems);
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<VideoVO>> call, Throwable t) {
                Log.e(TAG, "ERROR: onFailure");
                mSwipeContainer.setRefreshing(false);
                hideProgress();

            }
        });
    }

    private String getGenderArgs() {
        return "12".equals(mGender) ? "" : mGender;
    }

    private String getExerciseType() {
        if (Utils.isEmpty(mExerciseTypeList)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : mExerciseTypeList) {
                sb.append(s).append(",");
            }
            return sb.toString();
        }
    }

    private String getBodypart() {
        if (Utils.isEmpty(mBodypartList)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : mBodypartList) {
                sb.append(s).append(",");
            }
            return sb.toString();
        }
    }

    @Override
    public boolean onLoadMore(int position) {
        Log.d(TAG, "onLoadMore: getTotalVideoCount(): " + mPopAdaper.getTotalVideoCount());
        Log.d(TAG, "onLoadMore: position: " + position);

        if (mPopAdaper.getTotalVideoCount() > position) {
            getData();
            return false;
        }
        return true;
    }

    public void callBodyTypeFilterActivity() {
        Intent intent = new Intent(getActivity(), PopularityVideoFilterActivity.class);
        intent.putExtra("gender", mGender);
        intent.putExtra("order", mOrder);
        intent.putStringArrayListExtra("exerciseType", mExerciseTypeList);
        intent.putStringArrayListExtra("bodypart", mBodypartList);
        startActivityForResult(intent, FILTER);
    }


    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.show(getActivity());
        }
    }


    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
    }
}
