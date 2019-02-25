package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.adapter.CommentListAdapter;
import com.fitmanager.app.listener.RecyclerItemClickListener;
import com.fitmanager.app.model.CommentVO;
import com.fitmanager.app.network.CommentRestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";
    private CommentListAdapter mCommentListAdapter;
    private List<CommentVO> mCommentItems = new ArrayList<>();
    RecyclerView mRecyclerView;
    private int mTargetId;
    private int mTargeType;
    EditText editComment;
    ImageButton imgBtnfinish;
    Button mBtnInsert;
    TextView mCommentTotalCount;
    private Context mContext;
    private Toolbar mToolbar;
    FrameLayout frameLayoutComment;
    LinearLayout linear_recyclerView;
    @BindView(R.id.back_area_frame_layout)
    FrameLayout mBackAreaFrameLayout;
    @BindView(R.id.topbar_title)
    TextView tvTobbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "## onCreate...");
        mContext = this;
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        getIntentInit();
        setLayout();
        tvTobbarTitle.setText("댓글");
        setSupportActionBar(mToolbar);
        mCommentListAdapter = new CommentListAdapter(getApplicationContext(), mCommentItems);
        mRecyclerView.setAdapter(mCommentListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        final CommentVO commentVO = mCommentListAdapter.getItemAt(position);
                        ImageButton btnMore = (ImageButton) view.findViewById(R.id.btn_more);
                        if (btnMore != null) {
                            btnMore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (LoginUtils.isLoggedIn() &&
                                            LoginUtils.getLoginUserVO().getMemberId() == commentVO.getMemberId()) {
                                        boolean deletable = (commentVO.getCommentCount() == 0);
                                        openOptionMenu(v, position, deletable);
                                    } else {
                                        openOptionMenuAnonymous(v, position);
                                    }
                                }
                            });
                        }
                    }
                })
        );
        mBackAreaFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mBtnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertComment();
            }
        });


        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "## onClick...");
                if (LoginUtils.isLoggedIn()) {
                    Utils.showKeyboard(v,mContext);
                } else {
                    Utils.hideKeyboard(v, mContext);
                    LoginUtils.showLoginDialog(mContext);
                }
            }
        });
    }


    private void openOptionMenu(View v, final int position, final boolean deletable) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.comment_option_menu_items, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        if (deletable) {
                            deleteRestService(mCommentItems.get(position).getCommentId());
                        } else {
                            Toast.makeText(getBaseContext(), "댓글이 있어서 삭제가 불가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                Toast.makeText(getBaseContext(), "You selected the action : "
                        + item.getTitle() + " position " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }

    private void deleteRestService(int commentId) {
        Map<String, Object> param = new HashMap<>();
        param.put("commentId", commentId);
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<Integer> deleteRestServiceCall = service.deleteComment(param);

        deleteRestServiceCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                editComment.setText("");

                Integer result = response.body();
                if (result > 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("targetId", mTargetId);
                    map.put("targetType", mTargeType);
                    getRestService(map);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
//                mEditComment.setEnabled(true);
                Toast.makeText(mContext, "댓글 입력이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void openOptionMenuAnonymous(View v, final int position) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.non_members_option_menu_items, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.copy:
                        mCommentItems.get(position).getCommentId();
                        break;
                }
                Toast.makeText(getBaseContext(), "You selected the action : "
                        + item.getTitle() + " position " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }

    private void insertComment() {
        String comment = editComment.getText().toString();
        if (StringUtils.isBlank(comment)) {
            Toast.makeText(mContext, "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("targetId", mTargetId);
        map.put("targetType", mTargeType);
        map.put("content", comment);
        map.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        postCommentInsertService(map);

    }

    private void postCommentInsertService(final Map<String, Object> param) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<Integer> insertCommentCall = service.insertComment(param);

        insertCommentCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                mEditComment.setEnabled(true);
                editComment.setText("");


                Integer result = response.body();
                if (result > 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("targetId", mTargetId);
                    map.put("targetType", mTargeType);
                    getRestService(map);

                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
//                mEditComment.setEnabled(true);
                Toast.makeText(mContext, "댓글 입력이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void setLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        imgBtnfinish = (ImageButton) findViewById(R.id.imgbtn_finish);
        editComment = (EditText) findViewById(R.id.edit_comment);
        mBtnInsert = (Button) findViewById(R.id.btn_insert);
        mCommentTotalCount = (TextView) findViewById(R.id.comment_total_count);
        frameLayoutComment = (FrameLayout) findViewById(R.id.frameLayout_comment_empty);
        linear_recyclerView = (LinearLayout) findViewById(R.id.linear_recyclerview);


    }

    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTargetId = bundle.getInt("targetId");
        mTargeType = bundle.getInt("targetType");
        Log.d(TAG, "mealId : " + mTargetId);
        Log.d(TAG, "targetType : " + mTargeType);
    }

    protected void onResume() {
        super.onResume();
        Map<String, Object> map = new HashMap<>();
        map.put("targetId", mTargetId);
        map.put("targetType", mTargeType);
        getRestService(map);
    }

    private void getRestService(Map<String, Object> param) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<List<CommentVO>> commentList = service.getCommentList(param);

        commentList.enqueue(new Callback<List<CommentVO>>() {
            @Override
            public void onResponse(Call<List<CommentVO>> call, Response<List<CommentVO>> response) {
                mCommentItems = response.body();

                if (mCommentItems == null) {
                    mCommentItems = Collections.EMPTY_LIST;
                }

                if (CollectionUtils.isNotEmpty(mCommentItems)) {
                    linear_recyclerView.setVisibility(View.VISIBLE);
                    frameLayoutComment.setVisibility(View.GONE);
                } else {
                    linear_recyclerView.setVisibility(View.GONE);
                    frameLayoutComment.setVisibility(View.VISIBLE);
                }

                mCommentListAdapter.setData(mCommentItems, mTargetId, mTargeType);
                mCommentTotalCount.setText(mCommentItems.size() + "");

                editComment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
            }

            @Override
            public void onFailure(Call<List<CommentVO>> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }

}
