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
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.activity.CoachAcitivty;
import com.fitmanager.app.activity.CoachListFilterActivity;
import com.fitmanager.app.activity.MainActivity;
import com.fitmanager.app.adapter.CoachListAdapter;
import com.fitmanager.app.model.CoachVO;
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

public class HomeFragment extends Fragment implements CoachListAdapter.EndlessScrollListener,
        CoachListAdapter.CoachListListener {

    static final boolean GRID_LAYOUT = false;
    private RecyclerView mRecyclerView;
    private CoachListAdapter mCoachProfileAdapter;
    private static final String TAG = "HomeFragment";
    private List<CoachVO> mCoachProfileItems = new ArrayList<>();
    Context mContext;
    LinearLayoutManager mLayoutManager;
    private final int ITEM_REFRESH_SIZE = 5;
    private ArrayList<String> mFilterList;
    private String mGender = "";
    private SwipeRefreshLayout mSwipeContainer;
    private static FitProgressBar mProgressBar = new FitProgressBar();

    public static final int FILTER = 0;
    private MainActivity mActivity;
    private boolean loadingDataOnActivityResult = false;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();

        return inflater.inflate(R.layout.fragment_recyclerview, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    private void getData() {
        getRestCoachTotalCount();
        getCoachListRestService();
    }

    public void callCoachActivity(int coachId) {
        Log.i(TAG, "coachId: " + coachId);
        Intent intent = new Intent(getActivity(), CoachAcitivty.class);
        Bundle extras = new Bundle();
        extras.putInt("coachId", coachId);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER) {
            if (Activity.RESULT_OK == resultCode) {
                loadingDataOnActivityResult = true;
                mFilterList = data.getStringArrayListExtra("WORK_KIND_FILTER");
                mGender = StringUtils.trimToEmpty(data.getStringExtra("GENDER"));
                clearList();
                getData();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void clearList() {
        mCoachProfileItems.clear();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSwipeContainer = view.findViewById(R.id.swipeContainer);


        if (GRID_LAYOUT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mCoachProfileAdapter = new CoachListAdapter(this, mCoachProfileItems, CoachListAdapter.ADAPTER_TYPE_FILTER);
        mRecyclerView.setAdapter(mCoachProfileAdapter);
        Log.i(TAG, "setAdapter");

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getCoachListRestService();
                getRestCoachTotalCount();
            }
        });
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void callCoachFilterActivity() {
        Intent intent = new Intent(getActivity(), CoachListFilterActivity.class);
        intent.putExtra("gender", mGender);
        intent.putStringArrayListExtra("filter_list", mFilterList);
        startActivityForResult(intent, FILTER);
    }

    @Override
    public void callSearchActivity() {
//        Intent intent = new Intent(getActivity(), SearchActivity.class);
//        startActivity(intent);
    }

    private void getRestCoachTotalCount() {
        showProgress();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        String filter = getFilter();
        final Call<Integer> coachCount = service.getCoachCount(getGenderArgs(), filter);

        coachCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                mSwipeContainer.setRefreshing(false);
                Integer resultInt = response.body();
                if (resultInt != null) {
                    mCoachProfileAdapter.setTotalCoachCount(resultInt);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(mContext, "Error occurred while getting total cound.", Toast.LENGTH_SHORT).show();
                mSwipeContainer.setRefreshing(false);
                hideProgress();

            }


        });

    }

    private void getCoachListRestService() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        int start = mCoachProfileItems.size();
        int end = ITEM_REFRESH_SIZE;

        RestService service = retrofit.create(RestService.class);
        String filter = getFilter();
        final Call<List<CoachVO>> coachList = service.getCoachList(start, end, getGenderArgs(), filter);

        coachList.enqueue(new Callback<List<CoachVO>>() {
            @Override
            public void onResponse(Call<List<CoachVO>> call, Response<List<CoachVO>> response) {
                mSwipeContainer.setRefreshing(false);
                List<CoachVO> resultList = response.body();
                if (!Utils.isEmpty(resultList)) {
                    mCoachProfileItems.addAll(resultList);
                }
                mCoachProfileAdapter.setData(mCoachProfileItems);
                hideProgress();

            }


            @Override
            public void onFailure(Call<List<CoachVO>> call, Throwable t) {
                Log.e(TAG, "## onFailure: ");
                mSwipeContainer.setRefreshing(false);
                hideProgress();

            }
        });
    }

    /**
     * 필터창에서 넘어온값이 남, 여 모두 체크되어 넘어올때는 "12" 로 넘어오지만, 서버에서 쿼리할때는 "" 로 넘겨줌.
     * mGender 에서는 필터창에 다시 들어갔을때, 남, 여 버튼체크 상태를 유지해야 하므로,
     * 내부적으로는 "12" 값을 유지한다. Rest 통신시에만 남, 여 모두 체크일때 "" 값을 넘겨준다.
     *
     * @return 서버에서 사용 할 성별 버튼이 체크된 값
     */
    private String getGenderArgs() {
        return "12".equals(mGender) ? "" : mGender;
    }

    private String getFilter() {
        if (Utils.isEmpty(mFilterList)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : mFilterList) {
                sb.append(s).append(",");
            }
            return sb.toString();
        }
    }

    @Override
    public boolean onLoadMore(int position) {
        Log.d(TAG, "onLoadMore: getTotalCoachCount(): " + mCoachProfileAdapter.getTotalCoachCount());
        Log.d(TAG, "onLoadMore: position: " + position);

        if (mCoachProfileAdapter.getTotalCoachCount() != position) {
            getData();
            return true;
        } else {
            return false;
        }
    }


    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.show(getActivity());
        }
    }


    private void hideProgress() {
        Log.d(TAG, "hideProgress: ");

        if (mProgressBar != null) {
            Log.d(TAG, "hideProgress: not null");
            mProgressBar.hide();
        }
    }


    @Override
    public Context getCoachContext() {
        return mContext;
    }
}
