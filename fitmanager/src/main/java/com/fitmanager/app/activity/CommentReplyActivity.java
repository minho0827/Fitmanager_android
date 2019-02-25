package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.fitmanager.app.adapter.CommentReplyAdapter;
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


public class CommentReplyActivity extends AppCompatActivity {


    private static final String TAG = "CommentReplyActivity";
    private CommentReplyAdapter mCommentReplyAdapter;
    private List<CommentVO> mCommentItems = new ArrayList<>();
    RecyclerView mRecyclerView;
    private int mCommentId;
    private int mTargetId;
    private int mTargetType;
    ImageButton mImgBtnfinish;
    EditText mEditComment;
    TextView topbarTitle;
    TextView mCommentTotalCount;
    Button mBtnInsert;
    private Context mContext;
    LinearLayout mLinear_header;
    FrameLayout mFrameLayout;
    FrameLayout mBackAreaFrameLayout;

    @BindView(R.id.layout_root)
    View layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_comment_reply);
        ButterKnife.bind(this);
        getIntentInit();
        setLayout();
        topbarTitle.setText("댓글");
        Log.i(TAG, "isLogin" + LoginUtils.isLoggedIn());
        mCommentReplyAdapter = new CommentReplyAdapter(getApplicationContext(), mCommentItems);
        mRecyclerView.setAdapter(mCommentReplyAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (position == 0) {
                            return;
                        }

                        final CommentVO commentVO = mCommentReplyAdapter.getIteamAt(position);
                        ImageButton btnMore = (ImageButton) view.findViewById(R.id.btn_more);
                        if (btnMore != null) {
                            btnMore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (LoginUtils.isLoggedIn() &&
                                            LoginUtils.getLoginUserVO().getMemberId() == commentVO.getMemberId()) {
                                        boolean deleteable = (commentVO.getCommentCount() == 0);
                                        openOptionMenu(v, position, deleteable);
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
                if (LoginUtils.isLoggedIn() == false) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    LoginUtils.showLoginDialog(mContext);
                } else {
                    insertComment();
                }
            }
        });

        mEditComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {

                Log.d(TAG, "onFocusChange: hasFocus: " + hasFocus);

                if (!hasFocus) {
                    Utils.hideKeyboard(v, mContext);
                    return;
                }

                if (LoginUtils.isLoggedIn() == false) {
                    v.clearFocus();
                    layoutRoot.requestFocus();
                    Utils.hideKeyboard(v, mContext);
                    LoginUtils.showLoginDialog(mContext);
                }
            }
        });

        if (LoginUtils.isLoggedIn()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditComment, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void insertComment() {
        String comment = mEditComment.getText().toString();
        if (StringUtils.isBlank(comment)) {
            Toast.makeText(mContext, "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("parentId", mCommentId);
        map.put("targetId", mTargetId);
        map.put("targetType", mTargetType);
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
        final Call<Integer> insertCommentReplyCall = service.insertReplyComment(param);

        insertCommentReplyCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                mEditComment.setEnabled(true);
                mEditComment.setText("");

                Integer result = response.body();
                if (result > 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("parentId", mCommentId);
                    map.put("targetId", mTargetId);
                    map.put("targetType", mTargetType);
                    getReplyCommentListRestService(map);
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
        mImgBtnfinish = (ImageButton) findViewById(R.id.imgbtn_finish);
        mEditComment = (EditText) findViewById(R.id.edit_replycomment);
        mBtnInsert = (Button) findViewById(R.id.btn_insert);
        mCommentTotalCount = (TextView) findViewById(R.id.comment_total_count);
        topbarTitle = (TextView) findViewById(R.id.topbar_title);
        mLinear_header = (LinearLayout) findViewById(R.id.comment_header);
        mFrameLayout = (FrameLayout) findViewById(R.id.frameLayout_comment_empty);
        mBackAreaFrameLayout = (FrameLayout) findViewById(R.id.back_area_frame_layout);
    }

    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTargetId = bundle.getInt("targetId");
        mTargetType = bundle.getInt("targetType");
        mCommentId = bundle.getInt("comment_id");
        Log.d(TAG, "targetId : " + mTargetId);
        Log.d(TAG, "targetType : " + mTargetType);
        Log.d(TAG, "comment_id : " + mCommentId);
    }

    protected void onResume() {
        super.onResume();
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("targetId", mTargetId);
        commentMap.put("targetType", mTargetType);
        commentMap.put("commentId", mCommentId);
        getRestComment(commentMap);
    }

    private void getRestComment(Map<String, Object> param) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<CommentVO> replyComment = service.getComment(param);

        replyComment.enqueue(new Callback<CommentVO>() {
            @Override
            public void onResponse(Call<CommentVO> call, Response<CommentVO> response) {
                final CommentVO commentVO = response.body();
                if (commentVO != null) {
                    mCommentReplyAdapter.setFirstCommentData(commentVO);

                    Map<String, Object> commentReplyMap = new HashMap<>();
                    commentReplyMap.put("targetId", mTargetId);
                    commentReplyMap.put("targetType", mTargetType);
                    commentReplyMap.put("parentId", mCommentId);
                    getReplyCommentListRestService(commentReplyMap);
                } else {

                }
            }

            @Override
            public void onFailure(Call<CommentVO> call, Throwable t) {

                Log.e(TAG, "## onFailure: " + t.getMessage());

            }
        });
    }

    private void getReplyCommentListRestService(Map<String, Object> param) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<List<CommentVO>> commentReplyList = service.getCommentReplyList(param);

        commentReplyList.enqueue(new Callback<List<CommentVO>>() {
            @Override
            public void onResponse(Call<List<CommentVO>> call, Response<List<CommentVO>> response) {
                mCommentItems = response.body();

                mLinear_header.setVisibility(View.VISIBLE);
                mFrameLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                if (mCommentItems == null) {
                    mCommentItems = Collections.EMPTY_LIST;
                }
                if (CollectionUtils.isNotEmpty(mCommentItems)) {
                    mCommentTotalCount.setText(mCommentItems.size() + "");
                    mCommentReplyAdapter.setData(mCommentItems);
                } else {
                    mCommentTotalCount.setText(mCommentItems.size() + "");
                    mCommentReplyAdapter.setData(mCommentItems);

                }

            }

            @Override
            public void onFailure(Call<List<CommentVO>> call, Throwable t) {

                Log.e(TAG, "## onFailure: " + t.getMessage());
            }
        });
    }

    private void openOptionMenu(View v, final int position, final boolean deleteable) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.comment_option_menu_items, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        if (deleteable) {
                            deleteRestService(mCommentItems.get(position - 1).getCommentId());
                        } else {
                            Toast.makeText(getBaseContext(), "댓글이 있어서 삭제가 불가능합니다", Toast.LENGTH_SHORT).show();
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
                mEditComment.setText("");

                Integer result = response.body();
                if (result > 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("targetId", mTargetId);
                    map.put("targetType", mTargetType);
                    getReplyCommentListRestService(map);
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


}
