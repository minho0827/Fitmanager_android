package com.fitmanager.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fitmanager.app.R;
import com.fitmanager.app.activity.BookmarkCoachActivity;
import com.fitmanager.app.activity.BookmarkMealListActivity;
import com.fitmanager.app.activity.BookmarkVideoActivity;
import com.fitmanager.app.activity.InsertMealActivity;
import com.fitmanager.app.activity.LoginActivity;
import com.fitmanager.app.activity.MyCommentHistoryActivity;
import com.fitmanager.app.activity.ProfileEditActivity;
import com.fitmanager.app.activity.VideoActivity;
import com.fitmanager.app.activity.VideoHistoryActivity;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.ImageUtils;
import com.fitmanager.app.util.LoginUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";
    ImageView mProfileImg;
    TextView mName, mEmail;
    LinearLayout layoutLoggedIn;
    LinearLayout linearHistory;
    LinearLayout linearVideo;
    LinearLayout linearMeal;
    LinearLayout linearCoach;
    LinearLayout linearComment;
    FloatingActionMenu floatingMenuBtn;
    com.github.clans.fab.FloatingActionButton fbMealInsert, fbVideoInsert;
    private static FitProgressBar mProgressBar = new FitProgressBar();

    FrameLayout layoutLoggedOut;
    Button btnLogin;

    Context mContext;


    public static Fragment newInstance() {
        return new MyPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mypage_list, container, false);


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();


    }


    public static MyPageFragment createInstance() {
        MyPageFragment mealFragment = new MyPageFragment();

        return mealFragment;

    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        floatingMenuBtn = view.findViewById(R.id.floating_menu_btn);
        fbMealInsert = view.findViewById(R.id.fb_meal_insert);
        fbVideoInsert = view.findViewById(R.id.fb_video_insert);

        mProfileImg = view.findViewById(R.id.img_profile);
        mName = view.findViewById(R.id.name);
        btnLogin = view.findViewById(R.id.btn_login);
        mEmail = view.findViewById(R.id.email);
        layoutLoggedIn = view.findViewById(R.id.layout_loggedin);
        linearHistory = view.findViewById(R.id.linear_history);
        linearVideo = view.findViewById(R.id.linear_bookmark_video);
        linearMeal = view.findViewById(R.id.linear_bookmark_meal);
        linearCoach = view.findViewById(R.id.linear_bookmark_coach);
        linearComment = view.findViewById(R.id.linear_comment);
        layoutLoggedOut = view.findViewById(R.id.layout_loggedout);


        fbMealInsert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InsertMealActivity.class);
                startActivity(intent);
            }
        });
        fbVideoInsert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        // 기록
        linearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(new Intent(mContext, VideoHistoryActivity.class));


            }
        });

        // 북마크 비디오
        linearVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookmarkVideoActivity.class);
                startActivity(intent);

            }
        });
        //북마크 식단
        linearMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, BookmarkMealListActivity.class));


            }
        });
        //북마크 코치
        linearCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, BookmarkCoachActivity.class));


            }
        });

        //내가 단 댓글
        linearComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyCommentHistoryActivity.class));


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                startActivity(intent);

            }
        });


    }


    /* 일반 회원이면 floatingbtn GONE 코치면 VISIBLE */
    private void floatingBtnGoneVisible() {

        /* 코치 일경우 */
        if (LoginUtils.isLoggedIn() &&
                LoginUtils.getLoginUserVO().getMemberType() == 2) {
            floatingMenuBtn.setVisibility(View.VISIBLE);
        } else {
            floatingMenuBtn.setVisibility(View.GONE);
        }
    }

    private void callActivity() {
        Intent intent = new Intent(mContext, VideoActivity.class);
        Bundle extras = new Bundle();
        intent.putExtras(extras);
        mContext.startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (LoginUtils.isLoggedIn() == true) {
            getLoginUserInfoService();
            MemberVO memberVO = LoginUtils.getLoginUserVO();

            ImageUtils.getProfileImage(mContext,
                    mProfileImg,
                    memberVO.getProfileImg()
            );

            mEmail.setText(LoginUtils.getLoginUserVO().getEmail() + "");
            mName.setText(memberVO.getName());

            layoutLoggedIn.setVisibility(View.VISIBLE);
            layoutLoggedOut.setVisibility(View.GONE);
        } else {
            mProfileImg.setImageResource(R.drawable.person);
            mName.setText("로그인");
            layoutLoggedIn.setVisibility(View.GONE);
            layoutLoggedOut.setVisibility(View.VISIBLE);
        }
        Log.i(TAG, "onResume");
        floatingBtnGoneVisible();

    }

    private void getLoginUserInfoService() {
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
        final Call<MemberVO> memberVOCall = service.getLoginUserInfoService(param);

        memberVOCall.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                final MemberVO memberVO = response.body();
                if (memberVO != null) {

                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");
            }
        });
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