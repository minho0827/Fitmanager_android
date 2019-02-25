package com.fitmanager.app.fragment;

import android.content.Context;
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
import com.fitmanager.app.adapter.MealsListAdapter;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.model.MealVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealFragment extends Fragment {
    private static FitProgressBar mProgressBar = new FitProgressBar();
    private MealsListAdapter mMealsListAdapter;
    private static final String TAG = "MealFragment";
    private List<MealVO> mMealsItems = new ArrayList<>();
    Context mContext;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    static final boolean GRID_LAYOUT = false;
    private static int mCoachId;
    private CoachVO mCoachVO = new CoachVO();
    private SwipeRefreshLayout swipeContainer;


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "");
        getMealListRestService();
    }

    private void getMealListRestService() {
        showProgress();
        Log.i(TAG, "getMealListRestService start");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.i(TAG, "Retrofit addConverterFactory build()");

        RestService service = retrofit.create(RestService.class);
        final Call<List<MealVO>> coachList = service.getMealList(mCoachId);
        coachList.enqueue(new Callback<List<MealVO>>() {

            @Override
            public void onResponse(Call<List<MealVO>> call, Response<List<MealVO>> response) {
                swipeContainer.setRefreshing(false);
                List<MealVO> resultList = response.body();
                if (resultList != null) {
                    mMealsItems = resultList;
                    Log.i(TAG, "meal size: " + mMealsItems.size());
                    Log.i(TAG, "mMealsItems : " + mMealsItems);
                    mMealsListAdapter.setData(mMealsItems);
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<List<MealVO>> call, Throwable t) {
                Log.e(TAG, "ERROR: onFailure");
                swipeContainer.setRefreshing(false);
                hideProgress();
            }
        });
    }

    public void setCoachVO(CoachVO coachVO) {
        this.mCoachVO = coachVO;
        if (mMealsListAdapter != null) {
            mMealsListAdapter.setCoachVO(coachVO);
        }
    }

    public static MealFragment createInstance(int coachId) {
        MealFragment mealFragment = new MealFragment();
        mCoachId = coachId;
        Log.i(TAG, "coachId: " + coachId);

        return mealFragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();


    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        swipeContainer = view.findViewById(R.id.swipeContainer);


        if (GRID_LAYOUT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //Use this now
        mMealsListAdapter = new MealsListAdapter(getActivity(), mMealsItems);
        mRecyclerView.setAdapter(mMealsListAdapter);
        Log.i(TAG, "setAdapter");

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getMealListRestService();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meals_recyclerview, container, false);
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