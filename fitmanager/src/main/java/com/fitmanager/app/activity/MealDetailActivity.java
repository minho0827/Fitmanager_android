package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fitmanager.app.R;
import com.fitmanager.app.model.BookmarkVO;
import com.fitmanager.app.model.MealVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.BookmarkClickListener;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MealDetailActivity extends AppCompatActivity implements BookmarkClickListener {
    private static FitProgressBar mProgressBar = new FitProgressBar();
    MealVO mMealData = null;
    private Context mContext;
    final static String TAG = "MealDetailActivity";
    private int mMealId;
    private boolean mMealBookmarked = false;


    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.tv_mealtype)
    TextView tvMealtype;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_detail_mealview)
    ImageView mMealImgView;
    @BindView(R.id.btn_comment)
    ImageButton imgbtnComment;
    @BindView(R.id.comment_count)
    TextView tvCmtCount;
    @BindView(R.id.btn_bookmark)
    ImageButton btnBookmark;
    @BindView(R.id.bookmark_count)
    TextView tvBookmarkCount;
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.tv_content)

    TextView tvContent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_meal);
        ButterKnife.bind(this);
        getIntentInit();
        linearLayout.setVisibility(View.GONE);
        imgbtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCommentActivity(mMealId);

            }
        });

        btnBookmark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (LoginUtils.isLoggedIn()) {
                    mealOnBookmarkClicked(!mMealBookmarked, mMealId);
                } else {
                    Utils.hideKeyboard(v, mContext);
                    LoginUtils.showLoginDialog(mContext);
                }
            }
        });
    }

    private void postBookmarkService(boolean isSelected, int mealId) {

        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Map<String, Object> param = new HashMap<>();
//        {target_id}, #{target_type}, #{member_id}
        param.put("targetId", mMealId);
        param.put("targetType", Constant.TargetType.MEAL.value);
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        param.put("bookmarkYn", isSelected ? "Y" : "N");

        RestService service = retrofit.create(RestService.class);
        final Call<BookmarkVO> insertOrDeleteBookmarkRequest = service.insertOrDeleteBookmark(param);

        insertOrDeleteBookmarkRequest.enqueue(new Callback<BookmarkVO>() {
            @Override
            public void onResponse(Call<BookmarkVO> call, Response<BookmarkVO> response) {
                BookmarkVO bookmarkVO = response.body();
                // 서버 리턴값 - 북마크 설정: 1, 북마크 해제: 2
                if (bookmarkVO != null) {
                    String msg;
                    if (bookmarkVO.getResultType() == 1) {
                        msg = "북마크 되었습니다.";
                        setMealBookmarkResultData(true);
                    } else {
                        msg = "북마크 해제 되었습니다";
                        setMealBookmarkResultData(false);
                    }
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    updateBookmarkCount(bookmarkVO.getBookmarkCount());
                    hideProgress();
//                    getRestIsBookmarkService();
//                    updateBookmarkCount(bookmarkVO.getBookmarkCount());

                }
            }

            @Override
            public void onFailure(Call<BookmarkVO> call, Throwable t) {
                Log.e(TAG, "onFailure: rest 통신실패");
                hideProgress();
            }
        });
    }


    private void callCommentActivity(int mealId) {
        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        Bundle extras = new Bundle();
        Log.i(TAG, "targetId" + mealId);
        extras.putInt("targetId", mealId);
        extras.putInt("targetType", Constant.TargetType.MEAL.value);
        intent.putExtras(extras);
        startActivity(intent);
    }


    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mMealId = bundle.getInt("targetId");
        Log.d(TAG, "targetId : " + mMealId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMealInfoRestService(false);
        getRestIsBookmarkService();
    }

    private void getRestIsBookmarkService() {
        showProgress();
        if (LoginUtils.isLoggedIn() == true) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.SERVER_ADDR)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Map<String, Object> param = new HashMap<>();
            param.put("targetId", mMealId);
            param.put("targetType", Constant.TargetType.MEAL.value);
            param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());

            RestService service = retrofit.create(RestService.class);
            final Call<Integer> getIsBookmarkCall = service.getIsBookmark(param);

            getIsBookmarkCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    int bookmarkCount = response.body();
                    if (bookmarkCount > 0) {
                        setMealBookmarkResultData(true);
                    } else {
                        setMealBookmarkResultData(false);
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    hideProgress();
                    Log.e(TAG, "onFailure: rest 통신실패");
                }
            });
        }
    }

    /**
     * 북마크 설정/해제시 마다 호출하여 상태를 업데이트 해준다.
     *
     * @param isBookmakred 북마크 되었는지 여부 true or false
     */
    private void setMealBookmarkResultData(boolean isBookmakred) {
        mMealBookmarked = isBookmakred;
        if (isBookmakred) {
            btnBookmark.setBackgroundResource(R.drawable.bookmark_purple);
        } else {
            btnBookmark.setBackgroundResource(R.drawable.bookmark_star);
        }
    }


    private void getMealInfoRestService(final boolean isBookmarkUpdate) {
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<MealVO> mealVO = service.getMealInfo(mMealId);

        mealVO.enqueue(new Callback<MealVO>() {
            @Override
            public void onResponse(Call<MealVO> call, Response<MealVO> response) {
                final MealVO mealVO = response.body();
                if (mealVO != null) {
                    Log.d(TAG, "mealVO : " + mealVO);
                    if (isBookmarkUpdate) {
                        setData(mealVO);
                    } else {
                        mMealData = mealVO;
                        mMealId = mMealData.getMealId();
                        setData(mealVO);


                        linearLayout.setVisibility(View.VISIBLE);
                        Log.d(TAG, "mealVO : " + mealVO);
                        tvMealtype.setBackgroundResource(Utils.getMealType(mealVO.getType()));
                        tvMealtype.setText(Utils.getMealTypeText(mealVO.getType()));
                        tvTitle.setText(mealVO.getTitle());
                        tvCmtCount.setText(mealVO.getCommentCount() + "");
                        tvBookmarkCount.setText(mealVO.getBookmarkCount() + "");
                        tvContent.setText(mealVO.getContent());


                        Glide.with(getApplicationContext())
                                .load(mealVO.getImageUrl())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.placeholder)
                                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .centerCrop())
                                .transition(withCrossFade())
                                .into(mMealImgView);

                        Glide.with(getApplicationContext())
                                .load(mealVO.getProfileImg())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.placeholder)
                                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .centerCrop())
                                .transition(withCrossFade())
                                .into(imgProfile);
                    }

                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<MealVO> call, Throwable t) {
                hideProgress();
                t.printStackTrace();
            }
        });
    }

    private void setData(MealVO mealVO) {
        if (mealVO != null) {
            mMealData = mealVO;
        }
    }


    public void updateBookmarkCount(int bookmarkCount) {
        getData().setBookmarkCount(bookmarkCount);
        tvBookmarkCount.setText(bookmarkCount + "");
    }

    private MealVO getData() {
        return mMealData;
    }


    @Override
    public void mealOnBookmarkClicked(boolean isSelected, int mealId) {
        postBookmarkService(isSelected, mealId);

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




