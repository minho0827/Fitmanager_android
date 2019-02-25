package com.fitmanager.app.fragment;//package com.fitmanager.app.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.fitmanager.app.R;
//import com.fitmanager.app.adapter.VideoInfoAdaper;
//import com.fitmanager.app.model.VideoVO;
//
//public class ExerciseInfoFragment extends Fragment {
//
//    private VideoInfoAdaper mExerciseInfoAdaper;
//    private static final String TAG = "MealFragment";
//
//    Context mContext;
//    RecyclerView mRecyclerView;
//    LinearLayoutManager mLayoutManager;
//    static final boolean GRID_LAYOUT = false;
//    boolean isFavorite = false;
//    private VideoVO mVideoVO = null;
//    private SwipeRefreshLayout swipeContainer;
//
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//
//    public void setVideoVO(VideoVO videoVO) {
//        this.mVideoVO = videoVO;
//        mExerciseInfoAdaper.setVideoVO(videoVO);
//    }
//
//    public static ExerciseInfoFragment createInstance(int videoId) {
//        ExerciseInfoFragment mealFragment = new ExerciseInfoFragment();
//        return mealFragment;
//
//    }
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext = getActivity();
//
//
//
//
//    }
//
//    @Override
//    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
//
//
//        if (GRID_LAYOUT) {
//            mLayoutManager = new GridLayoutManager(getActivity(), 2);
//        } else {
//            mLayoutManager = new LinearLayoutManager(getActivity());
//        }
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
//        //Use this now
//        mExerciseInfoAdaper = new VideoInfoAdaper(getActivity(), mVideoVO);
//        mRecyclerView.setAdapter(mExerciseInfoAdaper);
//        Log.i(TAG, "setAdapter");
//
//    }
//
//
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_meals_recyclerview, container, false);
//    }
//}