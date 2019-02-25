//package com.fitmanager.app.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.fitmanager.app.model.MealVO;
//
//public class MealImagePagerFragment extends android.support.v4.app.Fragment {
//    private Context mContext;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    public static MealDetailSwipeFragment newInstance(MealVO.MealImagesVO mealVO, int index) {
//        MealImagePagerFragment fragment = new MealImagePagerFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("mealVO", mealVO);
//        args.putInt("index", index);
//
//        fragment.setArguments(args);
//
//        return fragment;
//    }
//}
