package com.fitmanager.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.VideoListAdapter;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.model.VideoVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoFragment extends Fragment {

    private VideoListAdapter mVideoAdapter;
    private static final String TAG = "VideoFragment";
    CoachVO mCoachVO = new CoachVO();
    private List<VideoVO> mVideoItems = new ArrayList<>();

    Context mContext;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    static final boolean GRID_LAYOUT = false;
    boolean isFavorite = false;
    private static int mCoachId;
    LinearLayout mLinearLayout;
    FrameLayout mFrameLayout;
    TextView emptyView;


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "");
        getRestService();

    }


    private void getRestService() {
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
                mVideoItems = response.body();
                if (mVideoItems == null) {
                    mVideoItems = Collections.EMPTY_LIST;
                }
                Log.i(TAG, "video size: " + mVideoItems.size());
                Log.i(TAG, "mVideoItems : " + mVideoItems);

                if (mVideoItems.size() == 0) {
                    Toast.makeText(mContext, "등록된 영상이없습니다", Toast.LENGTH_SHORT).show();
//                    emptyView.setVisibility(View.VISIBLE);
//                    emptyView.setText("현재 등록된 동영상이 없습니다");

                } else {
//                    emptyView.setVisibility(View.GONE);
                    mVideoAdapter.setData(mVideoItems);

                }
            }

            @Override
            public void onFailure(Call<List<VideoVO>> call, Throwable t) {
                Log.e(TAG, "ERROR: " + t.getMessage());
            }
        });
    }


    public void setCoachVO(CoachVO coachVO) {
        this.mCoachVO = coachVO;
        if (mVideoAdapter != null) {
            mVideoAdapter.setCoachVO(coachVO);
        }
    }


    public static VideoFragment createInstance(int coachId) {
        VideoFragment videoFragment = new VideoFragment();
        mCoachId = coachId;
        Log.i("NKEKW", "coachId: " + coachId);

        return videoFragment;

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
        emptyView = view.findViewById(R.id.tv_empty);
        mLinearLayout = view.findViewById(R.id.linear_layout);
        mFrameLayout = view.findViewById(R.id.frame_layout);


        if (GRID_LAYOUT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //Use this now
        mVideoAdapter = new VideoListAdapter(getActivity(), mVideoItems);
//        mVideoAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mVideoAdapter);
        Log.i(TAG, "setAdapter");


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_video, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        return mRecyclerView;
    }


}
