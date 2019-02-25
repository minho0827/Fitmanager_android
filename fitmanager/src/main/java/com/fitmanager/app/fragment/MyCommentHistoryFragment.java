package com.fitmanager.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.MyCommentListAdapater;
import com.fitmanager.app.model.CommentHistoryVO;
import com.fitmanager.app.network.CommentRestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.LoginUtils;
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

public class MyCommentHistoryFragment extends Fragment {

    private MyCommentListAdapater mMyCommentListAdapter;
    private static final String TAG = "MyCommentHistoryFragment";
    private List<CommentHistoryVO> mCommentsItems = new ArrayList<>();

    Context mContext;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    static final boolean GRID_LAYOUT = false;
    private static int mMemberId;
    private SwipeRefreshLayout swipeContainer;


    @Override
    public void onResume() {
        super.onResume();
        getMyCommentHistoryListService();
    }



    public static MyCommentHistoryFragment createInstance(int memberId) {
        MyCommentHistoryFragment myCommentHistoryFragment = new MyCommentHistoryFragment();
        mMemberId = memberId;

        return myCommentHistoryFragment;

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
        mMyCommentListAdapter = new MyCommentListAdapater(getActivity(), mCommentsItems);
        mRecyclerView.setAdapter(mMyCommentListAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyCommentHistoryListService();
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

    private void getMyCommentHistoryListService() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Map<String, Object> param = new HashMap<>();
//        {target_id}, #{target_type}, #{member_id}
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<List<CommentHistoryVO>> getMyCommentHistoryListCall = service.getMyCommentHistoryList(param);

        getMyCommentHistoryListCall.enqueue(new Callback<List<CommentHistoryVO>>() {
            @Override
            public void onResponse(Call<List<CommentHistoryVO>> call, Response<List<CommentHistoryVO>> response) {
                swipeContainer.setRefreshing(false);
                mCommentsItems = response.body();
                if (mCommentsItems != null) {
                    mMyCommentListAdapter.setData(mCommentsItems);
                }
            }

            @Override
            public void onFailure(Call<List<CommentHistoryVO>> call, Throwable t) {
                Toast.makeText(mContext, "통신실패.", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });

    }


}
