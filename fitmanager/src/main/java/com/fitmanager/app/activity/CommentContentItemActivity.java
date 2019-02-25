package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fitmanager.app.R;
import com.fitmanager.app.adapter.ContentCommentAdapter;
import com.fitmanager.app.listener.RecyclerItemClickListener;
import com.fitmanager.app.model.CommentVO;
import com.fitmanager.app.model.NotiVO;
import com.fitmanager.app.network.CommentRestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.LoginUtils;
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

public class CommentContentItemActivity extends AppCompatActivity {
    private List<CommentVO> mCommentItems = new ArrayList<>();

    private ContentCommentAdapter mContentCommentAdapter;

    private static final String TAG = "CommentContentItemActivity";
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    private int mTargetId;
    private int mTargetType;
    private int mCommentId;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit_comment)
    EditText mEditComment;
    @BindView(R.id.btn_insert)
    Button mBtnInsert;

    @BindView(R.id.content_image)
    ImageView contentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_comment_item);
        ButterKnife.bind(this);
        getIntentInit();
        setLayout();
        mContentCommentAdapter = new ContentCommentAdapter(getApplicationContext(), mCommentItems);
        mRecyclerView.setAdapter(mContentCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (position == 0) {
                            return;
                        }

                        final CommentVO commentVO = mContentCommentAdapter.getIteamAt(position);
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


        mBtnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtils.isLoggedIn() == false) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    LoginUtils.showLoginDialog(getApplicationContext());
                } else {
                    insertComment();
                }
            }
        });
    }

    private void insertComment() {
        String comment = mEditComment.getText().toString();
        if (StringUtils.isBlank(comment)) {
            Toast.makeText(getApplicationContext(), "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "댓글 입력이 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "댓글 입력이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void setLayout() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("내댓글");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

    }

    private void getIntentInit() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTargetId = bundle.getInt("targetId");
        mCommentId = bundle.getInt("commentId");
        mTargetType = bundle.getInt("targetType");
        Log.i(TAG, "targetId : " + mTargetId);
        Log.i(TAG, "commentId : " + mCommentId);
        Log.i(TAG, "targetType : " + mTargetType);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("targetId", mTargetId);
        commentMap.put("targetType", mTargetType);
        commentMap.put("commentId", mCommentId);
        getMyCommentContent();
        getRestComment(commentMap);
    }


    private void getMyCommentContent() {
        Map<String, Object> param = new HashMap<>();
        param.put("notiMemberId", LoginUtils.getLoginUserVO().getMemberId());
        param.put("targetId", mTargetId);
        param.put("targetType", mTargetType);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<NotiVO> replyComment = service.getMyCommentContent(param);

        replyComment.enqueue(new Callback<NotiVO>() {
            @Override
            public void onResponse(Call<NotiVO> call, Response<NotiVO> response) {
                final NotiVO notiVO = response.body();
                if (notiVO != null) {
                    title.setText(notiVO.getTitle());
                    Glide.with(getApplicationContext())
                            .load(notiVO.getThumbnail())
                            .centerCrop()
                            .crossFade()
                            .into(contentImage);

                } else {

                }
            }

            @Override
            public void onFailure(Call<NotiVO> call, Throwable t) {

                Log.e(TAG, "## onFailure: " + t.getMessage());

            }
        });
    }

    private void getRestComment(Map<String, Object> commentMap) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentRestService service = retrofit.create(CommentRestService.class);
        final Call<CommentVO> replyComment = service.getComment(commentMap);

        replyComment.enqueue(new Callback<CommentVO>() {
            @Override
            public void onResponse(Call<CommentVO> call, Response<CommentVO> response) {
                final CommentVO commentVO = response.body();
                if (commentVO != null) {
                    mContentCommentAdapter.setFirstCommentData(commentVO);

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
                if (mCommentItems == null) {
                    mCommentItems = Collections.EMPTY_LIST;
                }
                if (CollectionUtils.isNotEmpty(mCommentItems)) {
                    mContentCommentAdapter.setData(mCommentItems);
                }

            }

            @Override
            public void onFailure(Call<List<CommentVO>> call, Throwable t) {

                Log.e(TAG, "## onFailure: " + t.getMessage());
            }
        });
    }


}
