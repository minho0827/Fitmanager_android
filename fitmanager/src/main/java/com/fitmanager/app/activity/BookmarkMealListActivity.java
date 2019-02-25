package com.fitmanager.app.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.BookmarkMealListAdapter;
import com.fitmanager.app.model.MealVO;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/* 북마크한 식단 */
public class BookmarkMealListActivity extends AppCompatActivity implements BookmarkMealListAdapter.OnCheckedListener {


    @BindView(R.id.frame_layout)
    FrameLayout frame_layout;
    @BindView(R.id.frame_layout_edit)
    FrameLayout frameLayoutEdit;
    @BindView(R.id.tv_count)
    AppCompatTextView tvCount;
    @BindView(R.id.linearLayout_btn)
    LinearLayout linearLayoutBtn;
    @BindView(R.id.tv_edit)
    AppCompatTextView tvEdit;
    @BindView(R.id.icon_delete)
    ImageButton iconDelete;
    @BindView(R.id.frame_layout_select)
    FrameLayout frameLayoutAllSelect;
    @BindView(R.id.btn_all_select)
    ImageButton btnAllSelect;
    @BindView(R.id.tv_all_select)
    AppCompatTextView tvAllSelect;
    @BindView(R.id.tv_delete)
    AppCompatTextView tvDelete;

    @BindView(R.id.btn_complete)
    ImageButton btnComplete;
    @BindView(R.id.tv_complete)
    AppCompatTextView tvComplete;
    @BindView(R.id.tv_empty_info)
    AppCompatTextView tvEmptyInfo;
    @BindView(R.id.frame_layout_empty)
    FrameLayout frame_empty;
    @BindView(R.id.layout_all_select)
    View layoutAllSelect;


    private Toolbar mToolbar;
    private List<MealVO> mBookmarkMealList = new ArrayList<>();
    RecyclerView mRecyclerView;
    Context mContext;
    BookmarkMealListAdapter mBookmarkMealListAdapter;
    private final static String TAG = "BookmarkMealActivity";
    private GridLayoutManager lLayout;
    private static FitProgressBar mProgressBar = new FitProgressBar();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_bookmark_meal);
        ButterKnife.bind(this);
        setLayout();
        lLayout = new GridLayoutManager(BookmarkMealListActivity.this, 3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lLayout);
        mBookmarkMealListAdapter = new BookmarkMealListAdapter(mContext, mBookmarkMealList);
        mRecyclerView.setAdapter(mBookmarkMealListAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
        HashMap map = new HashMap();
        map.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        getBookmarkMealListRequestService(map);

    }

    private void getBookmarkMealListRequestService(Map<String, Object> param) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<List<MealVO>> mealBookmarkListCall = service.getBookmarkMealList(param);

        mealBookmarkListCall.enqueue(new Callback<List<MealVO>>() {
            @Override
            public void onResponse(Call<List<MealVO>> call, Response<List<MealVO>> response) {

                mBookmarkMealList = response.body();
                if(mBookmarkMealList == null) {
                    mBookmarkMealList = Collections.EMPTY_LIST;
                }
                mBookmarkMealListAdapter.setData(mBookmarkMealList);
                tvCount.setText("총" + mBookmarkMealList.size() + "개");

                if (mBookmarkMealList.size() == 0) {
                    frame_empty.setVisibility(View.VISIBLE);

                }else {
                    frame_empty.setVisibility(View.GONE);

                }
                hideProgress();
            }


            @Override
            public void onFailure(Call<List<MealVO>> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");

            }

        });
    }

    @OnClick(R.id.frame_layout_edit)
    void frameLayoutEditOnClick() {
        setEditMode(true);
    }

    // 완료
    @OnClick(R.id.btn_complete)
    void btnCompleteOnClick() {
        Log.i(TAG, "frameLayoutCompleteOnClick !!!!!!");
        deleteBookmarkMealListService();
    }

    // 완료
    @OnClick(R.id.tv_complete)
    void tvCompleteOnClick() {
        Log.i(TAG, "frameLayoutCompleteOnClick !!!!!!");
        deleteBookmarkMealListService();
    }

    //전체선택
    @OnClick(R.id.layout_all_select)
    void btnAllSelectOnClick() {
        layoutAllSelect.setSelected(!layoutAllSelect.isSelected());
        Log.d(TAG, "btnAllSelectOnClick: selected: " + layoutAllSelect.isSelected());
//        setEditMode(true);
        if (layoutAllSelect.isSelected()) {
            btnAllSelect.setBackgroundResource(R.drawable.checked_purple);
            mBookmarkMealListAdapter.selectAll(true);
        } else {
            btnAllSelect.setBackgroundResource(R.drawable.checked_gray);
            mBookmarkMealListAdapter.selectAll(false);
        }
        onItemChecked();
    }

    private void setEditMode(boolean editMode) {
        if (editMode) {
            frameLayoutEdit.setVisibility(View.GONE);
            frameLayoutAllSelect.setVisibility(View.VISIBLE);
        } else {
            frameLayoutEdit.setVisibility(View.VISIBLE);
            frameLayoutAllSelect.setVisibility(View.GONE);
            layoutAllSelect.setSelected(false);
        }
        mBookmarkMealListAdapter.setEditMode(editMode);
    }

    // 삭제
    @OnClick(R.id.icon_delete)
    void iconDeleteClicked() {
        List<MealVO> mealList = mBookmarkMealListAdapter.getSelectedItems();
        for (MealVO mealVO : mealList) {
            Log.d(TAG, "selected item: " + mealVO.getMealId() + ", " + mealVO.getTitle());
        }

    }

    // 삭제
    @OnClick(R.id.tv_delete)
    void tvDeleteClicked() {
        List<MealVO> mealList = mBookmarkMealListAdapter.getSelectedItems();
        for (MealVO mealVO : mealList) {
            Log.d(TAG, "selected item: " + mealVO.getMealId() + ", " + mealVO.getTitle());
        }

    }

    @Override
    public void onBackPressed() {
        if (mBookmarkMealListAdapter.isEditMode()) {
            setEditMode(false);
        } else {
            super.onBackPressed();
        }
    }

    private void setLayout() {

        mRecyclerView = findViewById(R.id.recyclerview);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("북마크 식단");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });


    }

    @Override
    public void onItemChecked() {
        List<MealVO> selectedList = mBookmarkMealListAdapter.getSelectedItems();
        int selectedSize = selectedList.size();
        tvDelete.setText(String.format("삭제(%d)", selectedSize));

        if (selectedSize == 0) {
            //검은색쓰레기통 이미지변경
            iconDelete.setBackgroundResource(R.drawable.delete);
        } else {
            //불들어오는 쓰레기통 이미지변경
            iconDelete.setBackgroundResource(R.drawable.delete_purple);
        }

        if (selectedSize == mBookmarkMealListAdapter.getItemCount()) {
            btnAllSelect.setBackgroundResource(R.drawable.checked_purple);
            layoutAllSelect.setSelected(true);
        } else {
            btnAllSelect.setBackgroundResource(R.drawable.checked_gray);
            layoutAllSelect.setSelected(false);
        }
    }

    /* 식단 다수 선택 삭제 통신 */
    private void deleteBookmarkMealListService() {
        showProgress();
//        bookmark insert
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        List<String> selectedItems = new ArrayList<>();
        List<MealVO> mealList = mBookmarkMealListAdapter.getSelectedItems();
        for (MealVO mealVO : mealList) {
            selectedItems.add(mealVO.getMealId() + "");
        }

        Map<String, Object> param = new HashMap<>();
//        {target_id}, #{target_type}, #{member_id}
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        param.put("list", selectedItems);

        Log.d(TAG, "deleteBookmarkMealListService: param: " + param);

        RestService service = retrofit.create(RestService.class);
        final Call<List<MealVO>> deleteBookmarkRequest = service.deleteMealBookmarkList(param);

        deleteBookmarkRequest.enqueue(new Callback<List<MealVO>>() {
            @Override
            public void onResponse(Call<List<MealVO>> call, Response<List<MealVO>> response) {

                mBookmarkMealList = response.body();
                if (mBookmarkMealList != null) {

                    mBookmarkMealListAdapter.setData(mBookmarkMealList);
                    tvCount.setText("총" + mBookmarkMealList.size() + "개");

                    // TODO: Item 이 하나도 없을 경우 처리.
                    if (mBookmarkMealList.size() == 0) {
                        frame_empty.setVisibility(View.VISIBLE);

                    }else {
                        frame_empty.setVisibility(View.GONE);

                    }
                }
                setEditMode(false);
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<MealVO>> call, Throwable t) {
                Log.e(TAG, "onFailure: postVideoBookmarkService rest 통신실패");
                hideProgress();
                setEditMode(false);
            }
        });
    }



    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.show(this);
        }
    }


    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
    }

}
